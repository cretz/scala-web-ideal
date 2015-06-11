package webideal

import akka.stream.ActorFlowMaterializer
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import scala.io.StdIn

object Main extends App {
  
  println("Starting server!")
  
  implicit val system = ActorSystem("my-system")
  import system.dispatcher
  implicit val materializer = ActorFlowMaterializer()
  
  val bindingFuture = Http().bindAndHandle(Routes(), "localhost", 8080)
 
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()

  import system.dispatcher
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ ⇒ system.shutdown())
}