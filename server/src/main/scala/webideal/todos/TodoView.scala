package webideal
package todos

import scalatags.Text.all._
import akka.http.scaladsl.server.RequestContext

trait TodoView extends View {
  def apply()(implicit ctx: RequestContext) = {
    MainTemplate(
      titleText = "Todos",
      header = Seq(css(Assets.relativeStyle(ctx, TodoStyle))),
      footer = Seq(jsModule[TodoJs])
    )
  }
}
object TodoView extends TodoView