package webideal

import scala.scalajs.js
import js.Dynamic.{ global => g }

object IndexJs extends js.JSApp {
  def main(): Unit = {
    g.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
  }
}