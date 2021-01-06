public final class Inteiro extends Variavel {

    public Inteiro(String nome, Object valor) throws Exception {
        super(nome, Tipos.INTEIRO, valor);
    }

    // troca o valor se ele for do mesmo tipo que o da variável. se não, joga um erro
    public void setValor(Object valor) throws Exception {                                     
        if(valor.getClass() != Integer.class)
            throw new Exception(); // trocar essa exceção por "tipo incompativel com a variavel na atribuição" ou algo do tipo
        this.valor = valor;
    }

    public Tipos getTipo() {
        return Tipos.INTEIRO;
    }

    public Palavra toPalavra() {
        // nome = null pois o retorno é um literal (isto é, não é atrelado a alguma variável)
        return new Palavra(null, valor.toString());
    }

    public Flutuante toFlutuante() {
        return new Flutuante(null, Double.valueOf(valor.toString()));
    }

}