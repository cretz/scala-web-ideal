package webideal

import scala.scalajs.js.Dynamic.global

object IndexJsImpl extends IndexJs {
  def main(): Unit = {
    org.scalajs.dom.console.log("YAY1")
    global.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
  }
}