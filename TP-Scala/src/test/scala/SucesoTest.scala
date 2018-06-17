import org.scalatest._

class SucesoTest extends FlatSpec with Matchers {

  "Un suceso ruleta " should " ser válido si su valore está entre 0 y 36" in {
    SucesoRuleta(0) should be (SucesoRuleta(0))
    SucesoRuleta(15) should be (SucesoRuleta(15))
    SucesoRuleta(36) should be (SucesoRuleta(36))
  }

  "Un suceso ruleta " should " ser inválido si su valore no está entre 0 y 36" in {
    assertThrows[IllegalArgumentException] {
      SucesoRuleta(-1)
    }

    assertThrows[IllegalArgumentException] {
      SucesoRuleta(37)
    }
  }
}