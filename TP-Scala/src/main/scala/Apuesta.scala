sealed trait Apuesta{
  def montoApostado:Int
  def puedeJugarse(montoDisponible:Int): Boolean = montoApostado <= montoDisponible

  def apuestas: List[ApuestaSimple]
}

case class ApuestaSimple(montoApostado: Int, jugada: Jugada) extends Apuesta {
  override def apuestas: List[ApuestaSimple] = List(this)

  def escenariosPosibles(distribucion: DistribucionProbabilidad): List[EscenarioPosible] =
    jugada.escenariosPosibles(montoApostado, distribucion)
}

case class ApuestaCompuesta(apuestas: List[ApuestaSimple]) extends Apuesta{
  val montoApostado:Int = apuestas.map(_.montoApostado).sum
}

case class PlanDeJuego(juegos: List[Juego]) {
  require(juegos.nonEmpty)

  def resultadosPosibles(montoInicial: Int): DistribucionResultados =
    juegos.foldLeft(DistribucionResultados(List(EscenarioPosible(montoInicial)))) {
      (resultadoAnterior, juego) => {
        resultadoAnterior.obtenerResultadosPosiblesPara(juego)
      }
    }
}

case class Juego(apuesta: Apuesta, distribucionResultados: DistribucionProbabilidad){}

case class DistribucionResultados(escenarios: List[EscenarioPosible]){
  def obtenerResultadosPosiblesPara(juego: Juego):DistribucionResultados =
    copy(escenarios = for {
      escenariosPosibles <- escenarios
      escenario <- escenariosPosibles.jugar(juego.apuesta, juego.distribucionResultados)
      if escenario.posible
    } yield escenario)

  def probabilidadDeGanancia(montoInicial:Int):Double = escenarios.filter(_.monto > montoInicial).map(_.probabilidad).sum

  def probabilidadDeSalirHecho(montoInicial:Int):Double = escenarios.filter(_.monto >= montoInicial).map(_.probabilidad).sum
}


case class EscenarioPosible(monto: Int, probabilidad: Double = 1.0){
  require(monto >= 0)
  require(probabilidad >= 0.0 && probabilidad <= 1.0)

  val posible:Boolean = probabilidad > 0.0

  val gananciaPonderada:Double = monto * probabilidad

  def jugar(apuesta: Apuesta, distribucion: DistribucionProbabilidad): List[EscenarioPosible] =
    if(!apuesta.puedeJugarse(monto)){
      List(this)
    }else{
      apuesta.apuestas.foldLeft(List(this)){ (escenariosPosibles: List[EscenarioPosible], apuesta: ApuestaSimple) =>
        escenariosPosibles.flatMap{(escenarioPosible) =>
          apuesta.escenariosPosibles(distribucion).map((escenario) =>
              escenario.copy(monto = escenarioPosible.monto - apuesta.montoApostado + escenario.monto,
                             probabilidad = DoubleFormatter.format(escenario.probabilidad * escenarioPosible.probabilidad)))
        }
      }
    }
}
