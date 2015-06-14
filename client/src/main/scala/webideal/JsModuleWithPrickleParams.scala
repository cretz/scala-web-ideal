package webideal

import prickle._
import scala.scalajs.js

trait JsModuleWithPrickleParams extends JsModuleWithParams {
  implicit def paramsUnpickler: Unpickler[ParamType]
  
  override def runWithParams(params: Any): Unit = {
    // TODO: find serialization library that works w/ native browser JSON
    val unpickled = Unpickle[ParamType].fromString(js.JSON.stringify(params.asInstanceOf[js.Any])).get
    run(unpickled)
  }
}