public final class Binario extends Variavel {

    public Binario(String nome, Object valor) throws Erro {
        super(nome, Tipos.BINARIO, valor);
    }

    // troca o valor se ele for do mesmo tipo que o da variável. se não, joga um
    // erro
    public void setValor(Object valor) throws Erro {
        this.valor = valor;
    }

    public Tipos getTipo() {
        return Tipos.BINARIO;
    }
}
