package webideal
package todos

import scalacss.Defaults._
import scala.concurrent.duration._

trait TodoStyle extends StyleSheet.Standalone with Style {
  import dsl._
  
  val name = "todo"

  "button" - (
    margin(0 px),
    padding(0 px),
    border(0 px),
    background := none,
    fontSize(100 %%),
    verticalAlign.baseline,
    fontFamily := inherit,
    color.inherit,
    appearance := none
  )
  
  "body" - (
    // TODO: make this more typed
    font := "14px 'Helvetica Neue', Helvetica, Arial, sans-serif",
    lineHeight(1.4 em),
    // TODO: make this better please
    background := "#eaeaea url('../images/bg.png')",
    color("#4d4d4d"),
    fontSmooth := antialiased
  )

  // TODO: can the name be a bit more typed
  "button, input[type=\"checkbox\"]" - outline.none
  
  "#todoapp" - (
    backgroundColor(rgba(255, 255, 255, 0.9)),
    border(1 px, solid, "#ccc".color),
    position.relative,
    borderTopLeftRadius(2 px),
    borderTopRightRadius(2 px),
    // TODO: more typed
    boxShadow := "0 2px 6px 0 rgba(0, 0, 0, 0.2), 0 25px 50px 0 rgba(0, 0, 0, 0.15)",
    &.before - (
      content := "",
      borderLeft(1 px, solid, "#f5d6d6".color),
      borderRight(1 px, solid, "#f5d6d6".color),
      width(2 px),
      position.absolute,
      top(0 px),
      left(40 px),
      height(100 %%)
    ),
    // TODO: can this be done better?
    &("input::-webkit-input-placeholder") - fontStyle.italic,
    &("input::-moz-placeholder") - (
      fontStyle.italic,
      color("#a9a9a9")
    )
  )

  "#header" - (
    paddingTop(15 px),
    borderRadius.inherit,
    &.before - (
      top(0 px),
      right(0 px),
      left(0 px),
      height(15 px),
      zIndex(2),
      borderBottom(1 px, solid, "#6c615c".color),
      // TODO: ug
      background := "#8d7d77".color,
      background := "-webkit-gradient(linear, left top, left bottom, from(rgba(132, 110, 100, 0.8)),to(rgba(101, 84, 76, 0.8)))",
      background := "-webkit-linear-gradient(top, rgba(132, 110, 100, 0.8), rgba(101, 84, 76, 0.8))",
      background := "linear-gradient(top, rgba(132, 110, 100, 0.8), rgba(101, 84, 76, 0.8))",
      filter := "progid:DXImageTransform.Microsoft.gradient(GradientType=0,StartColorStr='#9d8b83', EndColorStr='#847670')",
      borderTopLeftRadius(1 px),
      borderTopRightRadius(1 px)
    )
  )

  "#new-todo, .edit" - (
    position.relative,
    margin(0 px),
    width(100 %%),
    fontSize(24 px),
    fontFamily := inherit,
    lineHeight(1.4 em),
    border(0 px),
    outline.none,
    color.inherit,
    padding(6 px),
    border(1 px, solid, "#999".color),
    boxShadow := "inset 0 -1px 5px 0 rgba(0, 0, 0, 0.2)",
    boxSizing.borderBox,
    fontSmooth := antialiased
  )
  
  "#new-todo" - (
    padding(16 px, 16 px, 16 px, 16 px),
    border.none,
    background := rgba(0, 0, 0, 0.2),
    zIndex(100),
    boxShadow := none
  )

  "#main" - (
    position.relative,
    zIndex(2),
    borderTop(1 px, dotted, "#adadad".color)
  )
  
  "label[for='toggle-all']" - display.none
  
  "#toggle-all" - (
    position.absolute,
    top(-42 px),
    left(-4 px),
    width(40 px),
    textAlign.center,
    border.none,
    &.before - (
      content := "»",
      fontSize(28 px),
      color("#d9d9d9"),
      padding(0 px, 25 px, 7 px)
    ),
    // TODO: how do I do this better?
    &.checked.before - color("#737373")
  )

  "#todo-list" - (
    margin(0 px),
    padding(0 px),
    listStyle := none,
    &("li") - (
      position.relative,
      fontSize(24 px),
      borderBottom(1 px, dotted, "#ccc".color),
      &.lastChild - borderBottom.none,
      &(".toggle") - (
        textAlign.center,
        width(40 px),
        height.auto,
        position.absolute,
        top(0 px),
        bottom(0 px),
        margin(auto, 0 px),
        border.none,
        appearance := none,
        &.after - (
          content := "✔",
          lineHeight(43 px),
          fontSize(20 px),
          color("#d9d9d9"),
          textShadow := "0 -1px 0 #bfbfbf"
        ),
        &.checked.after - (
          color("#85ada7"),
          textShadow := "0 1px 0 #669991",
          bottom(1 px),
          position.relative
        )
      ),
      &("label") - (
        whiteSpace.pre,
        wordBreak.breakAll,
        padding(15 px, 60 px, 15 px, 15 px),
        marginLeft(45 px),
        display.block,
        lineHeight(1.2),
        transition := "color 0.4s"
      ),
      &(".destroy") - (
        display.none,
        position.absolute,
        top(0 px),
        right(10 px),
        bottom(0 px),
        width(40 px),
        height(40 px),
        margin(auto, 0 px),
        fontSize(22 px),
        color("#a88a8a"),
        transition := "all 0.2s",
        &.hover - (
          textShadow := "0 0 1px #000, 0 0 10px rgba(199, 107, 107, 0.8)",
          transform := "scale(1.3)"
        ),
        &.after - (content := "x")
      ),
      &(":hover .destroy") - display.block,
      &(".edit") - display.none
    ),
    &("li.editing") - (
      borderBottom.none,
      padding(0 px),
      &(".edit") - (
        display.block,
        width(506 px),
        padding(13 px, 17 px, 12 px, 17 px),
        margin(0 px, 0 px, 0 px, 43 px)
      ),
      &(".view") - display.none,
      &.lastChild - marginBottom(-1 px)
    ),
    &("li.completed label") - (
      color("#a9a9a9"),
      textDecoration := ^.lineThrough
    )
  )

