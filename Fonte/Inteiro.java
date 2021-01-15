public final class Inteiro extends Variavel {

    public Inteiro(String nome, Integer valor) throws Erro {
        super(nome, Tipos.INTEIRO, valor);
    }

    // troca o valor se ele for do mesmo tipo que o da variável. se não, joga um erro
    public void setValor(Object valor)  {                                     
        // if(valor.getClass() != Integer.class)
        //     throw new AtribuicaoTipoIncompativel(codigoFonte, indiceAbsoluto); // trocar essa exceção por "tipo incompativel com a variavel na atribuição" ou algo do tipo
        this.valor = valor;
    }

    public Tipos getTipo() {
        return Tipos.INTEIRO;
    }
}
