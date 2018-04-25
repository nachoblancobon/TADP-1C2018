module GeneradorMetodos
  def uses(trait)
    trait.singleton_methods.each do |method_sym|
      metodo = trait.method(method_sym)
      self.method(:define_method).call(method_sym, &metodo)
    end
  end
end

class Trait
  def <<(operacion_renombrar)
    nuevo_trait = Trait.new

    nuevo_trait.new_method(operacion_renombrar.nombre_nuevo, &self.method(operacion_renombrar.nombre_original))

    self + nuevo_trait
  end

  def +(other_trait)
    nuevo_trait = Trait.new

    nuevo_trait.singleton_class.uses(self)
    nuevo_trait.singleton_class.uses(other_trait)

    conflictos = self.singleton_methods & other_trait.singleton_methods

    conflictos.each do |conflicto|
      nuevo_trait.new_method(conflicto) do
        raise "Metodo con conflicto"
      end
    end

    nuevo_trait
  end

  def -(metodo)
    nuevo_trait = Trait.new
    nuevo_trait.singleton_class.uses(self)

    nuevo_trait.singleton_class.method(:remove_method).call(metodo)

    nuevo_trait
  end

  def self.define(&trait_definition)
    new_trait = self.new
    new_trait.instance_eval(&trait_definition)

    new_trait
  end

  def name(trait_name)
    Object.const_set(trait_name, self)
  end

  def new_method(method_name, &method_body)
    self.singleton_class.method(:define_method).call(method_name, &method_body)
  end

end

class Class
  include GeneradorMetodos
end

class OperacionRenombrar
  attr_accessor :nombre_original, :nombre_nuevo

  def initialize(nombre_original, nombre_nuevo)
    @nombre_original = nombre_original
    @nombre_nuevo = nombre_nuevo
  end

end

class Symbol
  def >(other_symbol)
    OperacionRenombrar.new(self, other_symbol)
  end
end


#### Ejemplo de uso ####

trait = Trait.define do
  name :MiTrait
  new_method :metodo1 do
    "Hola"
  end
  new_method :metodo2 do |un_numero|
    un_numero * 0 + 42
  end
end

trait2 = Trait.define do
  name :MiTrait2
  new_method :metodo1 do
    "Chau"
  end
end

class A
  uses MiTrait + Mitrait2
end
