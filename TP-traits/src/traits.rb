module GeneradorMetodos
  def uses(trait)
    trait.singleton_methods.each do |method_sym|
      metodo = trait.method(method_sym)
      self.method(:define_method).call(method_sym, &metodo)
    end
  end
end

class Trait
  attr_accessor :conflictos

  def initialize
    self.conflictos= []
  end

  def <<(operacion_renombrar)
    nuevo_trait = Trait.new

    nuevo_trait.new_method(operacion_renombrar.nombre_nuevo, &self.method(operacion_renombrar.nombre_original))

    self + nuevo_trait
  end

  def +(other_trait)
    nuevo_trait = Trait.new

    nuevo_trait.singleton_class.uses(self)
    nuevo_trait.singleton_class.uses(other_trait)

    nombres_repetidos = self.singleton_methods & other_trait.singleton_methods
    nuevo_trait.conflictos = self.conflictos + other_trait.conflictos
    nuevo_trait.conflictos += (self.obtenerMetodos + other_trait.obtenerMetodos).select { |x| nombres_repetidos.include?( x.name ) }
    # nuevo_trait.conflictos = metodos.collect { |met| met.to_proc}
    nuevo_trait.limpiar_metodos_conflictivos

    nuevo_trait
  end

  def limpiar_metodos_conflictivos
    self.singleton_methods.each do |nombre|
      if self.conflictos.map{|x| x.name}.include? nombre
        self.conflictos.push(self.method(nombre))
        self.singleton_class.send(:remove_method, nombre)
      end
    end
  end
  def obtenerMetodos
    self.singleton_methods.collect do |simbolo|
      self.method simbolo
    end
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

TraitConflictivo = MiTrait + MiTrait2
TC2 = TraitConflictivo + MiTrait2

class A
  uses MiTrait + MiTrait2
end
