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
Trait.define do
  name :MiTrait3
  new_method :m do
    puts "metodo m trait3"
    1
  end
end

Trait.define do
  name :MiTrait4
  new_method :m do
    puts "metodo m trait4"
    2
  end
end
Trait.define do
  name :MiTrait7
  new_method :m do
    puts "metodo m trait7"
    10
  end
end
Trait.define do
  name :MiTrait5
  new_method :suma do |a, b|
    a+b
  end
end
Trait.define do
  name :MiTrait6
  new_method :suma do |a, b|
    a+b+1
  end
end

Trait.define do
  name :Guerrero
  new_method :atacar do |potencialOfensivo|
    potencialOfensivo * 2
  end
end
Trait.define do
  name :Misil
  new_method :atacar do |potencialOfensivo|
    potencialOfensivo * 10
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

EstNueva = Proc.new do |conflictos, args|
  res=0
  conflictos.each do |x|
    res += x.call(*args)
  end
  res
end

class A
  uses MiTrait5 + MiTrait6 ,&EstNueva
end

class B
  uses MiTrait + MiOtroTrait, &Estrategia.estrategia_mostrar_todos
end

class C
  uses MiTrait3 + MiTrait4 + MiTrait7, &Estrategia.estrategia_fold 
end

class SuperGuerrero
  uses Guerrero + Misil, &Estrategia.estrategia_fold
end

class Condicional
  uses MiTrait3 + MiTrait4 + MiTrait7, &Estrategia.estrategia_condicional
end
