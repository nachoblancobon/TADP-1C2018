import org.scalatest._

class JuegosTest extends FlatSpec with Matchers {
  "Si no hay monto suficiente" should " no apostar" in {
    val distribucionPerdedora = DistribucionCaraCruz(0.0)
    val jugarACara = JugarACara
    jugarACara.probabilidadVictoria(distribucionPerdedora) should be(0.0)

    val apuesta = ApuestaSimple(1000, jugarACara)

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(10, 1.0)))

    distribucionResultado.obtenerResultadosPosiblesPara(Juego(apuesta, distribucionPerdedora)) should be(distribucionResultado)
  }

  "Si hay monto suficiente " should " apostar" in {
    val distribucion = DistribucionCaraCruz(0.75)
    val jugarACara = JugarACara
    jugarACara.probabilidadVictoria(distribucion) should be(0.75)

    val apuesta = ApuestaSimple(1000, jugarACara)

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(100000, 1.0)))

    distribucionResultado.obtenerResultadosPosiblesPara(Juego(apuesta, distribucion)) should be(DistribucionResultados(List(EscenarioPosible(101000, 0.75), EscenarioPosible(99000, 0.25))))
  }

  "Al apostar " should " excluir resultados imposibles" in {
    val distribucion = DistribucionCaraCruz(1.0)
    val jugarACara = JugarACara
    jugarACara.probabilidadVictoria(distribucion) should be(1.0)

    val apuesta = ApuestaSimple(1000, jugarACara)

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(100000, 1.0)))

    distribucionResultado.obtenerResultadosPosiblesPara(Juego(apuesta, distribucion)) should be(DistribucionResultados(List(EscenarioPosible(101000, 1.0))))
  }

  "Al jugar múltiples veces " should " multiplicar probabilidades" in {
    val distribucion = DistribucionCaraCruz(0.75)
    val jugarACara = JugarACara
    jugarACara.probabilidadVictoria(distribucion) should be(0.75)

    val apuesta = ApuestaSimple(1000, jugarACara)

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(100000, 1.0)))

    val distribucionResultado2 = distribucionResultado.obtenerResultadosPosiblesPara(Juego(apuesta, distribucion))

    distribucionResultado2.obtenerResultadosPosiblesPara(Juego(apuesta, distribucion)) should be(DistribucionResultados(
      List(
        EscenarioPosible(102000, 0.5625),
        EscenarioPosible(100000, 0.1875),
        EscenarioPosible(100000, 0.1875),
        EscenarioPosible(98000, 0.0625))))
  }

  "Al jugar múltiples veces " should " jugar sólo las apuestas posibles probabilidades" in {
    val distribucion = DistribucionCaraCruz(0.75)
    val jugarACara = JugarACara
    jugarACara.probabilidadVictoria(distribucion) should be(0.75)

    val apuesta = ApuestaSimple(1000, jugarACara)

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(1000, 1.0)))

    val distribucionResultado2 = distribucionResultado.obtenerResultadosPosiblesPara(Juego(apuesta, distribucion))

    distribucionResultado2.obtenerResultadosPosiblesPara(Juego(apuesta, distribucion)) should be(DistribucionResultados(
      List(
        EscenarioPosible(3000, 0.5625),
        EscenarioPosible(1000, 0.1875),
        EscenarioPosible(0, 0.25))))
  }

  "Las jugadas compuestas " should " generar todas la combinación de escenarios" in {
    val distribucion = DistribucionProbabilidadFactory.distribucionEquiprobableRuleta

    val jugarAlRojo = JugarAlRojo
    val jugarAlTres = JugarNumero(3)

    val apuestaCompuesta = ApuestaCompuesta(List(ApuestaSimple(1000, jugarAlRojo), ApuestaSimple(1000, jugarAlTres)))

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(100000, 1.0)))

    distribucionResultado.obtenerResultadosPosiblesPara(Juego(apuestaCompuesta, distribucion)) should be(DistribucionResultados(
      List(
        EscenarioPosible(136000, 0.013148),
        EscenarioPosible(100000, 0.473338),
        EscenarioPosible(134000, 0.013879),
        EscenarioPosible(98000, 0.499635))))
  }

  "plan de juegos " should " jugar muchos juegos" in{
    val distribucion = DistribucionProbabilidadFactory.distribucionEquiprobableRuleta
    val distribucionCaraCruz = DistribucionCaraCruz(0.5)

    val jugarAlCero = JugarNumero(0)
    val jugarACara = JugarACara

    val apuesta1 = ApuestaCompuesta(List( ApuestaSimple(10, jugarACara)))
    val apuesta2 = ApuestaCompuesta(List( ApuestaSimple(15, jugarAlCero)))

    val planDeJuego= PlanDeJuego(List(Juego(apuesta1, distribucionCaraCruz), Juego(apuesta2, distribucion)))

    planDeJuego.resultadosPosibles(15) should be (DistribucionResultados(
      List(
        EscenarioPosible(550,0.013514),
        EscenarioPosible(10,0.486487),
        EscenarioPosible(5,0.5))
    ))
  }
  "Las distribuciones ponderadas " should " dar mayor probabilidad al mayor peso" in {
    var distribucion = DistribucionProbabilidadFactory.distribucionPonderada(List(SucesoRuletaPonderado(SucesoRuleta(3), 2)))

    distribucion.probabilidadDe(SucesoRuleta(3)) should be(1.0)

    distribucion = DistribucionProbabilidadFactory.distribucionPonderada(List(SucesoRuletaPonderado(SucesoRuleta(3), 2), SucesoRuletaPonderado(SucesoRuleta(5), 3)))

    distribucion.probabilidadDe(SucesoRuleta(3)) should be(0.4)
    distribucion.probabilidadDe(SucesoRuleta(5)) should be(0.6)

    distribucion = DistribucionProbabilidadFactory.distribucionPonderada(List(SucesoRuletaPonderado(SucesoRuleta(3), 2), SucesoRuletaPonderado(SucesoRuleta(5), 3), SucesoRuletaPonderado(SucesoRuleta(6), 5)))

    distribucion.probabilidadDe(SucesoRuleta(3)) should be(0.2)
    distribucion.probabilidadDe(SucesoRuleta(5)) should be(0.3)
    distribucion.probabilidadDe(SucesoRuleta(6)) should be(0.5)

  }

  "Arrancando con 65 si le jugamos 25 al rojo 10 a la 2da decena y 30 al numero 23" should "devolver como en el enunciado"in{
    val distribucion = DistribucionProbabilidadFactory.distribucionEquiprobableRuleta

    val jugarAlRojo = JugarAlRojo
    val jugarAl23 = JugarNumero(23)
    val jugar2daDecena = JugarDecena(2)

    val apuestaCompuesta = ApuestaCompuesta(List(ApuestaSimple(25, jugarAlRojo), ApuestaSimple(30, jugarAl23), ApuestaSimple(10, jugar2daDecena)))

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(65, 1.0)))

    distribucionResultado.obtenerResultadosPosiblesPara(Juego(apuestaCompuesta, distribucion)) should be(DistribucionResultados(
      List(
        EscenarioPosible(1160,0.004264),
        EscenarioPosible(1130,0.008884),
        EscenarioPosible(80,0.153515),
        EscenarioPosible(50,0.319823),
        EscenarioPosible(1110,0.004501),
        EscenarioPosible(1080,0.009378),
        EscenarioPosible(30,0.162044),
        EscenarioPosible(0,0.337591))))
  }
}
