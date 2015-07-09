package webideal
package hangman

import akka.stream.Materializer
import akka.actor.ActorSystem

trait HangmanPage extends Page {
  def apply()(implicit sys: ActorSystem, mat: Materializer) = {
    pathEnd {
      get {
        extractRequestContext { implicit ctx =>
          complete(HangmanView())
        }
      }
    } ~
    post {
      import util.PrickleAutowireSupport._
      import InSessionHangmanHandler._
      import sys.dispatcher
      withHangmanHandler { handler =>
        completeWithAutowire(AutowireServer.route[HangmanHandler](handler))
      }
    }
  }
}
object HangmanPage extends HangmanPage