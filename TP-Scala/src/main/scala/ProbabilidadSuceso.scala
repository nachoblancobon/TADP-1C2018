case class ProbabilidadSuceso(suceso: Suceso, rango:RangoProbabilidad){
  val esPosible:Boolean = !rango.esRangoNulo

  val probabilidad:Double = rango.tamano

  def matcheaSuceso(sucesoAMatchear : Suceso):Boolean = suceso == sucesoAMatchear

  def valorEnRango(valor: Double):Boolean = rango.valorEnRango(valor)
}