package webideal

import akka.http.scaladsl.server.RequestContext

object Assets {
  // TODO: make this a config
  val mainJavascriptLocalPath = "../client/build/js/scala-web-ideal.js"
  val mainJavascriptRemotePath = "assets/webideal/javascripts/scala-web-ideal.js"
  
  val assetsDir = "/assets/webideal"
  
  // TODO: These could have a macro to check whether they are valid
  def public(relativePath: String): String = s"$assetsDir/$relativePath"
  def webJar(webJar: String, partialPath: String): String = s"/assets/$webJar/$partialPath"
  
  def style(style: Style): String = assetsDir + "/" + style.relativePath
  def relativeStyle(ctx: RequestContext, style: Style): String =
    (ctx.request.uri.path + "/" + style.relativePath).toString
}