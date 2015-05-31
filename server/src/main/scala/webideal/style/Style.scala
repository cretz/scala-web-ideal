package webideal
package style

import scalacss.mutable.StyleSheet

trait Style { this: StyleSheet.Base =>
  def name: String
  
  def path: String = Style.baseDir + '/' + name + ".css"
}
object Style {
  // TODO: make this a config please
  val baseDir = "stylesheets"
}