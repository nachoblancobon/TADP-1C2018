class Estrategia
  def self.bindear_conflictos(conflictos, estrategia)
    Proc.new do |*args, &bloque|
      estrategia.call(conflictos, args, &bloque)
    end
  end

  def self.estrategia_mostrar_todos
    Proc.new do |conflictos, args|
      res = nil
      conflictos.each do |x|
        res = x.call(*args)
      end
      res
    end
  end

  def self.estrategia_fold
    Proc.new do |conflictos, args, &funcion|
      res = conflictos.shift.call(*args)
      conflictos.each do |x|
        res = funcion.call(res, x.call(*args))
      end
      res
    end
  end

  def self.estrategia_condicional
    Proc.new do |conflictos, args, &funcion|
      res = nil
      conflictos.each do |x|
        res = x.call(*args)
        break if funcion.call(res)
      end
      res
    end
  end
end