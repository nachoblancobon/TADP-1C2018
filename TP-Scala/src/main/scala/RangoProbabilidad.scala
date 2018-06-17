case class RangoProbabilidad(inicio:Double, fin:Double){
  require(inicio >= 0 && fin <= 1.0 && inicio <= fin)

  val tamano: Double = fin - inicio

  val esRangoNulo: Boolean = tamano == 0

  def valorEnRango(valor: Double):Boolean = inicio <= valor && valor <= fin
}