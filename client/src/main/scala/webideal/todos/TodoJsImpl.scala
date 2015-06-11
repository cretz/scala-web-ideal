package webideal
package todos

import org.scalajs.dom
import rx._
import scalatags.JsDom.all._
import scalatags.JsDom.tags2.section
import scala.concurrent.Future
import autowire._
import scalajs.concurrent.JSExecutionContext.Implicits.queue

// A lot of this copied directly from https://github.com/hussachai/play-scalajs-showcase/blob/1c4e622958a0a79949d2c316bbee402fa8db0087/example-client/src/main/scala/example/TodoJS.scala
// I did a few cleanups and moved it to use autowire
object TodoJsImpl extends TodoJs {
  import util.ScalaRxSupport._

  def run(): Unit =
    Model.init.foreach { _ =>
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
          Model.create(inputBox.value)
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
          val target = Model.tasks().exists(!_.done)
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
      p("Original version created by ", a(href := "https://github.com/lihaoyi/workbench-example-app/blob/todomvc/src/main/scala/example/ScalaJSExample.scala")("Li Haoyi")),
      p("Modified version with database backend can be found ", a(href := "https://github.com/hussachai/play-scalajs-showcase")("here")),
      p("Ported to all-scala ", a(href := "https://github.com/cretz/scala-web-ideal")("here"))
    )

  def partList = Rx {
    ul(id := "todo-list")(
      for (task <- Model.tasks() if Model.filters(Model.filter())(task)) yield {
        val inputRef = input(`class` := "edit", value := task.text).render

        li(
          `class` := Rx{
            if (task.done) "completed"
            else if (Model.editing() == Some(task)) "editing"
            else ""
          },
          div(`class` := "view")(
            "ondblclick".attr := { () =>
              Model.editing() = Some(task)
            },
            input(`class`:= "toggle", `type`:= "checkbox", cursor:= "pointer", onchange:= { () =>
                Model.update(task.copy(done = !task.done))
              }, if (task.done) checked := true else ""
            ),
            label(task.text),
            button(
              `class` := "destroy",
              cursor := "pointer",
              onclick := { () => Model.delete(task.id) }
            )
          ),
          form(
            onsubmit := { () =>
              Model.update(task.copy(text = inputRef.value))
              Model.editing() = None
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
      span(id:="todo-count")(strong(Model.notDone), " item left"),
      ul(id := "filters")(
        for ((name, pred) <- Model.filters.toSeq) yield {
          li(a(
            `class` := Rx{
              if(name == Model.filter()) "selected"
              else ""
            },
            name,
            href := "#",
            onclick := {() => Model.filter() = name}
          ))
        }
      ),
      button(
        id := "clear-completed",
        onclick := { () => Model.clearCompletedTasks },
        "Clear completed (", Model.done, ")"
      )
    )

  object Model {
    val tasks = Var(Seq.empty[Task])
    val done = Rx { tasks().count(_.done) }
    val notDone = Rx { tasks().length - done() }
    val editing = Var(Option.empty[Task])
    val filter = Var("All")
    val filters = Map[String, Task => Boolean](
      ("All", t => true),
      ("Active", !_.done),
      ("Completed", _.done)
    )
    
    lazy val client = util.PrickleAutowireClient("/todos/")
    def handler = client[TodoHandler]
  
    def init: Future[Unit] = handler.all().call().map(tasks() = _)
  
    def all: Seq[Task] = tasks()
  
    def create(text: String, done: Boolean = false) =
      handler.create(Task(None, text, done)).call().foreach(tasks() +:= _)
  
    def update(task: Task) =
      handler.update(task).call().foreach { _ =>
        tasks() = tasks().updated(tasks().indexWhere(_.id == task.id), task)
      }
  
    def delete(id: Option[Long]) =
      id.foreach(l => handler.delete(Seq(l)).call().foreach { _ =>
        tasks() = tasks().filterNot(_.id == id)
      })
  
    def clearCompletedTasks =
      handler.clearCompleted().call.foreach(_ => tasks() = tasks().filterNot(_.done))
  }
}