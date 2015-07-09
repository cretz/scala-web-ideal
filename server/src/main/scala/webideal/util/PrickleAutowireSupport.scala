package webideal
package util

import prickle._
import akka.stream.Materializer
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.model.StatusCodes

trait PrickleAutowireSupport extends PrickleExtras {
  val AutowireServer = new autowire.Server[String, Unpickler, Pickler] with PrickleAutowireSerializers { }
    
  def completeWithAutowire[T](router: autowire.Core.Router[String])(implicit mat: Materializer) = {
    import Directives._
    path(Segments) { s =>
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