public class AusenciaPontoEVirgula extends Erro {
    
    private static final long serialVersionUID = 1L;

    public AusenciaPontoEVirgula(String codigoFonte, int indiceAbsoluto) {
        super(codigoFonte, indiceAbsoluto);
    }

    public String getNome() {
        return "ausência de símbolo de aspas (\")";
    }
}
