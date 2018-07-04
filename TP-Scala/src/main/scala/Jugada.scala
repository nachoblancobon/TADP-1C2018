import scala.language.postfixOps


sealed trait Jugada{
  def sucesosVictoriosos:List[Suceso]
  def montoVictoria(monto: Int):Int

  def jugar(resultado: Suceso, montoApostado: Int): Int = {
    if(sucesosVictoriosos.contains(resultado)){
      montoVictoria(montoApostado)
    }else{
      0
    }
  }

  def escenariosPosibles(montoApostado: Int, distribucionProbabilidad: DistribucionProbabilidad): List[EscenarioPosible] =
    List(
      EscenarioPosible(montoVictoria(montoApostado), probabilidadVictoria(distribucionProbabilidad)),
      EscenarioPosible(0, DoubleFormatter.format(1.0 - probabilidadVictoria(distribucionProbabilidad)))
    )

  def probabilidadVictoria(distribucionProbabilidad: DistribucionProbabilidad):Double = DoubleFormatter.format(sucesosVictoriosos.map(distribucionProbabilidad.probabilidadDe).sum)
}

object DoubleFormatter{
  def format(probabilidad:Double):Double = BigDecimal(probabilidad).setScale(6, BigDecimal.RoundingMode.HALF_UP).toDouble
}

case object JugarACara extends Jugada{
  override val sucesosVictoriosos: List[Suceso] = List(Cara)

  override def montoVictoria(monto: Int): Int = 2 * monto
}

case object JugarACruz extends Jugada{
  override val sucesosVictoriosos: List[Suceso] = List(Cruz)

  override def montoVictoria(monto: Int): Int = 2 * monto
}

case object JugarAlRojo extends Jugada{
  override val sucesosVictoriosos: List[Suceso] = List(1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36).map(SucesoRuleta)

  override def montoVictoria(monto: Int): Int = 2 * monto
}

case object JugarAlNegro extends Jugada{
  override val sucesosVictoriosos: List[Suceso] = List(2,4,6,8,10,11,13,15,17,20,22,24,26,28,29,31,33,35).map(SucesoRuleta)

  override def montoVictoria(monto: Int): Int = 2 * monto
}

case class JugarNumero(numero: Int) extends Jugada{
  override val sucesosVictoriosos: List[Suceso] = List(SucesoRuleta(numero))

  override def montoVictoria(monto: Int): Int = 36 * monto
}

case object JugarPar extends Jugada{
  override val sucesosVictoriosos: List[Suceso] = (2 to 36 by 2 toList).map(SucesoRuleta)

  override def montoVictoria(monto: Int): Int = 2 * monto
}

case object JugarImpar extends Jugada{
  override val sucesosVictoriosos: List[Suceso] = (1 to 35 by 2 toList).map(SucesoRuleta)

  override def montoVictoria(monto: Int): Int = 2 * monto
}

case class JugarDecena(decena:Int) extends Jugada{
  override val sucesosVictoriosos: List[Suceso] = decena match{
    case 1 => (1 to 12 toList).map(SucesoRuleta)
    case 2 => (13 to 24 toList).map(SucesoRuleta)
    case 3 => (25 to 36 toList).map(SucesoRuleta)
  }

  override def montoVictoria(monto: Int): Int = 3 * monto
}