package webideal
package todos

import akka.stream.FlowMaterializer
import akka.actor.ActorSystem

trait TodoPage extends Page {
  import TodoPage._
  
  def apply()(implicit sys: ActorSystem, mat: FlowMaterializer) = {
    pathEnd {
      get {
        extractRequestContext { implicit ctx =>
          complete(TodoView())
        }
      }
    } ~
    path(separateOnSlashes(TodoStyle.relativePath)) {
      get(complete(TodoStyle))
    } ~
    post {
      import util.PrickleAutowireSupport._
      import sys.dispatcher
      completeWithAutowire(AutowireServer.route[TodoHandler](handler))
    }
  }
}
object TodoPage extends TodoPage {
  // Yeah, global as in other demo, ug...look in to DI such as macwire
  val handler = new InMemoryTodoHandler
  handler.update(Task(Some(1), "Upgrade Scala JS", true))
  handler.update(Task(Some(2), "Make it Rx", false))
  handler.update(Task(Some(3), "Make this example useful", false))
}