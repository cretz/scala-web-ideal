package webideal
package view

import scalatags.Text.all._

object IndexView extends View {
  def apply(message: String) =
    MainTemplate(
      "Trivial Example",
      Seq.empty,
      Seq(
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
      // See if we can find a cool way to statically type this. One way might be to
      //  have a local JsExport file on the server side and make a shared mutable variable
      //  that is exported and make the actual JsApp main bind it
      Seq(script(raw("webideal.IndexJs().main()")))
    )
}