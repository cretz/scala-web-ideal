package webideal
package util

import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.unmarshalling._
import prickle._
import akka.http.scaladsl.model.ContentTypes._
import akka.stream.FlowMaterializer

trait PrickleMarshallingSupport extends PrickleExtras {
  implicit def prickleMarshaller[T](implicit p: Pickler[T]): ToEntityMarshaller[T] =
    Marshaller.StringMarshaller.wrap(`application/json`)(Pickle.intoString(_))

  implicit def prickleUnmarshaller[T](implicit u: Unpickler[T], mat: FlowMaterializer): FromEntityUnmarshaller[T] =
    Unmarshaller.stringUnmarshaller.forContentTypes(`application/json`).map(Unpickle[T].fromString(_).get)
}
object PrickleMarshallingSupport extends PrickleMarshallingSupport