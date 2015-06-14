package webideal

import akka.http.scaladsl.server.Route
import org.webjars.WebJarAssetLocator
import akka.http.scaladsl.server.RequestContext
import akka.stream.FlowMaterializer
import akka.http.scaladsl.server.RejectionHandler
import akka.http.scaladsl.model.StatusCodes
import scala.util.Try
import scala.util.Success
import akka.actor.ActorSystem

object Routes extends Page {
  val webJarLocator = new WebJarAssetLocator()
  
  def apply()(implicit sys: ActorSystem, mat: FlowMaterializer): Route = {
    // Some things to support the application
    path(separateOnSlashes(Assets.mainJavascriptRemotePath)) {
      getFromFile(Assets.mainJavascriptLocalPath)
    } ~
    path(separateOnSlashes(MainStyle.path)) {
      get(complete(MainStyle))
    } ~
    // TODO: move these styles relative to their pages
    path(separateOnSlashes(todos.TodoStyle.path)) {
      get(complete(todos.TodoStyle))
    } ~
    path(separateOnSlashes(chat.ChatStyle.path)) {
      get(complete(chat.ChatStyle))
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
    pathPrefix("hangman") { hangman.HangmanPage() } ~
    pathPrefix("chat") { chat.ChatPage() }
  }
}