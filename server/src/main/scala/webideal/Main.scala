package webideal

import akka.stream.ActorFlowMaterializer
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import scala.io.StdIn

object Main extends App {
  
  import system.dispatcher
  
  println("Starting server!")
  
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorFlowMaterializer()
  
  import ScalaTagsSupport._
  
  val route = {
    (path(PathEnd) & get) {
      complete(view.IndexView(SharedMessages.itWorks))
    }
  }
  
  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
 
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()
 
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ ⇒ system.shutdown())
}