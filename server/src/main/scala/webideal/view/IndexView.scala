package webideal
package view

import scalatags.Text.all._

object IndexView extends View {
  def apply(message: String) =
    MainTemplate(
      "Trivial Example",
      Seq(
        // TODO: static-ify this a bit more
        scriptPath(Assets.public("javascripts/scalajs-example-launcher.js"))
      ),
      Seq(
        p(
          "Akka with Scala.js kickstart akin to",
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
      )
    )
}