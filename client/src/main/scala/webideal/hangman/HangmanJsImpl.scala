package webideal
package hangman

import org.scalajs.dom
import rx._
import autowire._
import scalatags.JsDom._
import scalatags.JsDom.all._
import scalajs.concurrent.JSExecutionContext.Implicits.queue

// Mostly taken from https://github.com/hussachai/play-scalajs-showcase/blob/e130bf5112c5cdd26485ea620988bad4b6589fc3/example-client/src/main/scala/example/HangmanJS.scala
object HangmanJsImpl extends HangmanJs {
  import util.ScalaRxSupport._
  var currentNode: dom.Node = null

  def run(): Unit =
    Model.resume { resume =>
      currentNode =
        if (!resume) pagePlay.render
        else if (Model.game().gameOver) pageResult.render
        else pageGuess.render
      dom.document.getElementById("content").appendChild(currentNode)
    }

  def pageGuess: TypedTag[dom.raw.HTMLElement] = div(`class`:="content"){
    div(
      Rx {
        div(
          h2("Please make a guess"),
          h3(style := "letter-spacing: 4px;")(Model.game().guess),
          p(s"You have made ${Model.game().misses} bad guesses out of a maximum of ${Model.game().level}"),
          p("Guess:")(
            for (c <- ('A' until 'Z'); if !Model.game().guess.contains(c)) yield {
              a(c.toString)(style := "padding-left:10px;", href := "javascript:void(0);", onclick := { () =>
                Model.guess(c) { () =>
                  if (Model.game().gameOver) goto(pageResult)
                }
              })
            }
          ), br,
          a("Give up?", href := "javascript:void(0);", onclick := { () =>
            Model.quit { () => goto(pagePlay) }
          })
        )
      }
    )
  }

  def pagePlay: TypedTag[dom.raw.HTMLElement] = div {
    val levels = Array(
      (10, "Easy game; you are allowed 10 misses."),
      (5, "Medium game; you are allowed 5 misses."),
      (3, "Hard game; you are allowed 3 misses.")
    )
    div(
      p("Inspired from ")(a(href := "http://www.yiiframework.com/demos/hangman/", target := "_blank", "Yii's demo")),
      p("This is the game of Hangman. You must guess a word, a letter at a time.\n" +
        "If you make too many mistakes, you lose the game!"),
      form(id := "playForm")(
        for ((level,text) <- levels) yield {
          val levelId = s"level_${level}"
          div(`class` := "radio")(
            input(id := levelId, `type` := "radio", name := "level", onclick := { ()=>
              Model.level() = level
            }, { if (level == Model.level()) checked := "checked"}),
            label(`for` := levelId, style := "padding-left: 5px")(text)
          )
        }, br,
        input(`type` := "button", value := "Play!", `class` := "btn btn-primary", onclick := { () =>
          if (Model.level() > 0) {
            Model.start()
            goto(pageGuess)
          } else dom.alert("Please select level!")
        })
      )
    )
  }

  def pageResult: TypedTag[dom.raw.HTMLElement] = div{
    val result = if (Model.game().won) "You Win!" else "You Lose!"
    div(
      h2(result),
      p(s"The word was: ${Model.game().gameOverWord}"), br,
      input(`type` := "button", value := "Start Again!", `class` := "btn btn-primary", onclick := { () =>
        goto(pagePlay)
      })
    )
  }

  def goto(page: TypedTag[dom.raw.HTMLElement]) = {
    val lastChild = dom.document.getElementById("content").lastChild
    dom.document.getElementById("content")
      .replaceChild(div(style := "margin-left:20px;")(page).render, lastChild)
  }

  object Model {
    import util.PrickleExtras._
    
    val level = Var(0)
    val game = Var(Game(0, ""))
    
    lazy val client = util.PrickleAutowireClient("/hangman/")
    def handler = client[HangmanHandler]

    def start(level: Int = Model.level()) =
      handler.start(level).call().foreach(game() = _)

    def resume(callback: (Boolean) => Unit) =
      handler.resume().call().foreach { gameUpdate =>
        gameUpdate.foreach { gameUpdate =>
          game() = gameUpdate
          level() = game().level
        }
        callback(gameUpdate.isDefined)
      }

    def guess(g: Char)(callback: () => Unit) =
      handler.guess(g).call().foreach { gameUpdate =>
        game() = gameUpdate
        callback()
      }

    def quit(callback: () => Unit) =
      handler.quit().call().foreach { _ =>
        level() = 0
        callback()
      }
  }
}