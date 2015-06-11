package webideal
package hangman

import akka.http.scaladsl.server.RequestContext
import scala.concurrent.Future
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.model.headers.HttpCookie
import akka.http.scaladsl.model.DateTime
import scala.io.Source
import scala.util.Random
import scala.util.Try

class InSessionHangmanHandler(var game: Option[InSessionHangmanHandler.GameWithWord]) extends HangmanHandler {
  import InSessionHangmanHandler._
  
  var deleteGame = false
  
  override def start(level: Int): Future[Game] = {
    val word = words(Random.nextInt(words.length))
    game = Some(GameWithWord(word, Game(level, "_" * word.length)))
    Future.successful(game.get.sharedGame)
  }
  
  override def resume(): Future[Option[Game]] = Future.successful(game.map(_.sharedGame))
  
  override def guess(chr: Char): Future[Game] = Future.fromTry(Try {
    var guess = game.get.sharedGame.guess
    game.get.word.zipWithIndex.collect { case (c, idx) if c == chr => guess = guess.updated(idx, chr) }
    val misses = game.get.sharedGame.misses + (if (guess == game.get.sharedGame.guess) 1 else 0)
    val won = guess.indexOf('_') == -1
    val gameOverWord =
      if (won || misses >= game.get.sharedGame.level) Some(game.get.word)
      else None
    game = Some(
      game.get.copy(
        sharedGame = game.get.sharedGame.copy(
          guess = guess,
          misses = misses,
          won = won,
          gameOverWord = gameOverWord
        )
      )
    )
    game.get.sharedGame
  })
  
  override def quit(): Future[Unit] = {
    deleteGame = true
    game = None
    Future.successful(())
  }
}
object InSessionHangmanHandler {
  lazy val words = Source.fromInputStream(getClass.getResourceAsStream("words.txt")).mkString.
    split("[\\s,]+").filter(word => (word.length > 5 && word.forall(Character.isLetter)))
  
  case class GameWithWord(word: String, sharedGame: Game) {
    def marshal(): String = {
      import prickle._
      Pickle.intoString(this)
    }
  }
  object GameWithWord {
    def apply(str: String): Option[GameWithWord] = {
      import prickle._
      Unpickle[GameWithWord].fromString(str).toOption
    }
  }
  
  import akka.http.scaladsl.server.Directives._
  
  val cookieName = "hangman"
  
  def withHangmanHandler: Directive1[HangmanHandler] = {
    optionalCookie(cookieName).flatMap { cookie =>
      val handler = new InSessionHangmanHandler(
        cookie.flatMap(c => GameWithWord(util.CryptoSupport.decrypt(c.content)))
      )
      val originalGame = handler.game
      mapResponseHeaders({ headers =>
        import akka.http.scaladsl.model.headers._
        if (handler.deleteGame) {
          headers :+ `Set-Cookie`(HttpCookie(cookieName, "deleted", expires = Some(DateTime.MinValue)))
        } else if (handler.game.isDefined && handler.game != originalGame) {
          headers :+ `Set-Cookie`(
            HttpCookie(
              name = cookieName,
              content = util.CryptoSupport.encrypt(handler.game.get.marshal)
            )
          )
        } else {
          headers
        }
      }) & provide(handler.asInstanceOf[HangmanHandler])
    }
  }
}