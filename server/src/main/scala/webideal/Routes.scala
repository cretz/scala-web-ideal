package webideal

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Route

object Routes extends (() => Route) with Directives with ScalaTagsSupport {
  def apply() = {
    path(PathEnd) {
      get {
        complete(view.IndexView(SharedMessages.itWorks))
      }
    }
  }
}