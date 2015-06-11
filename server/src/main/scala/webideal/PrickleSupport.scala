package webideal

import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.unmarshalling._
import prickle._
import akka.http.scaladsl.model.ContentTypes._
import akka.stream.FlowMaterializer
import akka.http.scaladsl.server.Directives

trait PrickleSupport {
  lazy val autowireServer = new autowire.Server[String, Unpickler, Pickler] with PrickleAutowireSerializers { }
  
  implicit def prickleMarshaller[T](implicit p: Pickler[T]): ToEntityMarshaller[T] =
    Marshaller.StringMarshaller.wrap(`application/json`)(Pickle.intoString(_))

  implicit def prickleUnmarshaller[T](implicit u: Unpickler[T], mat: FlowMaterializer): FromEntityUnmarshaller[T] =
    Unmarshaller.stringUnmarshaller.forContentTypes(`application/json`).map(Unpickle[T].fromString(_).get)

  def completeWithAutowire[T](handler: T)(implicit mat: FlowMaterializer) = {
    import Directives._
    path(Segments) { s =>
      entity(as[String]) { str =>
        complete {
          autowireServer.route[T](handler) {
            autowire.Core.Request(s, Unpickle[Map[String, String]].fromString(str).get)
          }
        }
      }
    }
  }
}
object PrickleSupport extends PrickleSupport