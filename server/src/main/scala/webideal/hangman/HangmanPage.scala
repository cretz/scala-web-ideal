package webideal
package hangman

import akka.stream.FlowMaterializer
import scala.concurrent.ExecutionContext

trait HangmanPage extends Page {
  def apply()(implicit ec: ExecutionContext, mat: FlowMaterializer) = {
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
      withHangmanHandler { handler =>
        completeWithAutowire(AutowireServer.route[HangmanHandler](handler))
      }
    }
  }
}
object HangmanPage extends HangmanPage