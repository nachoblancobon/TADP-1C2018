abstract class Suceso

sealed trait SucesoCaraCruz extends Suceso

case class Cara() extends SucesoCaraCruz
case class Cruz() extends SucesoCaraCruz

case class SucesoRuleta(numero: Int) extends Suceso {
  require(numero >= 0 && numero < 37)
}
