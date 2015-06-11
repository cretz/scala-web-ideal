package webideal

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Route
import org.webjars.WebJarAssetLocator
import akka.http.scaladsl.server.RequestContext
import akka.stream.FlowMaterializer
import scala.concurrent.ExecutionContext

object Routes extends Directives with ScalaTagsSupport with ScalaCssSupport {
  val webJarLocator = new WebJarAssetLocator()
  
  def apply()(implicit ec: ExecutionContext, mat: FlowMaterializer): Route = {
    // Some things to support the application
    path(separateOnSlashes(Assets.mainJavascriptRemotePath)) {
      getFromFile(Assets.mainJavascriptLocalPath)
    } ~
    // TODO: modularize the CSS better than putting it right here in Routes
    path(style.Style.baseDir / (style.MainStyle.name + ".css")) {
      get {
        complete(style.MainStyle)
      }
    } ~
    path(style.Style.baseDir / (style.TodoStyle.name + ".css")) {
      get {
        complete(style.TodoStyle)
      }
    } ~
    path("static" / Segment / Rest) { (webJar, partialPath) =>
      getFromResource(webJarLocator.getFullPath(webJar, partialPath))
    } ~
    // The different pages
    pathPrefix("todos") { page.TodoPage() } ~
    path(PathEnd) { page.IndexPage() }
  }
}