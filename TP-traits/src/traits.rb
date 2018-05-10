module GeneradorMetodos
  def uses(trait, &estrategia)
    trait.singleton_methods.each do |method_sym|
      metodo = trait.method(method_sym)
      self.method(:define_method).call(method_sym, &metodo)
    end

    if block_given?
      conflictos = trait.conflictos
      nombresMetodosACrear = (conflictos.map {|x| x.name}).uniq

      nombresMetodosACrear.each do |nom|
        codigoMetodo =Estrategia.bindear_conflictos(conflictos.select {|x| x.name === nom}, estrategia)
        self.method(:define_method).call(nom,codigoMetodo)
      end
    end
  end
end

class Estrategia
  def self.bindear_conflictos(conflictos, estrategia)
    Proc.new do |*args, &bloque|
      estrategia.call(conflictos, args, &bloque)
    end
  end
end

EstTodos= Proc.new do |conflictos, args|
  res=nil
  conflictos.each do |x|
    res = x.call(*args)
  end
  res
end

EstFold = Proc.new do |conflictos, args, &funcion|
  res = conflictos.shift.call(*args)
  conflictos.each do |x|
    res = funcion.call(res, x.call(*args))
  end
  res
end

EstCondicional = Proc.new do |conflictos, args, &funcion|
  res = nil
  conflictos.each do |x|
    res = x.call(*args)
    break if funcion.call(res)
  end
  res
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

    unionMetodosTraits = self.obtenerMetodos + other_trait.obtenerMetodos
    metodosRepetidos = self.conflictos + other_trait.conflictos #los conflictos que ya estaban
    nombres_repetidos = self.singleton_methods & other_trait.singleton_methods # los que surgen de la suma de traits actual
    nombres_repetidos +=  metodosRepetidos.map {|x| x.name} #los que estaban de antes en los conflictos
    metodosRepetidos += unionMetodosTraits.select { |x| nombres_repetidos.include?( x.name ) }
    nuevo_trait.conflictos = metodosRepetidos.uniq

    metodosSinConflictos = unionMetodosTraits.reject { |x| nuevo_trait.conflictos.include?( x ) }

    metodosSinConflictos.each do |m|
      nuevo_trait.singleton_class.method(:define_method).call(m.name, &m)
    end
    nuevo_trait
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
    puts "Hola"
  end
  new_method :metodo2 do |un_numero|
    un_numero * 0 + 42
  end
end

trait2 = Trait.define do
  name :MiTrait2
  new_method :metodo1 do
    puts "Chau"
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
    # puts a+b
    a+b
  end
end
Trait.define do
  name :MiTrait6
  new_method :suma do |a, b|
    # puts a+b+1
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

# TC1 = MiTrait + MiTrait2
# TC2 = TC1 + MiTrait2
# TC3 = MiTrait2 + MiTrait2
# TC4 = MiTrait + MiTrait

class A
  uses MiTrait5+ MiTrait6 do |conflictos, args|
    res=0
    conflictos.each do |x|
      res += x.call(*args)
    end
    res
  end
end

class B
  uses MiTrait5 + MiTrait6, &EstTodos
end


class C
  uses MiTrait3 + MiTrait4 + MiTrait7, &EstFold
end

class SuperGuerrero
  uses Guerrero + Misil, &EstFold
end

class Condicional
  uses MiTrait3 + MiTrait4 + MiTrait7, &EstCondicional
end

a= A.new
a.suma 1,2

c= C.new
c.m do|x,y|
  x*y
end


superman = SuperGuerrero.new
superman.atacar(10) {|x,y| x+y}

b = B.new
b.suma 1,2

c = Condicional.new
puts (c.m do |val| val>1 end)
