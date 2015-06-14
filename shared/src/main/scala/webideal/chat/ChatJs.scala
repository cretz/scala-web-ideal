package webideal
package chat

trait ChatJs extends JsModuleWithParams {
  type ParamType = ChatJs.Settings
}
object ChatJs {
  case class Settings(
    assetsDir: String,
    wsBaseUrl: String
  )
}