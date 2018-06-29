import org.scalatest._

class JugadorTest extends FlatSpec with Matchers {
  /*
  "Jugador " should  " elegir la apuesta según la estratega" in {
    val distribucion = DistribucionCaraCruz(0.75)
    val distribucion2 = DistribucionCaraCruz(0.5)
    val jugarACara = JugarACara()

    val apuestas1 = Apuestas(List(Apuesta(10, List(jugarACara))), List(Juego(distribucion)))
    val apuestas2 = Apuestas(List(Apuesta(15, List(jugarACara))), List(Juego(distribucion2)))

    val jugadorRacional = Jugadores.jugadorRacional(20)
    val jugadorArriesgado = Jugadores.jugadorArriesgado(20)
    val jugadorCauto = Jugadores.jugadorCauto(20)

    jugadorRacional.elegirJuegos(List(apuestas1, apuestas2)) should be(apuestas1)
    jugadorArriesgado.elegirJuegos(List(apuestas1, apuestas2)) should be(apuestas2)
    jugadorCauto.elegirJuegos(List(apuestas1, apuestas2)) should be(apuestas1)
  }

  "Jugador " should " jugar varios juegos" in{
    val jugadorRacional = Jugadores.jugadorRacional(20)

    val apuestas1 = Apuestas(List(Apuesta(20, List(JugarACara()))), List(Juego(DistribucionProbabilidadFactory.eventoSeguro(Cara()))))
    val apuestas2 = Apuestas(List(Apuesta(30, List(JugarPar()))), List(Juego(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(2)))))

    jugadorRacional.jugar(apuestas1).jugar(apuestas2) should be(Jugadores.jugadorRacional(60))
  }
  */
}
