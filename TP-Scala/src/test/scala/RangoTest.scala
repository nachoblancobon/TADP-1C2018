import org.scalatest._

class RangoTest extends FlatSpec with Matchers {

  "El tamaño de un rango " should " ser el fin menos el inicio" in {
    val rangoProbabilidad:RangoProbabilidad = RangoProbabilidad(0.0, 1.0)

    rangoProbabilidad.tamano should be(1.0)
  }

  "Un rango cuyo inicio y final son iguales " should " estar vacío" in {
    val rangoProbabilidad:RangoProbabilidad = RangoProbabilidad(0.0, 0.0)

    rangoProbabilidad.esRangoNulo should be(true)
  }

  "Un rango cuyo inicio y final son distinto " should " no estar vacío" in {
    val rangoProbabilidad:RangoProbabilidad = RangoProbabilidad(0.0, 1.0)

    rangoProbabilidad.esRangoNulo should be(false)
  }

  it should "throw IllegalArgumentException si el inicio es menor que 0" in {
    assertThrows[IllegalArgumentException] {
      RangoProbabilidad(-1, 0.5)
    }
  }

  it should "throw IllegalArgumentException si el final es mayor que 1" in {
    assertThrows[IllegalArgumentException] {
      RangoProbabilidad(0.0, 3.5)
    }
  }
}