package webideal

import scalatags.Text.all._
import scala.reflect.ClassTag

trait View {
  lazy val titleTag = "title".tag[String]
  
  def css(path: String) = link(rel := "stylesheet", media := "screen", href := path)
  
  def scriptPath(path: String) = script(src := path, tpe := "text/javascript")
  
  def jsModule[T <: JsModule](implicit tag: ClassTag[T]) =
    script(raw(tag.runtimeClass.getName + "Impl().run()"))
}