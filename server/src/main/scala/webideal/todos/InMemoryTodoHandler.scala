package webideal
package todos

import java.util.concurrent.atomic.AtomicLong
import scala.collection.immutable.TreeMap
import scala.concurrent.Future

class InMemoryTodoHandler extends TodoHandler {
  val seq = new AtomicLong
  var vals = TreeMap.empty[Long, Task]
  
  override def all() = Future.successful(vals.values.toSeq)
  
  override def create(task: Task) =
    Future.successful(task.copy(id = Some(seq.incrementAndGet)))

  override def update(task: Task) =
    Future.successful(task.id.map(vals += _ -> task).isDefined)

  // Other demo had this as always returning true
  override def delete(ids: Seq[Long]) = Future.successful { vals --= ids; true }
  
  // We don't guarantee any atomicness here for this demo by intention
  override def clearCompleted() = Future.successful {
    val initialSize = vals.size
    vals = vals.filterNot(_._2.done)
    vals.size - initialSize
  }
}