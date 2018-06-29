/*
import Estrategias._

case class Jugador(monto:Int, estrategia: EstrategiaEleccion){
  def elegirJuegos(apuestas: List[Apuestas]):Apuestas = estrategia.apply(monto, apuestas)

  def jugar(apuestas:Apuestas):Jugador = copy(monto = apuestas.jugar(monto))
}
object Jugadores{
  val jugadorRacional: (Int => Jugador) = monto => Jugador(monto, EstrategiaRacional)
  val jugadorArriesgado: (Int => Jugador) = monto => Jugador(monto, EstrategiaArriesgada)
  val jugadorCauto: (Int => Jugador) = monto => Jugador(monto, EstrategiaCauta)
}

object Estrategias{
  type EstrategiaEleccion = (Int, List[Apuestas]) => Apuestas

  object EstrategiaRacional extends EstrategiaEleccion{
    def apply(monto:Int, apuestas: List[Apuestas]): Apuestas = apuestas.maxBy(_.resultadosPosibles(monto).escenarios.map(_.gananciaPonderada).sum)
  }

  object EstrategiaArriesgada extends EstrategiaEleccion{
    def apply(monto:Int, apuestas: List[Apuestas]): Apuestas = apuestas.maxBy(_.resultadosPosibles(monto).escenarios.map(_.monto).max)
  }

  object EstrategiaCauta extends EstrategiaEleccion{
    def apply(monto:Int, apuestas: List[Apuestas]): Apuestas = apuestas.maxBy(_.resultadosPosibles(monto).probabilidadDeSalirHecho(monto))
  }
}

*/
