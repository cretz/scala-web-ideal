package webideal

import akka.http.scaladsl.model.MediaTypes
import akka.http.scaladsl.model.ContentType
import akka.http.scaladsl.marshalling._
import scalacss.mutable.StyleSheet
import scalacss.Defaults._

trait ScalaCssSupport {
  val cssContentType = ContentType(MediaTypes.`text/css`)

  implicit def scalaCssMarshaller: ToEntityMarshaller[StyleSheet.Base] =
    Marshaller.StringMarshaller.wrap(cssContentType)(_.render)
}
object ScalaVssSupport extends ScalaCssSupport