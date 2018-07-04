import org.scalatest._

class JugadorTest extends FlatSpec with Matchers {
  "Jugador " should  " elegir la apuesta según la estratega" in {
    val distribucion = DistribucionCaraCruz(0.75)
    val distribucion2 = DistribucionCaraCruz(0.5)
    val jugarACara = JugarACara

    val apuestas1 = PlanDeJuego(List(Juego(ApuestaSimple(10, jugarACara), distribucion)))
    val apuestas2 = PlanDeJuego(List(Juego(ApuestaSimple(15, jugarACara), distribucion2)))

    val jugadorRacional = Jugadores.jugadorRacional(20)
    val jugadorArriesgado = Jugadores.jugadorArriesgado(20)
    val jugadorCauto = Jugadores.jugadorCauto(20)

    jugadorRacional.elegirJuegos(List(apuestas1, apuestas2)) should be(apuestas1)
    jugadorArriesgado.elegirJuegos(List(apuestas1, apuestas2)) should be(apuestas2)
    jugadorCauto.elegirJuegos(List(apuestas1, apuestas2)) should be(apuestas1)
  }
}
