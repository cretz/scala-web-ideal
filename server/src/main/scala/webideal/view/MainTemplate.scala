package webideal
package view

import scalatags.Text.all._
import java.nio.charset.StandardCharsets
import akka.http.scaladsl.model.MediaTypes
import akka.http.scaladsl.model.HttpMethods

object MainTemplate extends View {
  def apply(
    titleText: String,
    header: Seq[Modifier] = Seq.empty,
    content: Seq[Modifier] = Seq.empty
  ) =
    html(
      head(
        meta(charset := StandardCharsets.UTF_8.name.toLowerCase),
        titleTag(s"Scala Web Ideal Showcase - $titleText"),
        link(
          rel := "shortcut icon",
          tpe := MediaTypes.`image/png`.value,
          href := Assets.public("images/favicon.png")
        ),
        css(Assets.webJar("bootstrap", "bootstrap.min.css")),
        // TODO: make the main.css also typesafe instead of a string
        css(Assets.public("stylesheets/main.css")),
        scriptPath(Assets.webJar("jquery", "jquery.min.js")),
        scriptPath(Assets.webJar("bootstrap", "bootstrap.min.js")),
        header
      ),
      body(
        div(cls := "navbar navbar-inverse navbar-fixed-top", role := "navigation")(
          div(cls := "container-fluid")(
            div(cls := "navbar-header")(
              button(
                tpe := "button",
                cls := "navbar-toggle",
                data("Toggle") := "collapse",
                data("Target") := ".navbar-collapse"
              )(
                span(cls := "sr-only", "Toggle navigation"),
                span(cls := "icon-bar"),
                span(cls := "icon-bar"),
                span(cls := "icon-bar")
              ),
              a(cls := "navbar-brand", href := "https://github.com/cretz/scala-web-ideal", "Scala Web Ideal")
            ),
            div(cls := "navbar-collapse collapse")(
              ul(cls := "nav navbar-nav navbar-right")(
                li(a(href := "http://www.scala-lang.org/", target := "_blank", "Scala")),
                li(a(href := "http://www.scala-js.org/", target := "_blank", "Scala.js")),
                li(a(href := "http://www.akka.io/", target := "_blank", "Akka"))
              ),
              form(
                cls := "navbar-form navbar-right",
                action := "https://github.com/search",
                method := HttpMethods.GET.name,
                target := "_blank"
              )(
                input(tpe := "hidden", name := "nwo", value := "cretz/scala-web-ideal"),
                input(tpe := "hidden", name := "search_target", value := "repository"),
                input(tpe := "hidden", name := "ref", value := "cmdform"),
                input(tpe := "text", cls := "form-control", name := "q", placeholder := "Search...Repo")
              )
            )
          )
        ),
        div(cls := "container-fluid")(
          div(cls := "col-sm-3 col-md-2 sidebar")(
            ul(cls := "nav nav-sidebar")(
              // TODO: generate this based on some "page" type of model
              li(id := "home-page", a(href := "application", "Hello")),
              li(cls := "menu", a(href := "todos", "Reactive TodoMVC")),
              li(cls := "menu", a(href := "hangman", "Reactive Hangman")),
              li(cls := "menu", a(href := "upload", "HTML5 File Upload")),
              li(cls := "menu", a(href := "chat", "Server Push Chat"))
            )
          ),
          div(cls := "col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main")(
            h1(cls := "page-header", titleText),
            div(id := "content"),
            div(content)
          )
        )
      )
    )
}