package webideal
package todos

import scala.concurrent.Future
import rx._
import autowire._
import scalajs.concurrent.JSExecutionContext.Implicits.queue

class Model {
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
  
  lazy val client = PrickleAutowireClient("/todos/")
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