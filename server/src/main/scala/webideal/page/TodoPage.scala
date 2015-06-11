package webideal
package page

import webideal.todos._
import akka.stream.FlowMaterializer
import akka.http.scaladsl.model.StatusCodes
import scala.concurrent.ExecutionContext

trait TodoPage extends Page {
  import TodoPage._
  
  def apply()(implicit ec: ExecutionContext, mat: FlowMaterializer) = {
    import PrickleAutowireSupport._
    pathEnd {
      get {
        extractRequestContext { implicit ctx =>
          complete(view.TodoView())
        }
      }
    } ~
    completeWithAutowire(AutowireServer.route[TodoHandler](handler))
  }
}
object TodoPage extends TodoPage {
  // Yeah, global as in other demo, ug...look in to DI such as macwire
  val handler = new InMemoryTodoHandler
  handler.update(Task(Some(1), "Upgrade Scala JS", true))
  handler.update(Task(Some(2), "Make it Rx", false))
  handler.update(Task(Some(3), "Make this example useful", false))
}