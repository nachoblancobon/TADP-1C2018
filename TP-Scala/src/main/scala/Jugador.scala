import Estrategias._

case class Jugador(monto:Int, estrategia: EstrategiaEleccion){
  def elegirJuegos(apuestas: List[PlanDeJuego]):PlanDeJuego = estrategia.apply(monto, apuestas)
}

object Jugadores{
  val jugadorRacional: (Int => Jugador) = monto => Jugador(monto, EstrategiaRacional)
  val jugadorArriesgado: (Int => Jugador) = monto => Jugador(monto, EstrategiaArriesgada)
  val jugadorCauto: (Int => Jugador) = monto => Jugador(monto, EstrategiaCauta)
  val jugadorVeloz: (Int => Jugador) = monto => Jugador(monto, EstrategiaVeloz)
}

object Estrategias{
  type EstrategiaEleccion = (Int, List[PlanDeJuego]) => PlanDeJuego

  object EstrategiaRacional extends EstrategiaEleccion{
    def apply(monto:Int, apuestas: List[PlanDeJuego]): PlanDeJuego = apuestas.maxBy(_.resultadosPosibles(monto).escenarios.map(_.gananciaPonderada).sum)
  }

  object EstrategiaArriesgada extends EstrategiaEleccion{
    def apply(monto:Int, apuestas: List[PlanDeJuego]): PlanDeJuego = apuestas.maxBy(_.resultadosPosibles(monto).escenarios.map(_.monto).max)
  }

  object EstrategiaCauta extends EstrategiaEleccion{
    def apply(monto:Int, apuestas: List[PlanDeJuego]): PlanDeJuego = apuestas.maxBy(_.resultadosPosibles(monto).probabilidadDeSalirHecho(monto))
  }

  object EstrategiaVeloz extends EstrategiaEleccion {
    def apply(monto: Int, apuestas: List[PlanDeJuego]): PlanDeJuego = apuestas.head
  }
}

