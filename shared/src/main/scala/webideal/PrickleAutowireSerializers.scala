package webideal

import prickle._

trait PrickleAutowireSerializers extends autowire.Serializers[String, Unpickler, Pickler] {
  def read[R: Unpickler](p: String) = Unpickle[R].fromString(p).get
  def write[R: Pickler](r: R) = Pickle.intoString[R](r)
}