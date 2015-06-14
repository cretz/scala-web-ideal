package webideal
package chat

import prickle._
import autowire._
import akka.stream.FlowMaterializer
import akka.stream.scaladsl.Flow
import akka.http.scaladsl.model.ws.Message
import akka.actor.ActorSystem
import akka.http.scaladsl.model.ws.TextMessage
import de.heikoseeberger.akkasse.EventStreamMarshalling
import de.heikoseeberger.akkasse.ServerSentEvent
import akka.stream.scaladsl.Source

trait ChatPage extends Page with EventStreamMarshalling {
  import ChatPage._
  
  def apply()(implicit sys: ActorSystem, mat: FlowMaterializer) = {
    pathEnd {
      get {
        extractRequestContext { implicit ctx =>
          complete(ChatView())
        }
      }
    } ~
    path("ws" / Rest) { username =>
      handleWebsocketMessages(websocketFlow(chatRoom, username))
    } ~
    path("sse" / Rest) { username =>
      complete {
        import sys.dispatcher
        sseSource(chatRoom, username)
      }
    } ~
    post {
      import util.PrickleAutowireSupport._
      import sys.dispatcher
      completeWithAutowire(AutowireServer.route[ChatHandler](chatRoom))
    }
  }
  
  def websocketFlow(chatRoom: ChatRoom, username: String): Flow[Message, Message, Unit] = {
    import Model._
    Flow[Message].
      collect({
        case TextMessage.Strict(msg) => Unpickle[CommandMessage].fromString(msg).get
      }).
      via(chatRoom.chatFlow(username)).
      map(msg => TextMessage.Strict(Pickle.intoString(msg)))
  }
  
  def sseSource(chatRoom: ChatRoom, username: String): Source[ServerSentEvent, Unit] = {
    import Model._
    chatRoom.chatNotifySource(username).
      map(msg => ServerSentEvent(Pickle.intoString(msg)))
  }
}
object ChatPage extends ChatPage {
  // TODO: define best practices on shared state
  // For this demo we don't care about the race condition making multiple rooms
  @volatile var _chatRoom = Option.empty[ChatRoom]
  def chatRoom(implicit sys: ActorSystem): ChatRoom = _chatRoom.getOrElse {
    _chatRoom = Some(new ChatRoom)
    _chatRoom.get
  }
}