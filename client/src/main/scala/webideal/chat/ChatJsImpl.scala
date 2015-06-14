package webideal
package chat

import prickle._
import org.scalajs.dom.raw.EventSource
import scala.scalajs.js
import js.Dynamic.{ global => g }
import org.scalajs.dom
import scalatags.JsDom._
import scalatags.JsDom.all._
import org.scalajs.jquery.{jQuery=>$}
import autowire._
import scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.Success
import scala.util.Failure
import scala.util.Try

// Mostly taken from https://github.com/hussachai/play-scalajs-showcase/blob/e130bf5112c5cdd26485ea620988bad4b6589fc3/example-client/src/main/scala/example/ChatJS.scala
object ChatJsImpl extends ChatJs with JsModuleWithPrickleParams {
  import Model._

  implicit val paramsUnpickler = Unpickler.materializeUnpickler[ChatJs.Settings]
  
  val maxMessages = 20

  var assetsDir = ""
  var wsBaseUrl = ""
  var client: Option[ChatClient] = None
  
  def run(settings: ChatJs.Settings) = {
    this.assetsDir = settings.assetsDir
    this.wsBaseUrl = settings.wsBaseUrl

    val content = dom.document.getElementById("content")
    content.appendChild(signInPanel.render)
    content.appendChild(chatPanel.render)
    ready
  }

  def ready = {
    $("#message").keypress((e: dom.KeyboardEvent) => 
      if(!e.shiftKey && e.keyCode == 13) {
        e.preventDefault()
        client.map(_.send(TalkCommand($("#message").value.toString)))
        $("#message").value("")
      }
    )
  }

  def signInPanel = div(id := "signInPanel")(
    form(`class` := "form-inline", "role".attr := "form")(
      div(id := "usernameForm", `class` := "form-group")(
        div(`class` := "input-group")(
          div(`class` := "input-group-addon", raw("&#9786;")),
          input(id := "username", `class` := "form-control", `type` := "text", placeholder := "Enter username")
        )
      ),
      span(style := "margin:0px 5px"),
      select(id := "channel", `class` := "form-control")(
        option(value := "0", "WebSocket"),
        option(value := "1", "Server-Sent Events")
      ),
      span(style := "margin:0px 5px"),
      button(`class` := "btn btn-default", onclick := { () =>
        val input = $("#username").value().toString.trim
        if (input.isEmpty) {
          $("#usernameForm").addClass("has-error")
          dom.alert("Invalid username")
        } else {
          $("#usernameForm").removeClass("has-error")
          client = ChatClient.connect(wsBaseUrl, input).map { c =>
            $("#loginAs").text("Login as: " + c.username)
            $("#username").value("")
            $("#signInPanel").addClass("hide")
            $("#chatPanel").removeClass("hide")
            c
          }
        }
        false
      })("Sign in")
    )
  )

  def chatPanel = div(id := "chatPanel", `class` := "hide")(
    div(`class` := "row", style := "margin-bottom: 10px;")(
      div(`class` := "col-md-12", style := "text-align: right;")(
        span(id := "loginAs", style := "padding: 0px 10px;"),
        button(`class` := "btn btn-default", onclick := { () => signOut }, "Sign out")
      )
    ),
    div(`class` := "panel panel-default")(
      div(`class` := "panel-heading")(
        h3(`class` := "panel-title")("Chat Room")
      ),
      div(`class` := "panel-body")(
        div(id := "messages")
      ),
      div(`class` := "panel-footer")(
        textarea(id:="message", `class` := "form-control message", placeholder := "Say something")
      )
    )
  )

  def createMessage(msg: String, username: String, avatar: String) = {
    val cls = "row message-box" + (if (client.map(_.username) == Some(username)) "-me" else "")
    div(`class` := cls)(
      div(`class` := "col-md-2")(
        div(`class` := "message-icon")(
          img(src := s"$assetsDir/images/avatars/$avatar", `class` := "img-rounded"),
          div(username)
        )
      ),
      // TODO: XSS attack found in other demo...we don't care enough right now :-)
      div(`class` := "col-md-10")(raw(msg))
    )
  }

  def signOut = {
    client.map { c => c.send(QuitCommand()); c.close() }
    $("#signInPanel").removeClass("hide")
    $("#chatPanel").addClass("hide")
    $("#messages").html("")
  }

  trait ChatClient {
    def username: String
    def send(msg: CommandMessage): Unit
    def close(): Unit
  }

  object ChatClient {
    def connect(url: String, username: String): Option[ChatClient] = {
      Try({
        $("#channel").value.toString match {
          case "0" if g.window.WebSocket.toString != "undefined" =>
            Some(new WSChatClient(url, username))
          case "1" if g.window.EventSource.toString != "undefined" =>
            Some(new SSEChatClient(username))
          case _ => None
        }
      }).recover({ case e =>
        dom.alert("Unable to connect because "+e.toString)
        None
      }).toOption.flatten
    }

    def receive(e: dom.MessageEvent): Unit = {
      val msgElem = dom.document.getElementById("messages")
      Unpickle[NotifyMessage].fromString(e.data.toString) match {
        case Success(NotifyUserExists()) =>
          dom.alert("Username already exists")
          signOut
        case Success(NotifyJoin(User(username, avatar))) =>
          msgElem.appendChild(createMessage("**joined**", username, avatar).render)
        case Success(NotifyTalk(User(username, avatar), msg)) =>
          msgElem.appendChild(createMessage(msg, username, avatar).render)
        case Success(NotifyQuit(User(username, avatar))) =>
          msgElem.appendChild(createMessage("**left**", username, avatar).render)
        case Failure(e) =>
          dom.alert("Failure: " + e)
          signOut
      }
      if(msgElem.childNodes.length >= maxMessages){
        msgElem.removeChild(msgElem.firstChild)
      }
      msgElem.scrollTop = msgElem.scrollHeight
    }
  }

  class WSChatClient(url: String, val username: String) extends ChatClient {
    val socket = new dom.WebSocket(url + "/" + username)
    socket.onmessage = ChatClient.receive _

    override def send(msg: CommandMessage): Unit =
      socket.send(Pickle.intoString(msg))

    override def close() = socket.close()
  }

  class SSEChatClient(val username: String) extends ChatClient {
    import util.PrickleExtras._
    
    lazy val client = util.PrickleAutowireClient("/chat/")
    def handler = client[ChatHandler]
    
    val sse = new EventSource(s"/chat/sse/$username")
    sse.onmessage = ChatClient.receive _

    override def send(msg: CommandMessage): Unit = msg match {
      case TalkCommand(str) => handler.talk(username, str).call()
      case QuitCommand() => handler.quit(username).call()
    } 

    override def close() = sse.close()
  }
}