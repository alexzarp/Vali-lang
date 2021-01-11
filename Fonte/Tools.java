public class Tools {
    // pula todos os espaços em branco.
    public void ignoraWhiteSpace() {
        char c = Vali.codigoFonte.charAt(Parser.indiceAbsoluto);
       while (Parser.indiceAbsoluto < Parser.comprimentoDoPrograma && (c == ' ' || c == '\n' || c == '\t')) {
            Parser.indiceAbsoluto++;
            c = Vali.codigoFonte.charAt(Parser.indiceAbsoluto);
        }
    }
}
