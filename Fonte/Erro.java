public abstract class Erro extends Exception {

    private static final long serialVersionUID = 1L;

    protected int linha;
    protected int coluna; 
    protected int indiceInicioLinha;
    protected int indiceAbsoluto;
    protected String codigoFonte;

    public Erro(String codigoFonte, int indiceAbsoluto) {
        super();
        this.codigoFonte = codigoFonte;
        this.indiceAbsoluto = indiceAbsoluto;
        setLinhaEColuna();
    }

    // percorre pelo código fonte até o índice absoluto e seta
    // os atributos linha e coluna conforme adequado para posterior
    // impressão formatada da linha do código em caso de erro.
    protected void setLinhaEColuna() {
        this.indiceInicioLinha = 0;
        this.linha = 0;
        this.coluna = 0;
        int indiceAtual = 0;
        while(indiceAtual <= indiceAbsoluto) {
            // System.out.println("indiceAtual " + indiceAtual);
            switch(codigoFonte.charAt(indiceAtual)) { 
                case '\n':
                    linha++;
                    indiceInicioLinha = indiceAtual + 1;
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
        int indiceAtual = indiceInicioLinha;
        int fimCodigoFonte = codigoFonte.length();

        // printa a linha em si.
        while(indiceAtual < fimCodigoFonte && codigoFonte.charAt(indiceAtual) != '\n') {
            System.out.print(codigoFonte.charAt(indiceAtual));

            indiceAtual++;
        }

        System.out.println();
        // printa o offset do cursor.
        for(int i = 0; i < (indiceAbsoluto - indiceInicioLinha); i++)
            System.out.print(" ");
        
        // printa o cursor.
        System.out.println("^");
    }

    public String toString() {
        return "Erro: [" + getNome() + "] no caractere \"" + codigoFonte.charAt(indiceAbsoluto) + "\" da linha " + linha + ", coluna " + coluna + " (" + (indiceAbsoluto + 1) + "o caractere do codigo fonte, \\n inclusos e contando a partir do 1).";
    }
    
    protected abstract String getNome();
}
