public class VariavelInexistente extends Erro {

    private static final long serialVersionUID = 1L;

    public VariavelInexistente(int linha, int coluna) {
        super(linha, coluna);
    }

    public void setNome() {
        this.nome = "variável inexistente";
    }
}
