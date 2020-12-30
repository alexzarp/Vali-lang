public abstract class Variavel extends Primitivo {

    public String nome;

    public Variavel(String nome, Tipo tipo, Object valor) {
        super(tipo, valor);
        this.nome = nome;
    }
    
    abstract void setValor(Object valor) throws Exception;

}
