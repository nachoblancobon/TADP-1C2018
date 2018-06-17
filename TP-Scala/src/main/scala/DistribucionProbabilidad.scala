import scala.util.Random

sealed trait DistribucionProbabilidad{
  def probabilidadDe(suceso:Suceso): Double
  def sucesosPosibles(): List[Suceso] = probalidadesSucesos.filter(_.esPosible)
    .map(_.suceso)

  def proximoSuceso():Suceso = {
    val valor:Double = Random.nextDouble()

    probalidadesSucesos.find(_.valorEnRango(valor)).map(_.suceso).get
  }

  protected def probalidadesSucesos: List[ProbabilidadSuceso]
}

case class DistribucionProbabilidadBase(probabilidadesSucesos : List[ProbabilidadSuceso]) extends DistribucionProbabilidad {
  require(probabilidadesSucesos.map(_.probabilidad).sum == 1)

  override def probabilidadDe(suceso: Suceso): Double = probabilidadesSucesos.find(_.matcheaSuceso(suceso))
    .map(_.probabilidad)
    .getOrElse(0.0)

  override protected val probalidadesSucesos: List[ProbabilidadSuceso] = probabilidadesSucesos
}

case class DistribucionCaraCruz(probabilidadCara: Double) extends DistribucionProbabilidad{
  require(probabilidadCara >= 0 && probabilidadCara <= 1)

  override def probabilidadDe(suceso:Suceso): Double = suceso match {
    case Cara() => probabilidadCara
    case Cruz() => 1 - probabilidadCara
    case _ => 0
  }

  override protected val probalidadesSucesos: List[ProbabilidadSuceso] =
    List(ProbabilidadSuceso(Cara(), RangoProbabilidad(0, probabilidadCara)), ProbabilidadSuceso(Cruz(), RangoProbabilidad(0, 1 - probabilidadCara)))
}

object DistribucionProbabilidadFactory{
  def eventoSeguro(suceso: Suceso):DistribucionProbabilidad = suceso match {
    case Cara() => DistribucionCaraCruz(1)
    case Cruz() => DistribucionCaraCruz(0)
    case `suceso` => DistribucionProbabilidadBase(List(ProbabilidadSuceso(suceso, RangoProbabilidad(0.0, 1.0))))
  }

  def distribucionEquiprobable(sucesosPosibles: List[Suceso]): DistribucionProbabilidad ={
    val probabilidad:Double = 1.0 / sucesosPosibles.size

    DistribucionProbabilidadBase(sucesosPosibles.zipWithIndex.map { case (suceso, index) => ProbabilidadSuceso(suceso, RangoProbabilidad(index * probabilidad, (index + 1) * probabilidad))})
  }

  def distribucionCaraCruzCargada(cantidadCaras: Int, cantidadCruces: Int) = DistribucionCaraCruz(1.0 * cantidadCaras / (cantidadCaras + cantidadCruces))

  val distribucionEquiprobableCaraCruz:DistribucionProbabilidad = DistribucionCaraCruz(0.5)
  val distribucionEquiprobableRuleta:DistribucionProbabilidad = DistribucionProbabilidadFactory.distribucionEquiprobable((0 to 36 toList).map(SucesoRuleta(_)))
}
