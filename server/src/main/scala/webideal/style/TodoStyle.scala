package webideal
package style

import scalacss.Defaults._

trait TodoStyle extends StyleSheet.Standalone with Style {
  import dsl._
  
  val name = "todo"
}
object TodoStyle extends TodoStyle