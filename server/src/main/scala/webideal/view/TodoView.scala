package webideal
package view

import style.TodoStyle
import scalatags.Text.all._
import akka.http.scaladsl.server.RequestContext

trait TodoView extends View {
  def apply(implicit ctx: RequestContext) = {
    MainTemplate(
      titleText = "Todos",
      header = Seq(css(Assets.style(TodoStyle))),
      footer = Seq(script(raw("webideal.todo.TodoJs().main()")))
    )
  }
}