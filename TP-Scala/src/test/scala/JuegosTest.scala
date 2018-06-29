import org.scalatest._

class JuegosTest extends FlatSpec with Matchers {
  "Si no hay monto suficiente" should  " no apostar" in{
    val distribucionPerdedora = DistribucionCaraCruz(0.0)
    val jugarACara = JugarACara()
    jugarACara.probabilidadVictoria(distribucionPerdedora) should be(0.0)

    val apuesta = ApuestaCompleta(List(Apuesta(1000, jugarACara)), distribucionPerdedora)

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(10, 1.0)))

    distribucionResultado.obtenerResultadosPosiblesPara(List(apuesta)) should be(distribucionResultado)
  }

  "Si hay monto suficiente " should  " apostar" in{
    val distribucion = DistribucionCaraCruz(0.75)
    val jugarACara = JugarACara()
    jugarACara.probabilidadVictoria(distribucion) should be(0.75)

    val apuesta = ApuestaCompleta(List(Apuesta(1000, jugarACara)), distribucion)

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(100000, 1.0)))

    distribucionResultado.obtenerResultadosPosiblesPara(List(apuesta)) should be(DistribucionResultados(List(EscenarioPosible(101000, 0.75), EscenarioPosible(99000, 0.25))))
  }

  "Al apostar " should  " excluir resultados imposibles" in{
    val distribucion = DistribucionCaraCruz(1.0)
    val jugarACara = JugarACara()
    jugarACara.probabilidadVictoria(distribucion) should be(1.0)

    val apuesta = ApuestaCompleta(List(Apuesta(1000, jugarACara)), distribucion)

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(100000, 1.0)))

    distribucionResultado.obtenerResultadosPosiblesPara(List(apuesta)) should be(DistribucionResultados(List(EscenarioPosible(101000, 1.0))))
  }

  "Al jugar múltiples veces " should  " multiplicar probabilidades" in{
    val distribucion = DistribucionCaraCruz(0.75)
    val jugarACara = JugarACara()
    jugarACara.probabilidadVictoria(distribucion) should be(0.75)

    val apuesta = ApuestaCompleta(List(Apuesta(1000, jugarACara)), distribucion)

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(100000, 1.0)))

    val distribucionResultado2 = distribucionResultado.obtenerResultadosPosiblesPara(List(apuesta))

    distribucionResultado2.obtenerResultadosPosiblesPara(List(apuesta)) should be(DistribucionResultados(
      List(EscenarioPosible(102000, 0.5625),
        EscenarioPosible(100000, 0.1875),
        EscenarioPosible(100000, 0.1875),
        EscenarioPosible(98000,  0.0625))))
  }

  "Al jugar múltiples veces " should  " jugar sólo las apuestas posibles probabilidades" in{
    val distribucion = DistribucionCaraCruz(0.75)
    val jugarACara = JugarACara()
    jugarACara.probabilidadVictoria(distribucion) should be(0.75)

    val apuesta = ApuestaCompleta(List(Apuesta(1000, jugarACara)), distribucion)

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(1000, 1.0)))

    val distribucionResultado2 = distribucionResultado.obtenerResultadosPosiblesPara(List(apuesta))

    distribucionResultado2.obtenerResultadosPosiblesPara(List(apuesta)) should be(DistribucionResultados(
      List(EscenarioPosible(3000, 0.5625),
        EscenarioPosible(1000, 0.1875),
        EscenarioPosible(0,  0.25))))
  }

  "Las jugadas compuestas " should " generar todas la combinación de escenarios" in{
    val distribucion = DistribucionProbabilidadFactory.distribucionEquiprobableRuleta

    val jugarAlRojo = JugarAlRojo()
    val jugarAlTres = JugarNumero(3)

    val apuestaCompuesta = ApuestaCompleta(List(Apuesta(1000, jugarAlRojo), Apuesta(1000, jugarAlTres)), distribucion)

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(100000, 1.0)))

    distribucionResultado.obtenerResultadosPosiblesPara(List(apuestaCompuesta)) should be (DistribucionResultados(
      List(EscenarioPosible(136000, 0.013148),
        EscenarioPosible(100000, 0.473338),
        EscenarioPosible(134000, 0.013879),
        EscenarioPosible(98000,  0.499635))))
  }

  "Ejemplo del enunciado" should " deberia dar lo que pide" in{
    val distribucion = DistribucionProbabilidadFactory.distribucionEquiprobableRuleta

    val jugarAlRojo = JugarAlRojo()
    val jugarAlVeintiTres = JugarNumero(23)
    val jugarASegundaDocena = JugarDecena(2)

    val apuestaCompuesta = ApuestaCompleta(List(Apuesta(25, jugarAlRojo), Apuesta(10, jugarASegundaDocena), Apuesta(30, jugarAlVeintiTres)), distribucion)

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(65, 1.0)))

    distribucionResultado.obtenerResultadosPosiblesPara(List(apuestaCompuesta)) should be (DistribucionResultados(
        List(
          EscenarioPosible(1160,0.004264),
          EscenarioPosible(80,0.153515),
          EscenarioPosible(1130,0.008884),
          EscenarioPosible(50,0.319823),
          EscenarioPosible(1110,0.004501),
          EscenarioPosible(30,0.162044),
          EscenarioPosible(1080,0.009378),
          EscenarioPosible(0,0.337591))
    ))
  }

  "juegos sucesivos" should " deberia dar distribuciones correspondientes" in{
    val distribucion = DistribucionProbabilidadFactory.distribucionEquiprobableRuleta
    val distribucionCaraCruz = DistribucionCaraCruz(0.5)

    val jugarAlCero = JugarNumero(0)
    val jugarACara = JugarACara()

    val apuesta1 = ApuestaCompleta(List( Apuesta(10, jugarACara)), distribucionCaraCruz)
    val apuesta2 = ApuestaCompleta(List( Apuesta(15, jugarAlCero)), distribucion)

    val distribucionResultado = DistribucionResultados(List(EscenarioPosible(15, 1.0)))

    distribucionResultado.obtenerResultadosPosiblesPara(List(apuesta1, apuesta2)) should be (DistribucionResultados(
      List(
        EscenarioPosible(5,0.5),
        EscenarioPosible(550,0.013514),
        EscenarioPosible(10,0.486487))
    ))
  }

}
