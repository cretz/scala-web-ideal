package webideal
package chat

import prickle._
import scalatags.Text.all._
import akka.http.scaladsl.server.RequestContext
import akka.http.scaladsl.model.Uri.Path

trait ChatView extends View {
  def apply()(implicit ctx: RequestContext) = {
    MainTemplate(
      titleText = "Server Push Chat",
      header = Seq(css(Assets.relativeStyle(ctx, ChatStyle))),
      footer = Seq(jsModuleWithParams[ChatJs](ChatJs.Settings(
        assetsDir = Assets.assetsDir,
        wsBaseUrl = ctx.request.uri.copy(path = Path("/chat/ws"), scheme = "ws").toString
      )))
    )
  }
}
object ChatView extends ChatView