package webideal

import prickle._
import akka.stream.FlowMaterializer
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.model.StatusCodes

trait PrickleAutowireSupport {
  val AutowireServer = new autowire.Server[String, Unpickler, Pickler] with PrickleAutowireSerializers { }
    
  def completeWithAutowire[T](router: autowire.Core.Router[String])(implicit mat: FlowMaterializer) = {
    import Directives._
    path(Segments) { s =>
      println("SEGS", s)
      entity(as[String]) { str =>
        complete {
          router {
            autowire.Core.Request(s, Unpickle[Map[String, String]].fromString(str).get)
          }
        }
      }
    }
  }
}
object PrickleAutowireSupport extends PrickleAutowireSupport