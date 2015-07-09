package webideal

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.actor.ActorSystem

trait Page extends Directives with util.ScalaTagsSupport with util.ScalaCssSupport {
  def apply()(implicit sys: ActorSystem, mat: Materializer): Route
}