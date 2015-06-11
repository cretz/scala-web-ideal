package webideal
package hangman

import scala.concurrent.Future

trait HangmanHandler {
  def start(level: Int): Future[Game]
  def resume(): Future[Option[Game]]
  def guess(chr: Char): Future[Game]
  def quit(): Future[Unit]
}