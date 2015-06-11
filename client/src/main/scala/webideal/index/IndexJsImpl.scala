package webideal
package index

import scala.scalajs.js.Dynamic.global

object IndexJsImpl extends IndexJs {
  def run(): Unit =
    global.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
}