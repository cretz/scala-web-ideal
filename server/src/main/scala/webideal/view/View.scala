package webideal
package view

import scalatags.Text.all._

trait View {
  lazy val titleTag = "title".tag[String]
  
  def css(path: String) = link(rel := "stylesheet", media := "screen", href := path)
  
  def scriptPath(path: String) = script(src := path, tpe := "text/javascript")
}