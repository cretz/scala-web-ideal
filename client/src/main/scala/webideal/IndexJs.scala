package webideal

import scala.scalajs.js
import js.Dynamic.{ global => g }
import scala.scalajs.js.annotation.JSExport

@JSExport
object IndexJs {
  @JSExport
  def main(): Unit = {
    g.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
  }
}