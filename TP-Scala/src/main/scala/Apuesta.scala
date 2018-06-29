case class Apuesta(montoApostado: Int, jugada: Jugada) {
  def jugar(suceso: Suceso): Int = {
    montoApostado match {
      case 0 => 0
      case _ => jugada.jugar(suceso, montoApostado)
    }
  }
}

case class ApuestaCompleta(apuestas: List[Apuesta], distribucion: DistribucionProbabilidad) {
  def montoApostado: Int = apuestas.map(_.montoApostado).sum
  def jugar(suceso: Suceso, montoDisponible: Int): Int = {
    if(montoDisponible >= montoApostado)
      apuestas.map(_.jugar(suceso)).sum
    else
      0
  }

}

/*
case class Apuestas(apuestas: List[Apuesta], juegos: List[Juego]){
  require(apuestas.lengthCompare(juegos.size) == 0)
  def resultadosPosibles(montoInicial: Int): DistribucionResultados = apuestas.zip(juegos)
    .foldLeft(DistribucionResultados(List(EscenarioPosible(montoInicial)))) { (resultadoAnterior, apuestaJuego) => {
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
*/

case class Juego(distribucionResultados: DistribucionProbabilidad){
  def proximoResultado:Suceso = distribucionResultados.proximoSuceso()
}

case class DistribucionResultados(escenarios: List[EscenarioPosible]){
  def obtenerResultadosPosiblesPara(apuestas: List[ApuestaCompleta]):DistribucionResultados =
    copy(escenarios = for {
      escenariosPosible <- escenarios
      escenario <- escenariosPosible.jugar(apuestas)
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

  def jugar(listaApuestas: List[ApuestaCompleta]): List[EscenarioPosible] =
    listaApuestas.foldLeft(List(this)){
      (escenariosPosibles: List[EscenarioPosible], apuestaCompleta: ApuestaCompleta) =>
        val escenariosSinProcesar = escenariosPosibles.filter(_.monto < apuestaCompleta.montoApostado)
        val escenariosAProcesar = escenariosPosibles.filter(_.monto >= apuestaCompleta.montoApostado)
        val escenariosProcesados = apuestaCompleta.apuestas.foldLeft(escenariosAProcesar){ (escenariosPosibles: List[EscenarioPosible], apuesta: Apuesta) =>
            escenariosPosibles.flatMap { (escenarioPosible) =>
              apuesta.jugada.escenariosPosibles(apuesta.montoApostado, apuestaCompleta.distribucion).map((escenario) =>
                escenario.copy(monto = escenarioPosible.monto - apuesta.montoApostado + escenario.monto,
                  probabilidad = DoubleFormatter.format(escenario.probabilidad * escenarioPosible.probabilidad)))
            }
          }
         escenariosSinProcesar:::escenariosProcesados
    }
}
