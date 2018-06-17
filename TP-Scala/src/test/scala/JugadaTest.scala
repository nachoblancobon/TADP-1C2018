import org.scalatest._

class JugadaTest extends FlatSpec with Matchers {
  /* Probabilidad Victoria
  ###############################################
  */
  "La probabilidad de ganar de JugarACara " should " depende de la Distribucion" in{
    val jugarACara = JugarACara()
    jugarACara.probabilidadVictoria(DistribucionCaraCruz(1)) should be(1.0)
    jugarACara.probabilidadVictoria(DistribucionCaraCruz(0.5)) should be(0.5)
    jugarACara.probabilidadVictoria(DistribucionCaraCruz(0.3)) should be(0.3)
    jugarACara.probabilidadVictoria(DistribucionCaraCruz(0.0)) should be(0.0)
  }

  "La probabilidad de ganar de JugarACruz " should " depende de la Distribucion" in{
    val jugarACruz = JugarACruz()
    jugarACruz.probabilidadVictoria(DistribucionCaraCruz(1)) should be(0.0)
    jugarACruz.probabilidadVictoria(DistribucionCaraCruz(0.5)) should be(0.5)
    jugarACruz.probabilidadVictoria(DistribucionCaraCruz(0.3)) should be(0.7)
    jugarACruz.probabilidadVictoria(DistribucionCaraCruz(0.0)) should be(1.0)
  }

  "La probabilidad de ganar de JugarACara " should " con moneda cargada" in{
    val jugarACara = JugarACara()
    jugarACara.probabilidadVictoria(DistribucionProbabilidadFactory.distribucionCaraCruzCargada(4, 3)) should be(0.571429)
  }

  "La probabilidad de ganar de JugarACruz " should " con moneda cargada" in{
    val jugarACruz = JugarACruz()
    jugarACruz.probabilidadVictoria(DistribucionProbabilidadFactory.distribucionCaraCruzCargada(4, 3)) should be(0.428571)
  }

