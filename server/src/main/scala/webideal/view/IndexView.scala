package webideal
package view

import scalatags.Text.all._
import akka.http.scaladsl.server.RequestContext

trait IndexView extends View {
  def apply(message: String)(implicit ctx: RequestContext) =
    MainTemplate(
      titleText = "Trivial Example",
      content = Seq(
        p(
          "Akka with Scala.js kickstart akin to ",
          a(
            href := "https://github.com/vmunier/play-with-scalajs-example",
            target := "_blank",
            "Vincent Munier"
          )
        ),
        h3("Akka and Scala.js share a same message"),
        ul(
          li("Akka shouts out: ", em(message)),
          li("Scala.js shouts out: ", em(id := "scalajsShoutOut"))
        )
      ),
      footer = Seq(jsModule[IndexJs])
    )
}
object IndexView extends IndexView