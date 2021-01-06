public class FaltaParenteses extends Erro {
    
    private static final long serialVersionUID = 1L;

    public FaltaParenteses(int linha, int coluna) {
        super(linha, coluna);
    }

    public void setNome() {
        this.nome = "número inadequado de parênteses";
    }
}
