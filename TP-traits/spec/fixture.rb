require_relative '../../TP-traits/src/traits.rb'

Trait.define do
  name :MiTrait
  new_method :metodo1 do
    "Hola"
  end
  new_method :metodo2 do |un_numero|
    un_numero * 0 + 42
  end
end

Trait.define do
  name :MiOtroTrait
  new_method :metodo1 do
    "Chau"
  end
  new_method :metodo3 do |numero1|
    numero1*2
  end
end
Trait.define do
  name :TraitParaRestarUno

  new_method :decrementar do
    self.var=self.var-1
  end
end

Trait.define do
  name :TraitParaRestarDos

  new_method :decrementar do
    self.var=self.var-2
  end
end

class MiClase
  uses MiTrait

  def metodo1
    "mundo"
  end
end

class SinUnMetodo
  uses MiOtroTrait - :metodo1
end

class SinConflicto
  uses MiTrait + (MiOtroTrait - :metodo1)
end

class ConAlias
  uses MiTrait << (:metodo1 > :saludo)
end