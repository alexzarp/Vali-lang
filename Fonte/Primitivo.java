package Fonte;
public abstract class Primitivo {

    public Tipos tipo;

    // valor pode receber apenas Integer, String, Boolean ou Double.
    // garantir sempre que estas serão as únicas classes!
    // além disso, um valor null significa que a variável ainda não foi
    // inicializada;
    protected Object valor;

    public Primitivo(Tipos tipo, Object valor) {
        this.tipo = tipo;
        this.valor = valor;
    }
}
