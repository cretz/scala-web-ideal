package webideal
package util

import akka.http.scaladsl.model.MediaTypes
import akka.http.scaladsl.model.ContentType
import akka.http.scaladsl.marshalling._
import scalacss.mutable.StyleSheet
import scalacss.Renderer
import scalacss.Css
import scalacss.Env
import scalacss.StringRenderer
import scalacss.CssEntry

trait ScalaCssSupport {
  val cssContentType = ContentType(MediaTypes.`text/css`)
  
  // See https://github.com/japgolly/scalacss/issues/47
  implicit val inOrderRenderer = new Renderer[String] {
    override def apply(css: Css): String = {
      val sb = new StringBuilder
      val fmt = StringRenderer.formatPretty()(sb)
      css.foreach { case CssEntry(mq, sel, content) =>
        mq.foreach(fmt.mqStart)
        fmt.selStart(mq, sel)
        fmt.kv1(mq, content.head)
        content.tail.foreach(fmt.kvn(mq, _))
        fmt.selEnd(mq, sel)
        mq.foreach(fmt.mqEnd)
      }
      fmt.done()
      sb.toString()
    }
  }
  implicit val env = Env.empty

  implicit def scalaCssMarshaller: ToEntityMarshaller[StyleSheet.Base] =
    Marshaller.StringMarshaller.wrap(cssContentType)(_.render)
}
object ScalaCssSupport extends ScalaCssSupport