  "#footer" - (
    color("#777"),
    padding(0 px, 15 px),
    position.absolute,
    right(0 px),
    bottom(-31 px),
    left(0 px),
    height(20 px),
    textAlign.center,
    &.before - (
      content := "",
      position.absolute,
      right(0 px),
      bottom(31 px),
      left(0 px),
      height(50 px),
      zIndex(-1),
      boxShadow :=
        "0 1px 1px rgba(0, 0, 0, 0.3)," +
        "0 6px 0 -3px rgba(255, 255, 255, 0.8)," +
        "0 7px 1px -3px rgba(0, 0, 0, 0.3)," +
        "0 43px 0 -6px rgba(255, 255, 255, 0.8)," +
        "0 44px 2px -6px rgba(0, 0, 0, 0.2)"
    )
  )
  
  "#todo-count" - (
    float.left,
    textAlign.left
  )

  "#filters" - (
    margin(0 px),
    padding(0 px),
    listStyle := none,
    position.absolute,
    right(0 px),
    left(0 px),
    &("li") - (
      display.inline,
      &("a") - (
        color("#83756f"),
        margin(2 px),
        textDecoration := none
      ),
      &("a.selected") - fontWeight.bold
    )
  )

  "#clear-completed" - (
    float.right,
    position.relative,
    lineHeight(20 px),
    textDecoration := none,
    background := rgba(0, 0, 0, 0.1),
    fontSize(11 px),
    padding(0 px, 10 px),
    borderRadius(3 px),
    boxShadow := "0 -1px 0 0 rgba(0, 0, 0, 0.2)",
    &.hover - (
      background := rgba(0, 0, 0, 0.15),
      boxShadow := "0 -1px 0 0 rgba(0, 0, 0, 0.3)"
    )
  )

  "#info" - (
    margin(65 px, auto, 0 px),
    color("#a6a6a6"),
    fontSize(12 px),
    textShadow := "0 1px 0 rgba(255, 255, 255, 0.7)",
    textAlign.center,
    &("a") - color.inherit
  )

  "@media screen and (-webkit-min-device-pixel-ratio:0)" - (
    &("#toggle-all, #todo-list li .toggle") - (background := none),
    &("#todo-list li .toggle") - height(40 px),
    &("#toggle-all") - (
      top(-56 px),
      left(15 px),
      width(65 px),
      height(41 px),
      transform := "rotate(90 deg)",
      appearance := none
    )
  )
  
  ".hidden" - display.none

  "hr" - (
    margin(20 px, 0 px),
    border(0 px),
    borderTop(1 px, dashed, "#c5c5c5".color),
    borderBottom(1 px, dashed, "#f7f7f7".color)
  )

  ".learn" - (
    &("a") - (
      fontWeight.normal,
      textDecoration := none,
      color("#b83f45"),
      &.hover - (
        textDecoration := ^.underline,
        color("#787e7e")
      )
    ),
    &("h3, h4, h5") - (
      margin(10 px, 0 px),
      fontWeight._500,
      lineHeight(1.2),
      color("#000")
    ),
    &("h3") - fontSize(24 px),
    &("h4") - fontSize(18 px),
    &("h5") - (
      marginBottom(0 px),
      fontSize(14 px)
    ),
    &("ul") - (
      padding(0 px),
      margin(0 px, 0 px, 30 px, 25 px)
    ),
    &("li") - lineHeight(20 px),
    &("p") - (
      fontSize(15 px),
      fontWeight._300,
      lineHeight(1.3 px),
      marginTop(0 px),
      marginBottom(0 px)
    )
  )

  ".quote" - (
    &("p") - (
      fontStyle.italic,
      &.before - (
        content := "“",
        fontSize(50 px),
        opacity(0.15),
        position.absolute,
        top(-20 px),
        left(3 px)
      ),
      &.after - (
        content := "”",
        fontSize(50 px),
        opacity(0.15),
        position.absolute,
        bottom(-42 px),
        right(3 px)
      )
    ),
    &("footer") - (
      position.absolute,
      bottom(-40 px),
      right(0 px),
      &("img") - borderRadius(3 px),
      &("a") - (
        marginLeft(5 px),
        verticalAlign.middle
      )
    )
  )

  ".speech-bubble" - (
    position.relative,
    padding(10 px),
    background := rgba(0, 0, 0, 0.04),
    borderRadius(5 px),
    &.after - (
      content := "",
      position.absolute,
      top(100 %%),
      right(30 px),
      border(13 px, solid, transparent),
      borderTopColor(rgba(0, 0, 0, 0.04))
    )
  )

  ".learn-bar > .learn" - (
    position.absolute,
    width(272 px),
    top(8 px),
    left(-300 px),
    padding(10 px),
    borderRadius(5 px),
    backgroundColor(rgba(255, 255, 255, 0.6)),
    transitionProperty := ^.left,
    transitionDuration(500 millis)
  )

  // TODO: why can't I use media queries here?
  "@media (min-width: 899px)" - (
    &(".learn-bar") - (
      width.auto,
      margin(0 px, 0 px, 0 px, 300 px),
      &("#todoapp") - (
        width(550 px),
        margin(130 px, auto, 40 px, auto)
      )
    ),
    &(".learn-bar > .learn") - left(8 px)
  )
}
object TodoStyle extends TodoStyle