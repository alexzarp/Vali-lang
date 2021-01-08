public abstract class Erro extends Exception {

    private static final long serialVersionUID = 1L;

    protected int linha;
    protected int coluna;
    protected int indiceFormatacao;
    protected String codigoFonte;

    public Erro(String codigoFonte, int indiceAbsoluto) {
        super();
        this.codigoFonte = codigoFonte;
        setLinhaEColuna(indiceAbsoluto);
    }

    // percorre pelo código fonte até o índice absoluto e seta
    // os atributos linha e coluna conforme adequado para posterior
    // impressão formatada da linha do código em caso de erro.
    protected void setLinhaEColuna(int indiceAbsoluto) {
        this.indiceFormatacao = 0;
        this.linha = 0;
        this.coluna = 0;
        int indiceAtual = 0;
        while(indiceAtual <= indiceAbsoluto) {
            System.out.println("feee");
            switch(codigoFonte.charAt(indiceAtual)) { 
                case '\n':
                    linha++;
                    indiceFormatacao = indiceAtual + 1;
                    coluna = 0;
                    break;
                default:
                    coluna++;
                    break;
            }
            indiceAtual++;
        }
    }

    // imprime a linha de erro, junto a um indicador visual da coluna
    // na qual o erro foi discriminado.
    public void printaLinhaFormatada() {
        int fimLinha = indiceFormatacao;
        int fimCodigoFonte = codigoFonte.length();

        // printa a linha em si.
        while(fimLinha < fimCodigoFonte && codigoFonte.charAt(fimLinha) != '\n') {
            System.out.print(codigoFonte.charAt(fimLinha));

            fimLinha++;
        }

        System.out.println();

        // printa o offset do cursor.
        for(int i = 0; i < (fimLinha - indiceFormatacao - 1); i++)
            System.out.print(" ");
        
        // printa o cursor.
        System.out.println("^");
    }

    public String toString() {
        return getNome() + " na linha " + linha + ", coluna " + coluna;
    }
    
    protected abstract String getNome();
}
