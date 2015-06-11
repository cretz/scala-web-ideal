package webideal
package util

import prickle._

trait PrickleAutowireSerializers extends autowire.Serializers[String, Unpickler, Pickler] with PrickleExtras {
  def read[R: Unpickler](p: String) = Unpickle[R].fromString(p).get
  def write[R: Pickler](r: R) = Pickle.intoString[R](r)
}