package webideal

import style.Style

object Assets {
  // TODO: make this a config
  val mainJavascriptLocalPath = "../client/build/js/scala-web-ideal.js"
  val mainJavascriptRemotePath = "javascripts/scala-web-ideal.js"
  
  // TODO
  def public(relativePath: String): String = relativePath 
  
  // This should be a macro to check for presence at compile time
  // TODO
  def webJar(webJar: String, partialPath: String): String = "static/" + webJar + "/" + partialPath
  
  def style(style: Style): String = style.path
}