public class AusenciaParenteses extends Erro {
    
    private static final long serialVersionUID = 1L;

    public AusenciaParenteses(String codigoFonte, int indiceAbsoluto) {
        super(codigoFonte, indiceAbsoluto);
    }

    public String getNome() {
        return "ausência de símbolo de aspas (\")";
    }
}
