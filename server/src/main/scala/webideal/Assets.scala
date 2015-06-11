package webideal

object Assets {
  // TODO: make this a config
  val mainJavascriptLocalPath = "../client/build/js/scala-web-ideal.js"
  val mainJavascriptRemotePath = "assets/webideal/javascripts/scala-web-ideal.js"
  
  // TODO: These could have a macro to check whether they are valid
  def public(relativePath: String): String = s"/assets/webideal/$relativePath"
  def webJar(webJar: String, partialPath: String): String = s"/assets/$webJar/$partialPath"
  
  def style(style: Style): String = style.path
}