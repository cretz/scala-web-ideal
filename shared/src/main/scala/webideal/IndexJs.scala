package webideal

import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.annotation.JSExportDescendentObjects

@JSExportDescendentObjects
trait IndexJs {
  @JSExport
  def main(): Unit
  
  def className = getClass.getName
}