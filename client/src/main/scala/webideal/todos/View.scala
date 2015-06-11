package webideal
package todos

import org.scalajs.dom
import rx._
import scalatags.JsDom.all._
import scalatags.JsDom.tags2.section
import scalajs.concurrent.JSExecutionContext.Implicits.runNow

class View(model: Model) {
  import Framework._
  
  def init(): Unit =
    model.init.foreach { _ =>
      dom.document.getElementById("content").appendChild(
        section(id := "todoapp")(
          templateHeader,
          templateBody,
          templateFooter
        ).render
      )
    }

  val inputBox = input(
    id := "new-todo",
    placeholder := "What needs to be done?",
    autofocus := true
  ).render

  def templateHeader =
    header(id:="header")(
      form(
        inputBox,
        onsubmit := { () =>
          model.create(inputBox.value)
          inputBox.value = ""
          false
        }
      )
    )

  def templateBody =
    section(id:="main")(
      input(
        id := "toggle-all",
        `type` := "checkbox",
        cursor := "pointer",
        onclick := { () =>
          val target = model.tasks().exists(!_.done)
          sys.error("Left out of other demo")
        }
      ),
      label(`for` := "toggle-all", "Mark all as complete"),
      partList,
      partControls
    )

  def templateFooter =
    footer(id := "info")(
      p("Double-click to edit a todo"),
      p("Original version created by ", a(href:="https://github.com/lihaoyi/workbench-example-app/blob/todomvc/src/main/scala/example/ScalaJSExample.scala")("Li Haoyi")),
      p("Modified version with database backend can be found ", a(href:="https://github.com/hussachai/play-scalajs-showcase")("here")),
      p("Ported to all-scala ", a(href:="https://github.com/cretz/scala-web-ideal")("here"))
    )

  def partList = Rx {
    ul(id := "todo-list")(
      for (task <- model.tasks() if model.filters(model.filter())(task)) yield {
        val inputRef = input(`class` := "edit", value := task.text).render

        li(
          `class` := Rx{
            if (task.done) "completed"
            else if (model.editing() == Some(task)) "editing"
            else ""
          },
          div(`class` := "view")(
            "ondblclick".attr := { () =>
              model.editing() = Some(task)
            },
            input(`class`:= "toggle", `type`:= "checkbox", cursor:= "pointer", onchange:= { () =>
                model.update(task.copy(done = !task.done))
              }, if (task.done) checked := true else ""
            ),
            label(task.text),
            button(
              `class` := "destroy",
              cursor := "pointer",
              onclick := { () => model.delete(task.id) }
            )
          ),
          form(
            onsubmit := { () =>
              model.update(task.copy(text = inputRef.value))
              model.editing() = None
              false
            },
            inputRef
          )
        )
      }
    )
  }

  def partControls =
    footer(id:="footer")(
      span(id:="todo-count")(strong(model.notDone), " item left"),
      ul(id := "filters")(
        for ((name, pred) <- model.filters.toSeq) yield {
          li(a(
            `class` := Rx{
              if(name == model.filter()) "selected"
              else ""
            },
            name,
            href := "#",
            onclick := {() => model.filter() = name}
          ))
        }
      ),
      button(
        id := "clear-completed",
        onclick := { () => model.clearCompletedTasks },
        "Clear completed (", model.done, ")"
      )
    )
}