package webideal

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Route
import akka.stream.FlowMaterializer
import scala.concurrent.ExecutionContext

trait Page extends Directives with util.ScalaTagsSupport with util.ScalaCssSupport {
  def apply()(implicit ec: ExecutionContext, mat: FlowMaterializer): Route
}