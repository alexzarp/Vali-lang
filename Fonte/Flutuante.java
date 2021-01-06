public final class Flutuante extends Variavel {

    public Flutuante(String nome, Object valor) throws Exception {
        super(nome, Tipos.FLUTUANTE, valor);
    }

    // troca o valor se ele for do mesmo tipo que o da variável. se não, joga um erro
    public void setValor(Object valor) throws Exception {                                     
        if(valor.getClass() != Flutuante.class)
            throw new Exception(); // trocar essa exceção por "tipo incompativel com a variavel na atribuição" ou algo do tipo
        this.valor = valor;
    }

    public Tipos getTipo() {
        return Tipos.FLUTUANTE;
    }

    public Palavra toPalavra() throws Exception {
        // nome = null pois o retorno é um literal (isto é, não é atrelado a alguma variável)
        return new Palavra(null, valor.toString());
    }

    public Inteiro toInteiro() throws Exception {
        return new Inteiro(null, Integer.valueOf(valor.toString()));
    }

}
