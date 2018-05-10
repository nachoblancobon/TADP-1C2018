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
end

