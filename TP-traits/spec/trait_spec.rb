require 'rspec'
require_relative '../../TP-traits/spec/fixture.rb'

describe 'Tests de traits' do

  it 'Una clase usa el un trait ok' do
    clase = MiClase.new
    clase.metodo1.should == "mundo"
    clase.metodo2(3).should == 42
  end

  it 'Una clase usa un trait pero le resta un metodo' do
    sin_metodo = SinUnMetodo.new
    sin_metodo.metodo3(1).should == 2
    expect {sin_metodo.metodo1}.to raise_error (NoMethodError)
  end

  it 'Una clase usa dos traits pero a una le resta un metodo conflictivo' do
    sin_conflicto = SinConflicto.new
    sin_conflicto.metodo3(2).should == 4
    sin_conflicto.metodo2(1).should == 42
    sin_conflicto.metodo1.should == "Hola"
  end

  it 'Una clase que usa alias en un metodo' do
    clase = ConAlias.new
    clase.metodo1.should == "Hola"
    clase.saludo.should == "Hola"
    clase.metodo2(1).should == 42
  end
  
  it 'Si usa estrategia Todos devuelve como si ubiese ejecutado todos los metodos conflictivos' do
    b = B.new
    b.metodo1.should == "Chau"
  end

  it 'Si usa estrategia Fold devuelve el resultado de aplicar la funcion a los resultados parciales'do
    c = C.new
    c.m {|x,y| x*y}.should == 20
  end

  it 'Si usa estrategia Condicional devuelve el primer que cumpla la condicion de la funcion' do
    d = Condicional.new
    d.m {|x| x>1}.should == 2
  end

  it 'Si tengo estrategia fold con una funcion que sume suma cada resultado' do
    superman = SuperGuerrero.new
    superman.atacar(10){|x,y| x+y}.should == 120
  end

  it 'Si creo una estrategia sume resultados de los metodos conflictivos devuelve bien' do
    a = A.new
    a.suma(1,2).should == 7
  end
end

