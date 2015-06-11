package webideal

import scala.scalajs.js.annotation._

@JSExportDescendentObjects
trait JsModule {
  @JSExport
  def run(): Unit
  
  // TODO: some kind of check to make sure there is an Impl version of this
}