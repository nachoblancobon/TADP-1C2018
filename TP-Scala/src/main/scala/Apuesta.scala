case class Apuesta(montoApostado: Int, jugadas: List[Jugada]){
  def jugar(juego: Juego): Int = {
    montoApostado match {
      case 0 => 0
      case _ => jugadas.map(_.jugar(juego.proximoResultado, montoApostado)).sum
    }
  }
}

case class Apuestas(apuestas: List[Apuesta], juegos: List[Juego]){
  require(apuestas.lengthCompare(juegos.size) == 0)
  def resultadosPosibles(montoInicial: Int): DistribucionResultados = apuestas.zip(juegos)
    .foldLeft(DistribucionResultados(Set(EscenarioPosible(montoInicial)))) { (resultadoAnterior, apuestaJuego) => {
      val (apuesta, juego) = apuestaJuego

      resultadoAnterior.obtenerResultadosPosiblesPara(apuesta, juego.distribucionResultados)
    }
  }

  def jugar(montoInicial:Int): Int = apuestas.zip(juegos)
    .foldLeft(montoInicial) { (montoAnterior, apuestaJuego) => {
        val (apuesta, juego) = apuestaJuego

        apuesta.jugar(juego)
      }
    }
}

case class Juego(distribucionResultados: DistribucionProbabilidad){
  def proximoResultado:Suceso = distribucionResultados.proximoSuceso()
}

case class DistribucionResultados(escenarios: Set[EscenarioPosible]){
  def obtenerResultadosPosiblesPara(apuesta: Apuesta, distribucion: DistribucionProbabilidad):DistribucionResultados =
    copy(escenarios = for {
      escenariosPosibles <- escenarios
      escenario <- escenariosPosibles.jugar(apuesta.jugadas, apuesta.montoApostado, distribucion)
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

  def jugar(jugadas: List[Jugada], montoApostado: Int, distribucion: DistribucionProbabilidad): Set[EscenarioPosible] =
    if(monto < montoApostado){
      Set(this)
    }else{
      jugadas.foldLeft(Set(this)){ (escenariosPosibles: Set[EscenarioPosible], jugada: Jugada) =>
        escenariosPosibles.flatMap{(escenarioPosible) =>
          jugada.escenariosPosibles(montoApostado, distribucion).map((escenario) =>
              escenario.copy(monto = escenarioPosible.monto - montoApostado + escenario.monto,
                             probabilidad = DoubleFormatter.format(escenario.probabilidad * escenarioPosible.probabilidad)))
        }
      }
    }
}
