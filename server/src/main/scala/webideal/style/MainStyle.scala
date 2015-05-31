package webideal
package style

import scalacss.Defaults._
import scalacss.Color

class MainStyle extends StyleSheet.Standalone with Style {
  import dsl._
  
  // Move down content because we have a fixed navbar that is 50px tall
  "body" - (
    paddingTop(50 px)
  )
  
  ".sub-header" - (
    paddingBottom(10 px),
    // TODO: remove Color() - https://github.com/japgolly/scalacss/issues/42
    borderBottom(1 px, solid, Color("#eee"))
  )
  
  // TODO: the rest
}