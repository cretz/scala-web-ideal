package webideal
package util

import prickle._
import collection.mutable
import scala.util.Try

trait PrickleExtras {
  implicit object UnitPickler extends Pickler[Unit] {
    def pickle[P](x: Unit, state: PickleState)(implicit config: PConfig[P]): P =
      config.makeNull()
  }
  
  implicit object UnitUnpickler extends Unpickler[Unit] {
    def unpickle[P](pickle: P, state: mutable.Map[String, Any])(implicit config: PConfig[P]) = Try(())
  }
}
object PrickleExtras extends PrickleExtras