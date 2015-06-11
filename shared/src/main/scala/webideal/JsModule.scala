package webideal

import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.annotation.JSExportDescendentObjects

@JSExportDescendentObjects
trait JsModule {
  @JSExport
  def run(): Unit
  
  // TODO: some kind of check to make sure there is an Impl version of this
}