public abstract class Primitivo {

    public Tipo tipo;
    protected Object valor; // valor pode receber apenas Integer, String, Character, Boolean ou Double. garantir sempre que estas serão as únicas classes!

    public Primitivo(Tipo tipo, Object valor) {
        this.tipo = tipo;
        this.valor = valor;
    }
}