  "La probabilidad de JugarAlRojo " should " depende de la Distribucion" in{
    val jugarAlRojo = JugarAlRojo()

    jugarAlRojo.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(1.0)
    jugarAlRojo.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(2))) should be(0)

    jugarAlRojo.probabilidadVictoria(DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(DoubleFormatter.format(18.0/37.0))
  }

  "La probabilidad de JugarAlNegro " should " depende de la Distribucion" in{
    val jugarAlNegro = JugarAlNegro()

    jugarAlNegro.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(0)
    jugarAlNegro.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(2))) should be(1.0)

    jugarAlNegro.probabilidadVictoria(DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(DoubleFormatter.format(18.0/37.0))
  }

  "La probabilidad de JugarANumero " should " depende de la Distribucion" in{
    (0 to 36).map(JugarNumero).foreach(_.probabilidadVictoria(DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(DoubleFormatter.format(1.0/37.0)))

    JugarNumero(1).probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(1.0)
    JugarNumero(2).probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(0.0)
  }

  "La probabilidad de JugarPar " should " depende de la Distribucion" in{
    val jugarPar = JugarPar()

    jugarPar.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(0)
    jugarPar.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(2))) should be(1.0)

    jugarPar.probabilidadVictoria(DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(DoubleFormatter.format(18.0/37.0))
  }

  "La probabilidad de JugarImpar " should " depende de la Distribucion" in{
    val jugarImpar = JugarImpar()

    jugarImpar.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(1.0)
    jugarImpar.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(2))) should be(0)

    jugarImpar.probabilidadVictoria(DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(DoubleFormatter.format(18.0/37.0))
  }

  "La probabilidad de JugarDecena " should " depende de la Distribucion" in{
    val jugarDecena1 = JugarDecena(1)
    val jugarDecena2 = JugarDecena(2)
    val jugarDecena3 = JugarDecena(3)

    jugarDecena1.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(1.0)
    jugarDecena2.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(0.0)
    jugarDecena3.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(0.0)

    jugarDecena1.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(17))) should be(0.0)
    jugarDecena2.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(17))) should be(1.0)
    jugarDecena3.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(17))) should be(0.0)

    jugarDecena1.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(33))) should be(0.0)
    jugarDecena2.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(33))) should be(0.0)
    jugarDecena3.probabilidadVictoria(DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(33))) should be(1.0)

    jugarDecena1.probabilidadVictoria(DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(DoubleFormatter.format(12.0/37.0))
    jugarDecena2.probabilidadVictoria(DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(DoubleFormatter.format(12.0/37.0))
    jugarDecena3.probabilidadVictoria(DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(DoubleFormatter.format(12.0/37.0))
  }

  /* Escenarios posibles
  ###############################################
  */

  "Los escenarios posibles de JugarACara " should " son duplicar monto o perder todo" in{
    val jugarACara = JugarACara()

    jugarACara.escenariosPosibles(10, DistribucionCaraCruz(1)) should be(Set(EscenarioPosible(20, 1.0), EscenarioPosible(0, 0.0)))
    jugarACara.escenariosPosibles(10,DistribucionCaraCruz(0.5)) should be(Set(EscenarioPosible(20, 0.5), EscenarioPosible(0, 0.5)))
    jugarACara.escenariosPosibles(10,DistribucionCaraCruz(0.3)) should be(Set(EscenarioPosible(20, 0.3), EscenarioPosible(0, 0.7)))
    jugarACara.escenariosPosibles(10,DistribucionCaraCruz(0.0)) should be(Set(EscenarioPosible(20, 0.0), EscenarioPosible(0, 1.0)))
  }

  "Los escenarios posibles de JugarACruz " should " son duplicar monto o perder todo" in{
    val jugarACruz = JugarACruz()

    jugarACruz.escenariosPosibles(10, DistribucionCaraCruz(1)) should be(Set(EscenarioPosible(20, 0.0), EscenarioPosible(0, 1.0)))
    jugarACruz.escenariosPosibles(10,DistribucionCaraCruz(0.5)) should be(Set(EscenarioPosible(20, 0.5), EscenarioPosible(0, 0.5)))
    jugarACruz.escenariosPosibles(10,DistribucionCaraCruz(0.3)) should be(Set(EscenarioPosible(20, 0.7), EscenarioPosible(0, 0.3)))
    jugarACruz.escenariosPosibles(10,DistribucionCaraCruz(0.0)) should be(Set(EscenarioPosible(20, 1.0), EscenarioPosible(0, 0.0)))
  }

  "Los escenarios posibles de JugarAlRojo " should " son duplicar monto o perder todo" in{
    val jugarAlRojo = JugarAlRojo()

    jugarAlRojo.escenariosPosibles(10, DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(Set(EscenarioPosible(20, 1.0), EscenarioPosible(0, 0.0)))
    jugarAlRojo.escenariosPosibles(10, DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(2))) should be(Set(EscenarioPosible(20, 0.0), EscenarioPosible(0, 1.0)))

    jugarAlRojo.escenariosPosibles(10, DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(Set(EscenarioPosible(20, DoubleFormatter.format(18.0/37.0)), EscenarioPosible(0, DoubleFormatter.format(19.0/37.0))))
  }

  "Los escenarios posibles de JugarAlNegro " should " son duplicar monto o perder todo" in{
    val jugarAlNegro = JugarAlNegro()

    jugarAlNegro.escenariosPosibles(10, DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(Set(EscenarioPosible(20, 0.0), EscenarioPosible(0, 1.0)))
    jugarAlNegro.escenariosPosibles(10, DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(2))) should be(Set(EscenarioPosible(20, 1.0), EscenarioPosible(0, 0.0)))

    jugarAlNegro.escenariosPosibles(10, DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(Set(EscenarioPosible(20, DoubleFormatter.format(18.0/37.0)), EscenarioPosible(0, DoubleFormatter.format(19.0/37.0))))
  }

  "Los escenarios posibles de JugarANumero " should " son duplicar monto o perder todo" in{
    (0 to 36).map(JugarNumero).foreach(_.escenariosPosibles(10, DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(Set(EscenarioPosible(360, DoubleFormatter.format(1.0/37.0)), EscenarioPosible(0, DoubleFormatter.format(36.0/37.0)))))

    JugarNumero(1).escenariosPosibles(10, DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(Set(EscenarioPosible(360, 1.0), EscenarioPosible(0, 0.0)))
    JugarNumero(2).escenariosPosibles(10, DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(Set(EscenarioPosible(360, 0.0), EscenarioPosible(0, 1.0)))
  }

  "Los escenarios posibles de JugarPar " should " son duplicar monto o perder todo" in{
    val jugarPar = JugarPar()

    jugarPar.escenariosPosibles(10, DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(Set(EscenarioPosible(20, 0.0), EscenarioPosible(0, 1.0)))
    jugarPar.escenariosPosibles(10, DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(2))) should be(Set(EscenarioPosible(20, 1.0), EscenarioPosible(0, 0.0)))

    jugarPar.escenariosPosibles(10, DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(Set(EscenarioPosible(20, DoubleFormatter.format(18.0/37.0)), EscenarioPosible(0, DoubleFormatter.format(19.0/37.0))))
  }

  "Los escenarios posibles de JugarImpar " should " son duplicar monto o perder todo" in{
    val jugarImpar = JugarImpar()

    jugarImpar.escenariosPosibles(10, DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(Set(EscenarioPosible(20, 1.0), EscenarioPosible(0, 0.0)))
    jugarImpar.escenariosPosibles(10, DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(2))) should be(Set(EscenarioPosible(20, 0.0), EscenarioPosible(0, 1.0)))

    jugarImpar.escenariosPosibles(10, DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(Set(EscenarioPosible(20, DoubleFormatter.format(18.0/37.0)), EscenarioPosible(0, DoubleFormatter.format(19.0/37.0))))
  }

  "Los escenarios posibles de JugarDecena " should " son duplicar monto o perder todo" in{
    val jugarDecena1 = JugarDecena(1)
    val jugarDecena2 = JugarDecena(2)
    val jugarDecena3 = JugarDecena(3)

    jugarDecena1.escenariosPosibles(10,DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(Set(EscenarioPosible(30, 1.0), EscenarioPosible(0, 0.0)))
    jugarDecena2.escenariosPosibles(10,DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(Set(EscenarioPosible(30, 0.0), EscenarioPosible(0, 1.0)))
    jugarDecena3.escenariosPosibles(10,DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(1))) should be(Set(EscenarioPosible(30, 0.0), EscenarioPosible(0, 1.0)))

    jugarDecena1.escenariosPosibles(10,DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(17))) should be(Set(EscenarioPosible(30, 0.0), EscenarioPosible(0, 1.0)))
    jugarDecena2.escenariosPosibles(10,DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(17))) should be(Set(EscenarioPosible(30, 1.0), EscenarioPosible(0, 0.0)))
    jugarDecena3.escenariosPosibles(10,DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(17))) should be(Set(EscenarioPosible(30, 0.0), EscenarioPosible(0, 1.0)))

    jugarDecena1.escenariosPosibles(10,DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(33))) should be(Set(EscenarioPosible(30, 0.0), EscenarioPosible(0, 1.0)))
    jugarDecena2.escenariosPosibles(10,DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(33))) should be(Set(EscenarioPosible(30, 0.0), EscenarioPosible(0, 1.0)))
    jugarDecena3.escenariosPosibles(10,DistribucionProbabilidadFactory.eventoSeguro(SucesoRuleta(33))) should be(Set(EscenarioPosible(30, 1.0), EscenarioPosible(0, 0.0)))

    jugarDecena1.escenariosPosibles(10,DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(Set(EscenarioPosible(30, DoubleFormatter.format(12.0/37.0)), EscenarioPosible(0, DoubleFormatter.format(25.0/37.0))))
    jugarDecena2.escenariosPosibles(10,DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(Set(EscenarioPosible(30, DoubleFormatter.format(12.0/37.0)), EscenarioPosible(0, DoubleFormatter.format(25.0/37.0))))
    jugarDecena3.escenariosPosibles(10,DistribucionProbabilidadFactory.distribucionEquiprobableRuleta) should be(Set(EscenarioPosible(30, DoubleFormatter.format(12.0/37.0)), EscenarioPosible(0, DoubleFormatter.format(25.0/37.0))))
  }
}

