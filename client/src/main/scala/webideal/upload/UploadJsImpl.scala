package webideal
package upload

import org.scalajs.dom
import org.scalajs.dom.raw.FileReader
import scala.scalajs.js
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import js.Dynamic.{ global => g }
import scalatags.JsDom._
import scalatags.JsDom.all._
import org.scalajs.jquery.{jQuery => $}
import scala.util.Try
import scala.language.implicitConversions

// Mostly taken from https://github.com/hussachai/play-scalajs-showcase/blob/e130bf5112c5cdd26485ea620988bad4b6589fc3/example-client/src/main/scala/example/FileUploadJS.scala
object UploadJsImpl extends UploadJs {

  val maxFileUploadSize = 3000000l
  val maxFileReadSize = 3000000l
    
  def run(): Unit = {
    // I decided not to replicate CSRF-check functionality right now, but it'd be really easy
    dom.document.getElementById("content").appendChild(markup().render)
    ready()
  }

  def markup() = div(
    p("Modified from ",
      a(href := "http://www.sitepoint.com/html5-file-drag-and-drop/", "How to Use HTML5 File Drag and Drop"),
      " by ",
      a(href := "http://twitter.com/craigbuckler", "Craig Buckler")
    ),
    p("""This is a demonstration of the HTML5 file drag & drop API with asynchronous Ajax file uploads,
      graphical progress bars and progressive enhancement."""),
      div(`class` := "panel panel-info")(
        div(`class` := "panel-heading")(h3(`class` := "panel-title", "HTML File Upload")),
        div(`class` := "panel-body")(
          div (
            label(`for` := "", "Files to upload:"),
            input(`type` := "file", id := "fileSelect", name := "fileSelect", "multiple".attr := "multiple")
          ),
          br,
          div(id := "fileDrag", `class` := "panel panel-info",
            style := "height: 70px; border-style: dashed; border-width: 2px;text-align: center;")(
              p(style := "margin-top:20px;")("Or drop file here...")
            )
        )
    ),
    div(`class` := "progress")(
      div(id := "progress", `class` := "progress-bar progress-bar-success progress-bar-striped",
        "role".attr := "progressbar", "aria-valuenow".attr := "0",
        "aria-valuemin".attr := "0", "aria-valuemax".attr := "100", style := "width: 100%", "0%")
    ),
    h4("Status Messages")(span(style := "margin-left:15px;"), span(id := "status", `class` := "label hide", "Done")),
    div(id := "messages", `class` := "alert alert-info")
  )

  trait EventTargetExt extends dom.EventTarget {
    var files: dom.FileList = js.native
  }

  trait EventExt extends dom.Event {
    var dataTransfer: dom.DataTransfer = js.native
    var loaded: Int = js.native
    var total: Int = js.native
  }

  def ready() = {
    implicit def pimpEventTarget(e: dom.EventTarget): EventTargetExt = e.asInstanceOf[EventTargetExt]
    implicit def pimpEvent(e: dom.Event): EventExt = e.asInstanceOf[EventExt]

    def $id(s: String) = dom.document.getElementById(s)

    def output(msg: String) = {
      val m = $id("messages")
      m.innerHTML = msg + m.innerHTML
    }

    def fileDragHover(e: dom.Event) = {
      e.stopPropagation()
      e.preventDefault()
      if (e.`type` == "dragover") {
        $("#fileDrag").removeClass("panel-info").addClass("panel-primary")
      } else {
        $("#fileDrag").removeClass("panel-primary").addClass("panel-info")
      }
    }

    def fileSelectHandler(e: dom.Event) = {
      fileDragHover(e)
      val files =
        if(e.target.files.toString != "undefined") e.target.files
        else e.asInstanceOf[dom.DragEvent].dataTransfer.files
      (0 until files.length).foreach { i =>
        Try({
          parseFile(files(i))
          uploadFile(files(i))
        }).recover { case e =>
          output(s"<p>Error: <pre>$e</pre>")
        }
      }
    }

    def parseFile(file: dom.File) = {
      output(
        s"""
          |<p>File information: <strong>${file.name}</strong>
          | type: <strong>${file.`type`}</strong>
          | size: <strong>${file.size}</strong> bytes</p>
        """.stripMargin)
      val reader = new FileReader()

      if (file.`type`.indexOf("image") != -1 && file.size <= maxFileReadSize) {
        reader.onload = (e: dom.UIEvent) => {
          output(
            s"""
              |<p><strong>${file.name}:</strong><br />
              |<img src="${reader.result}"/></p>
            """.stripMargin)
        }
        reader.readAsDataURL(file)
      } else if(file.`type`.indexOf("text") != -1 && file.size <= maxFileReadSize){
        reader.onload = (e: dom.UIEvent) => {
          output(
            s"""
              |<p><strong>${file.name}:</strong></p>
              |<pre>${reader.result}</pre>
            """.stripMargin)
        }
        reader.readAsText(file)
      }
    }

    def uploadFile(file: dom.File) = {
      val xhr = new dom.XMLHttpRequest
      if (xhr.upload == null || file.size > maxFileUploadSize) {
        output(s"<p>No XHR or file size of ${file.size} over max allowed of $maxFileUploadSize</p>")
      } else {
        xhr.upload.addEventListener("progress", (e: dom.Event) => {
          val pc = (e.loaded / e.total * 100)
          $("#progress").css("with", pc + "%").attr("aria-valuenow", pc.toString)
            .html(s"${file.name} ($pc %)")
        }, false)

        xhr.onreadystatechange = (e: dom.Event) => {
          if (xhr.readyState == dom.XMLHttpRequest.UNSENT){
            $("#status").addClass("hide")
          } else if(xhr.readyState == dom.XMLHttpRequest.DONE){
            val statusClass = if (xhr.status == 200) "label-success" else "label-danger"
            val statusMsg = if (xhr.status == 200) "Success" else "Error "+xhr.statusText
            $("#status").removeClass("hide").addClass(statusClass).text(statusMsg)
          }
        }
        //start upload
        xhr.open("POST", "/upload/upload", true)
        val formData = new dom.raw.FormData()
        formData.append("uploaded-file", file)
        xhr.send(formData)
      }
    }

    $("#fileDrag").on("dragenter dragstart dragend dragleave dragover drag drop", (e: dom.Event) => {
      e.preventDefault()
    });

    $id("fileSelect").addEventListener("change", fileSelectHandler _, false)
    val xhr = new dom.XMLHttpRequest
    if (xhr.upload != null){
      val fileDrag = $id("fileDrag")
      fileDrag.addEventListener("dragover", fileDragHover _, false)
      fileDrag.addEventListener("dragleave", fileDragHover _, false)
      fileDrag.addEventListener("drop", fileSelectHandler _, false)
    }
  }
}