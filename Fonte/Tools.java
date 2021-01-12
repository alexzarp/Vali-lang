public class Tools {
    // pula todos os espaços em branco.
    public void ignoraWhiteSpace() {
        char c = Parser.getCodigoFonte().charAt(Parser.getIndiceAbsoluto());
        while (Parser.getIndiceAbsoluto() < Parser.getComprimentoDoPrograma() && (c == ' ' || c == '\n' || c == '\t')) {
            Parser.sumIndiceAbsoluto(1);
            c = Parser.getCodigoFonte().charAt(Parser.getIndiceAbsoluto());
        }
    }


}
