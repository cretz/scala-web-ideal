package webideal

import scalacss.Attr
import scalacss.Transform
import scalacss.CanIUse
import scalacss.Literal
import scalacss.Defaults._

trait Style { this: scalacss.mutable.StyleSheet.Base =>
  def name: String
  
  def relativePath: String = "stylesheets/" + name + ".css"
  
  // Fills for things not in ScalaCSS
  val appearance = Attr.real("appearance", Transform keys CanIUse.appearance)
  val fontSmooth = Attr.real("font-smoothing", Transform keys Style.fontSmoothUse)
  object antialiased extends Literal("antialiased")
}
object Style {
  val fontSmoothUse: CanIUse.Subject = {
    import CanIUse.Agent._
    import CanIUse.Support._
    Map(
      AndroidBrowser    -> Set(Unsupported),
      AndroidChrome     -> Set(Unsupported),
      AndroidFirefox    -> Set(Unsupported),
      AndroidUC         -> Set(Unsupported),
      BlackberryBrowser -> Set(Unsupported),
      Chrome            -> Set(FullX),
      Firefox           -> Set(FullX),
      IE                -> Set(Unsupported),
      IEMobile          -> Set(Unsupported),
      IOSSafari         -> Set(Unsupported),
      Opera             -> Set(FullX),
      OperaMini         -> Set(Unsupported),
      OperaMobile       -> Set(Unsupported),
      Safari            -> Set(FullX)
    )
  }
}