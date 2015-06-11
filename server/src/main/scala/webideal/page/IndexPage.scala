package webideal
package page

import akka.stream.FlowMaterializer
import scala.concurrent.ExecutionContext

trait IndexPage extends Page {
  def apply()(implicit ec: ExecutionContext, mat: FlowMaterializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx =>
        complete(view.IndexView(SharedMessages.itWorks))
      }
    }
  }
}
object IndexPage extends IndexPage