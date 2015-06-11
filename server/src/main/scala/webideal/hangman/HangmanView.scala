package webideal
package hangman

import scalatags.Text.all._
import akka.http.scaladsl.server.RequestContext

trait HangmanView extends View {
  def apply()(implicit ctx: RequestContext) =
    MainTemplate(
      titleText = "Hangman",
      footer = Seq(jsModule[HangmanJs])
    )
}
object HangmanView extends HangmanView