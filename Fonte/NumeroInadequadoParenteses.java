public class NumeroInadequadoParenteses extends Erro {
    
    private static final long serialVersionUID = 1L;

    public NumeroInadequadoParenteses(int linha, int coluna) {
        super(linha, coluna);
    }

    public void setNome() {
        this.nome = "número inadequado de parênteses";
    }
}
