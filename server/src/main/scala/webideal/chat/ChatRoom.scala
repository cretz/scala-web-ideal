package webideal
package chat

import scala.concurrent.Future
import akka.actor._
import java.io.File
import scala.util.Random
import akka.stream.OverflowStrategy
import akka.stream.scaladsl._

// Lots of help from https://github.com/jrudolph/akka-http-scala-js-websocket-chat
class ChatRoom(implicit val system: ActorSystem) extends ChatHandler {
  import Model._
  import ChatRoom._
  
  val avatars = {
    new File(getClass.getResource("/webideal/assets/images/avatars").toURI).list
  }
  
  val chatActor = system.actorOf(Props(new Actor {
    var usersByName = Map.empty[String, (User, ActorRef)]
    var usersByActor = Map.empty[ActorRef, User]
    
    def receive = {
      case Noop => ()
      case Subscribe(username, ref) =>
        if (usersByName.contains(username)) {
          ref ! NotifyUserExists()
          ref ! PoisonPill
        } else {
          val user = User(username, avatars(Random.nextInt(avatars.length)))
          usersByActor += ref -> user
          usersByName += username -> (user, ref)
          context.watch(ref)
          self ! Broadcast(NotifyJoin(user))
        }
      case Broadcast(msg) =>
        usersByActor.keys.foreach(_ ! msg)
      case Command(username, TalkCommand(msg)) =>
        usersByName.get(username).foreach(user => self ! Broadcast(NotifyTalk(user._1, msg)))
      case Command(username, QuitCommand()) =>
        usersByName.get(username).map { case (user, actor) =>
          context.unwatch(actor)
          usersByActor -= actor
          usersByName -= username
          self ! Broadcast(NotifyQuit(user))
        }
      case Terminated(ref) =>
        usersByActor.get(ref).map { user =>
          usersByActor -= ref
          usersByName -= user.username
          self ! Broadcast(NotifyQuit(user))
        }
    }
  }))
  
  def talk(username: String, msg: String): Future[Unit] = {
    chatActor ! Command(username, TalkCommand(msg))
    Future.successful(())
  }
  
  def quit(username: String): Future[Unit] = {
    chatActor ! Command(username, QuitCommand())
    Future.successful(())
  }
  
  def chatInSink() = Sink.actorRef[ServerMessage](chatActor, Noop)
  val chatOutSource = Source.actorRef[NotifyMessage](1, OverflowStrategy.fail)
  
  def chatFlow(username: String): Flow[CommandMessage, NotifyMessage, ActorRef] = {
    Flow(chatInSink(), chatOutSource)(Keep.right) { implicit b =>
      (chatActorIn, chatActorOut) =>
        import FlowGraph.Implicits._                     
        val enveloper = b.add(Flow[CommandMessage].map(Command(username, _)))
        val merge = b.add(Merge[ServerMessage](2))
        enveloper ~> merge.in(0)
        b.materializedValue ~> Flow[ActorRef].map(Subscribe(username, _)) ~> merge.in(1)
        merge ~> chatActorIn
        (enveloper.inlet, chatActorOut.outlet)
    }
  }
  
  def chatNotifySource(username: String): Source[NotifyMessage, Unit] =
    Source.actorRef[NotifyMessage](1, OverflowStrategy.fail).
      mapMaterializedValue(chatActor ! Subscribe(username, _))
}
object ChatRoom {
  import Model._

  sealed trait ServerMessage
  case object Noop extends ServerMessage
  case class Subscribe(username: String, ref: ActorRef) extends ServerMessage
  case class Broadcast(msg: NotifyMessage) extends ServerMessage
  case class Command(username: String, cmd: CommandMessage) extends ServerMessage
}