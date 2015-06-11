package webideal
package index

import akka.stream.FlowMaterializer
import scala.concurrent.ExecutionContext
import akka.http.scaladsl.marshalling.ToResponseMarshallable.apply
import akka.http.scaladsl.server.Directive.addByNameNullaryApply
import akka.http.scaladsl.server.Directive.addDirectiveApply

trait IndexPage extends Page {
  def apply()(implicit ec: ExecutionContext, mat: FlowMaterializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx =>
        complete(IndexView(SharedMessages.itWorks))
      }
    }
  }
}
object IndexPage extends IndexPage