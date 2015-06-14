package webideal
package chat

import scalacss.Defaults._

trait ChatStyle extends StyleSheet.Standalone with Style {
  import dsl._
  
  val name = "chat"

  "#messages" - (
    maxHeight(250 px),
    overflowY.auto,
    overflowX.hidden
  )
  
  "div.message-box" - padding(10 px, 0 px)
  
  "div.message-box-me" - (
    padding(10 px, 0 px),
    backgroundColor("#b6e2ff")
  )
  
  "div.message-icon" - (
    textAlign.center,
    padding(2 px, 5 px)
  )
  
  "textarea.message" - (
    resize.vertical,
    width(100 %%)
  )
}
object ChatStyle extends ChatStyle