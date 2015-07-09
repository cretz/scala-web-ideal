package webideal
package index

import akka.stream.Materializer
import akka.http.scaladsl.marshalling.ToResponseMarshallable.apply
import akka.http.scaladsl.server.Directive.addByNameNullaryApply
import akka.http.scaladsl.server.Directive.addDirectiveApply
import akka.actor.ActorSystem

trait IndexPage extends Page {
  def apply()(implicit sys: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx =>
        complete(IndexView(SharedMessages.itWorks))
      }
    }
  }
}
object IndexPage extends IndexPage