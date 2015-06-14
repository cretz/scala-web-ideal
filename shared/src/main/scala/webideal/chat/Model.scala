package webideal
package chat

import prickle._

object Model {
  case class User(username: String, avatar: String)
  
  sealed trait CommandMessage
  case class TalkCommand(message: String) extends CommandMessage
  case class QuitCommand() extends CommandMessage
  
  implicit val commandPickler = CompositePickler[CommandMessage].
    concreteType[TalkCommand].
    concreteType[QuitCommand]
  
  sealed trait NotifyMessage
  case class NotifyUserExists() extends NotifyMessage
  case class NotifyJoin(user: User) extends NotifyMessage
  case class NotifyTalk(user: User, message: String) extends NotifyMessage
  case class NotifyQuit(user: User) extends NotifyMessage
  
  implicit val notifyPickler = CompositePickler[NotifyMessage].
    concreteType[NotifyUserExists].
    concreteType[NotifyJoin].
    concreteType[NotifyTalk].
    concreteType[NotifyQuit]
}