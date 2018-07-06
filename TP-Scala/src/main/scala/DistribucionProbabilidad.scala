import scala.util.Random

sealed trait DistribucionProbabilidad{
  def probabilidadDe(suceso:Suceso): Double
  def sucesosPosibles(): List[Suceso] = probalidadesSucesos.filter(_.esPosible)
    .map(_.suceso)

  protected def probalidadesSucesos: List[ProbabilidadSuceso]
}

case class DistribucionProbabilidadBase(probabilidadesSucesos : List[ProbabilidadSuceso]) extends DistribucionProbabilidad {
  require(probabilidadesSucesos.map(_.probabilidad).sum == 1)

  override def probabilidadDe(suceso: Suceso): Double = probabilidadesSucesos.find(_.matcheaSuceso(suceso))
    .map(_.probabilidad)
    .getOrElse(0.0)

  override protected val probalidadesSucesos: List[ProbabilidadSuceso] = probabilidadesSucesos
}

case class DistribucionCaraCruz(probabilidadCara: Double = 0.5) extends DistribucionProbabilidad{
  require(probabilidadCara >= 0 && probabilidadCara <= 1)

  override def probabilidadDe(suceso:Suceso): Double = suceso match {
    case Cara => probabilidadCara
    case Cruz => 1 - probabilidadCara
    case _ => 0
  }

  override protected val probalidadesSucesos: List[ProbabilidadSuceso] =
    List(ProbabilidadSuceso(Cara, RangoProbabilidad(0, probabilidadCara)), ProbabilidadSuceso(Cruz, RangoProbabilidad(0, 1 - probabilidadCara)))
}

/*case class DistribucionPonderada(sucesos: List[SucesoPonderado]) extends DistribucionProbabilidad{
  require(sucesos.nonEmpty)
 //a mayor peso mayor probabilidad
  override def probabilidadDe(suceso: Suceso): Double = sucesos.find(_.suceso == suceso) match{
    case Some(suc) => suc.peso / sucesos.map(_.peso).sum
    case None => 0.0
  }
   override protected val probalidadesSucesos: List[ProbabilidadSuceso] = probabilidadesSuceso
}*/

object DistribucionProbabilidadFactory{
  def eventoSeguro(suceso: Suceso):DistribucionProbabilidad = suceso match {
    case Cara => DistribucionCaraCruz(1)
    case Cruz => DistribucionCaraCruz(0)
    case `suceso` => DistribucionProbabilidadBase(List(ProbabilidadSuceso(suceso, RangoProbabilidad(0.0, 1.0))))
  }

  def distribucionEquiprobable(sucesosPosibles: List[Suceso]): DistribucionProbabilidad ={
    val probabilidad:Double = 1.0 / sucesosPosibles.size

    DistribucionProbabilidadBase(sucesosPosibles.zipWithIndex.map { case (suceso, index) => ProbabilidadSuceso(suceso, RangoProbabilidad(index * probabilidad, (index + 1) * probabilidad))})
  }

  def distribucionCaraCruzCargada(pesoCara: Int, pesoCruces: Int) =
    DistribucionCaraCruz(1 - 1.0 * pesoCara / (pesoCara + pesoCruces))

  def distribucionPonderada(sucesosPosibles: List[SucesoRuletaPonderado]):DistribucionProbabilidadBase ={
    require(sucesosPosibles.nonEmpty)

    if(sucesosPosibles.lengthCompare(1) == 0){
      DistribucionProbabilidadBase(sucesosPosibles.map((suceso) => {
        ProbabilidadSuceso(suceso.sucesoRuleta, RangoProbabilidad(0.0, 1.0))
      }))
    }else{
      val pesoTotal: Int = sucesosPosibles.map(_.peso).sum
      var inicioIntervalo = 0.0

      DistribucionProbabilidadBase(sucesosPosibles.map((suceso) => {
        val inicio = inicioIntervalo
        val fin = inicioIntervalo + 1 - 1.0 * suceso.peso / pesoTotal
        inicioIntervalo = fin
        ProbabilidadSuceso(suceso.sucesoRuleta, RangoProbabilidad(inicio, fin))
      }))
    }
  }


  val distribucionEquiprobableCaraCruz:DistribucionProbabilidad = DistribucionCaraCruz()
  val distribucionEquiprobableRuleta:DistribucionProbabilidad = DistribucionProbabilidadFactory.distribucionEquiprobable((0 to 36 toList).map(SucesoRuleta))
}
