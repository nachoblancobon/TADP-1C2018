import org.scalatest._

class JuegosTest extends FlatSpec with Matchers {
  "Si no hay monto suficiente" should  " no apostar" in{
    val distribucionPerdedora = DistribucionCaraCruz(0.0)
    val jugarACara = JugarACara()
    jugarACara.probabilidadVictoria(distribucionPerdedora) should be(0.0)

    val apuesta = Apuesta(1000, List(jugarACara))

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(10, 1.0)))

    distribucionResultado.obtenerResultadosPosiblesPara(apuesta, distribucionPerdedora) should be(distribucionResultado)
  }

  "Si hay monto suficiente " should  " apostar" in{
    val distribucion = DistribucionCaraCruz(0.75)
    val jugarACara = JugarACara()
    jugarACara.probabilidadVictoria(distribucion) should be(0.75)

    val apuesta = Apuesta(1000, List(jugarACara))

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(100000, 1.0)))

    distribucionResultado.obtenerResultadosPosiblesPara(apuesta, distribucion) should be(DistribucionResultados(List(EscenarioPosible(101000, 0.75), EscenarioPosible(99000, 0.25))))
  }

  "Al apostar " should  " excluir resultados imposibles" in{
    val distribucion = DistribucionCaraCruz(1.0)
    val jugarACara = JugarACara()
    jugarACara.probabilidadVictoria(distribucion) should be(1.0)

    val apuesta = Apuesta(1000, List(jugarACara))

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(100000, 1.0)))

    distribucionResultado.obtenerResultadosPosiblesPara(apuesta, distribucion) should be(DistribucionResultados(List(EscenarioPosible(101000, 1.0))))
  }

  "Al jugar múltiples veces " should  " multiplicar probabilidades" in{
    val distribucion = DistribucionCaraCruz(0.75)
    val jugarACara = JugarACara()
    jugarACara.probabilidadVictoria(distribucion) should be(0.75)

    val apuesta = Apuesta(1000, List(jugarACara))

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(100000, 1.0)))

    val distribucionResultado2 = distribucionResultado.obtenerResultadosPosiblesPara(apuesta, distribucion)

    distribucionResultado2.obtenerResultadosPosiblesPara(apuesta, distribucion) should be(DistribucionResultados(
      List(EscenarioPosible(102000, 0.5625),
          EscenarioPosible(100000, 0.1875),
          EscenarioPosible(100000, 0.1875),
          EscenarioPosible(98000,  0.0625))))
  }

  "Al jugar múltiples veces " should  " jugar sólo las apuestas posibles probabilidades" in{
    val distribucion = DistribucionCaraCruz(0.75)
    val jugarACara = JugarACara()
    jugarACara.probabilidadVictoria(distribucion) should be(0.75)

    val apuesta = Apuesta(1000, List(jugarACara))

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(1000, 1.0)))

    val distribucionResultado2 = distribucionResultado.obtenerResultadosPosiblesPara(apuesta, distribucion)

    distribucionResultado2.obtenerResultadosPosiblesPara(apuesta, distribucion) should be(DistribucionResultados(
      List(EscenarioPosible(3000, 0.5625),
        EscenarioPosible(1000, 0.1875),
        EscenarioPosible(0,  0.25))))
  }

  "Las jugadas compuestas " should " generar todas la combinación de escenarios" in{
    val distribucion = DistribucionProbabilidadFactory.distribucionEquiprobableRuleta

    val jugarAlRojo = JugarAlRojo()
    val jugarAlTres = JugarNumero(3)

    val apuestaCompuesta = Apuesta(1000, List(jugarAlRojo, jugarAlTres))

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(100000, 1.0)))

    distribucionResultado.obtenerResultadosPosiblesPara(apuestaCompuesta, distribucion) should be (DistribucionResultados(
      List(EscenarioPosible(136000, 0.013148),
          EscenarioPosible(100000, 0.473338),
          EscenarioPosible(134000, 0.013879),
          EscenarioPosible(98000,  0.499635))))
  }
}
