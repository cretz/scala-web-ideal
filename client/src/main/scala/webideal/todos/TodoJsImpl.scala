package webideal
package todos

import scala.scalajs.js.annotation.JSExport

// A lot of this copied directly from https://github.com/hussachai/play-scalajs-showcase/blob/1c4e622958a0a79949d2c316bbee402fa8db0087/example-client/src/main/scala/example/TodoJS.scala
// I did a few cleanups and moved it to use autowire
object TodoJsImpl extends TodoJs {
  def main(): Unit = new View(new Model).init()
}