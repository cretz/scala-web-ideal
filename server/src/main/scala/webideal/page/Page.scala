package webideal
package page

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Route
import akka.stream.FlowMaterializer
import scala.concurrent.ExecutionContext

trait Page extends Directives with ScalaTagsSupport with ScalaCssSupport {
  def apply()(implicit ec: ExecutionContext, mat: FlowMaterializer): Route
}