package webideal

import prickle._
import scala.scalajs.js.annotation._

@JSExportDescendentObjects
trait JsModuleWithParams {
  type ParamType
  
  @JSExport
  def runWithParams(params: Any): Unit
  
  def run(params: ParamType): Unit
}