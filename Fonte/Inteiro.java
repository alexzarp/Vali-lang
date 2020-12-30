public final class Inteiro extends Variavel {

    public Inteiro(String nome, Tipo tipo, Object valor) throws Exception {
        super(nome, tipo, valor);
    }

    // troca o valor se ele for do mesmo tipo que o da variável. se não, joga um erro
    public void setValor(Object valor) throws Exception {                                     
        if(valor.getClass() != Integer)
            throw Exception; // trocar essa exceção por "tipo incompativel com a variavel na atribuição" ou algo do tipo
        this.valor = valor;
    }

    public Tipos getTipo() {
        return Tipos.INTEIRO;
    }

    public Palavra toPalavra() {
        // nome = null pois o retorno é um literal (isto é, não é atrelado a alguma variável)
        return new Palavra(null, Tipos.PALAVRA, valor.toString());
    }

    public Flutuante toFlutuante() {
        return new Flutuante(null, Tipos.FLUTUANTE, Double(valor.floatValue()));
    }

}
