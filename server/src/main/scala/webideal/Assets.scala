package webideal

object Assets {
  def public(relativePath: String): String = ???
  
  // This should be a macro to check ffor presence at compile time
  def webJar(webJar: String, partialPath: String): String = ???
}