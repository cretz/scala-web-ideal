package webideal
package style

import scalacss.Defaults._

trait MainStyle extends StyleSheet.Standalone with Style {
  import dsl._
  
  val name = "main"
  
  // Move down content because we have a fixed navbar that is 50px tall
  "body" - paddingTop(50 px)
  
  ".sub-header" - (
    paddingBottom(10 px),
    borderBottom(1 px, solid, "#eee".color)
  )

  // Hide default border to remove 1px line.
  ".navbar-fixed-top" - border(0 px)
  
  // Hide for mobile, show later
  ".sidebar" - (
    // TODO: figure out the better way to do this
    &(media.maxWidth(767 px)) - display.none,
    &(media.minWidth(768 px)) - (
      position.fixed,
      top(51 px),
      bottom(0 px),
      left(0 px),
      zIndex(1000),
      display.block,
      padding(20 px),
      overflowX.hidden,
      overflowY.auto,
      backgroundColor("#f5f5f5"),
      borderRight(1 px, solid, "#eee".color)
    )
  )
  
  // Sidebar navigation
  ".nav-sidebar" - (
    marginRight(-21 px),
    marginBottom(20 px),
    marginLeft(-20 px)
  )
  // TODO: figure out if there's a shortcut for ".nav-sidebar > li > a"
  ".nav-sidebar > li > a" - (
    paddingRight(20 px),
    paddingLeft(20 px)
  )
  // TODO: figure out how to do reuse better here
  ".nav-sidebar > .active > a" - (
     color("#fff"), backgroundColor("#428bca"),
     &.hover(color("#fff"), backgroundColor("#428bca")),
     &.focus(color("#fff"), backgroundColor("#428bca"))
  )
  
  // Main content
  ".main" - (
    padding(20 px),
    &(media.minWidth(768 px)) - (
      paddingRight(40 px),
      paddingLeft(40 px)
    ),
    &(".page-header") - marginTop(0 px)
  )
  
  // Placeholder dashboard ideas
  ".placeholders" - (
    marginBottom(30 px),
    textAlign.center,
    &("h4") - marginBottom(0 px)
  )
  ".placeholder" - (
    marginBottom(20 px),
    &("img") - (
      display.inlineBlock,
      borderRadius(50 %%)
    )
  )
  
  ".boxsizing-border" - boxSizing.borderBox
}
object MainStyle extends MainStyle