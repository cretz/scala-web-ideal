package webideal.hangman

case class Game(
  level: Int,
  guess: String,
  misses: Int = 0,
  won: Boolean = false,
  gameOverWord: Option[String] = None
) {
  def gameOver = gameOverWord.isDefined
}