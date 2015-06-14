package webideal
package chat

import scala.concurrent.Future

trait ChatHandler {
  // For this demo there is no username check here, free rein to cheat
  def talk(username: String, msg: String): Future[Unit]
  def quit(username: String): Future[Unit]
}