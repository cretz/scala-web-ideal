package webideal

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Route
import org.webjars.WebJarAssetLocator

object Routes extends Directives with ScalaTagsSupport with ScalaCssSupport {
  val webJarLocator = new WebJarAssetLocator()
  
  def apply(): Route = {
    path(PathEnd) {
      get {
        complete(view.IndexView(SharedMessages.itWorks))
      }
    } ~
    path(separateOnSlashes(Assets.mainJavascriptRemotePath)) {
      getFromFile(Assets.mainJavascriptLocalPath)
    } ~
    path(style.Style.baseDir / (style.MainStyle.name + ".css")) {
      get {
        complete(style.MainStyle)
      }
    } ~
    path("static" / Segment / Rest) { (webJar, partialPath) =>
      getFromResource(webJarLocator.getFullPath(webJar, partialPath))
    }
  }
}