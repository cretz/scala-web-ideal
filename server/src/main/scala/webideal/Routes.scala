package webideal

import akka.http.scaladsl.server.Route
import org.webjars.WebJarAssetLocator
import akka.http.scaladsl.server.RequestContext
import akka.stream.FlowMaterializer
import scala.concurrent.ExecutionContext
import akka.http.scaladsl.server.RejectionHandler
import akka.http.scaladsl.model.StatusCodes
import scala.util.Try
import scala.util.Success

object Routes extends Page {
  val webJarLocator = new WebJarAssetLocator()
  
  def apply()(implicit ec: ExecutionContext, mat: FlowMaterializer): Route = {
    // Some things to support the application
    path(separateOnSlashes(Assets.mainJavascriptRemotePath)) {
      getFromFile(Assets.mainJavascriptLocalPath)
    } ~
    path(separateOnSlashes(MainStyle.path)) {
      get(complete(MainStyle))
    } ~
    path(separateOnSlashes(todos.TodoStyle.path)) {
      get(complete(todos.TodoStyle))
    } ~
    pathPrefix("assets" / "webideal") {
      getFromResourceDirectory("webideal/assets")
    } ~
    path("assets" / Segment / Rest) { (webJar, partialPath) =>
      Try(webJarLocator.getFullPath(webJar, partialPath)) match {
        case Success(path) => getFromResource(path)
        case _ => complete(StatusCodes.NotFound)
      }
    } ~
    // The different pages
    path(PathEnd) { index.IndexPage() } ~
    pathPrefix("todos") { todos.TodoPage() } ~
    pathPrefix("hangman") { hangman.HangmanPage() }
  }
}