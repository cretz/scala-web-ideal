package webideal
package upload

import prickle._
import scalatags.Text.all._
import akka.http.scaladsl.server.RequestContext
import akka.http.scaladsl.model.Uri.Path

trait UploadView extends View {
  def apply()(implicit ctx: RequestContext) = {
    MainTemplate(
      titleText = "HTML5 File Drag & Drop API",
      footer = Seq(jsModule[UploadJs])
    )
  }
}
object UploadView extends UploadView