package webideal

import prickle._
import autowire._
import scala.concurrent.Future
import org.scalajs.dom

// When using this, don't forget to import autowire._ even though it doesn't seem like it's needed
trait PrickleAutowireClient extends autowire.Client[String, Unpickler, Pickler] with PrickleAutowireSerializers {
  import scalajs.concurrent.JSExecutionContext.Implicits.runNow
  
  // Needs trailing slash
  def pathPrefix: String
  
  override def doCall(req: Request): Future[String] = {
    dom.ext.Ajax.post(
      url = pathPrefix + req.path.mkString("/"),
      data = Pickle.intoString(req.args)
    ).map(_.responseText)
  }
}
object PrickleAutowireClient {
  def apply(pathPrefixString: String): PrickleAutowireClient = new PrickleAutowireClient {
    val pathPrefix = pathPrefixString
  }
}