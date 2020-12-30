public final class Palavra extends Variavel {

    public Palavra(String nome, Tipo tipo, Object valor) throws Exception {
        super(nome, tipo, valor);
    }

    // troca o valor se ele for do mesmo tipo que o da variável. se não, joga um erro
    public void setValor(Object valor) throws Exception {                                     
        if(valor.getClass() != String)
            throw Exception; // trocar essa exceção por "tipo incompativel com a variavel na atribuição" ou algo do tipo
        this.valor = valor;
    }

    public Tipos getTipo() {
        return Tipos.PALAVRA;
    }

    public Palavra toInt() {
        // nome = null pois o retorno é um literal (isto é, não é atrelado a alguma variável)
        return new Palavra(null, Tipos.PALAVRA, Integer(Integer.parseInt(valor.toString())));
    }

    public Flutuante toFlutuante() {
        return new Flutuante(null, Tipos.FLUTUANTE, Double(Double.parseDouble(valor.toString())));
    }

}
