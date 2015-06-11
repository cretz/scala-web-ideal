package webideal
package util

import scalatags.Text.TypedTag
import akka.http.scaladsl.model.MediaTypes
import akka.http.scaladsl.model.ContentType
import akka.http.scaladsl.marshalling._

trait ScalaTagsSupport {
  val htmlContentType = ContentType(MediaTypes.`text/html`)
  
  implicit def scalaTagsMarshaller: ToEntityMarshaller[TypedTag[String]] =
    Marshaller.StringMarshaller.wrap(htmlContentType)(_.toString)
}
object ScalaTagsSupport extends ScalaTagsSupport