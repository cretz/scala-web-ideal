package webideal

import prickle._
import scalatags.Text.all._
import scala.reflect.ClassTag

trait View {
  lazy val titleTag = "title".tag[String]
  
  def css(path: String) = link(rel := "stylesheet", media := "screen", href := path)
  
  def scriptPath(path: String) = script(src := path, tpe := "text/javascript")
  
  def jsModule[T <: JsModule](implicit tag: ClassTag[T]) =
    script(raw(tag.runtimeClass.getName + "Impl().run();"))
  
  def jsModuleWithParams[T <: JsModuleWithParams](params: T#ParamType)
      (implicit tag: ClassTag[T], pickler: Pickler[T#ParamType]) =
    script(raw(tag.runtimeClass.getName + "Impl().runWithParams(" + Pickle.intoString(params) + ");"))
}