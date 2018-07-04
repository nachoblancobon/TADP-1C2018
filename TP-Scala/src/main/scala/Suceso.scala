abstract class Suceso

sealed trait SucesoCaraCruz extends Suceso

case object Cara extends SucesoCaraCruz
case object Cruz extends SucesoCaraCruz

case class SucesoRuleta(numero: Int) extends Suceso {
  require(numero >= 0 && numero < 37)
}

case class SucesoRuletaPonderado(sucesoRuleta: SucesoRuleta, peso:Int){}