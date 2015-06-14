package webideal
package upload

import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import akka.http.scaladsl.model.Multipart
import java.io.File
import akka.stream.io.SynchronousFileSink
import java.util.UUID
import scala.concurrent.Future

trait UploadPage extends Page {
  import UploadPage._
  
  def apply()(implicit sys: ActorSystem, mat: FlowMaterializer) = {
    pathEnd {
      get {
        extractRequestContext { implicit ctx =>
          complete(UploadView())
        }
      }
    } ~
    path("upload") {
      post {
        import sys.dispatcher
        entity(as[Multipart.FormData]) { data =>
          // We only want the one file
          val fut = data.parts.
            filter(_.name == "uploaded-file").
            runFold(Future.successful(0L)) { (_, part) =>
              val ext = part.filename.map(_.lastIndexOf('.')) match {
                case Some(index) if index != -1 => part.filename.get.substring(index)
                case _ => ""
              }
              val file = new File(directory, UUID.randomUUID.toString + ext)
              part.entity.dataBytes.runWith(SynchronousFileSink.apply(file, false))
            }.flatMap(identity)
          complete(fut.map("File uploaded w/ total size of " + _))
        }
      }
    }
  }
}
object UploadPage extends UploadPage {
  lazy val directory = {
    val dir = new File("upload")
    require(dir.isDirectory || dir.mkdir, "Unable to make upload directory")
    dir
  }
}