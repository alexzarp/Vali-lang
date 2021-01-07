public abstract class Erro extends Exception {

    private static final long serialVersionUID = 1L;
    
    protected int linha;
    protected int coluna;
    protected String nome;

    public Erro(int linha, int coluna) {
        super();
        setNome();
    }

    public String getMessage() {
        return nome + " na linha " + linha + ", coluna " + coluna;
    }
    
    public abstract void setNome();
}
