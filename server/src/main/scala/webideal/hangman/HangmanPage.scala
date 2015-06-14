package webideal
package hangman

import akka.stream.FlowMaterializer
import akka.actor.ActorSystem

trait HangmanPage extends Page {
  def apply()(implicit sys: ActorSystem, mat: FlowMaterializer) = {
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