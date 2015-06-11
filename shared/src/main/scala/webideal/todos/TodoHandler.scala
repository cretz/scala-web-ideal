package webideal
package todos

import scala.concurrent.Future

trait TodoHandler {
  def all(): Future[Seq[Task]]
  def create(task: Task): Future[Task]
  def update(task: Task): Future[Boolean]
  def delete(ids: Seq[Long]): Future[Boolean]
  def clearCompleted(): Future[Int]
}