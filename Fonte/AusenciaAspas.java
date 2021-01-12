public class AusenciaAspas extends Erro {
    
    private static final long serialVersionUID = 1L;

    public AusenciaAspas(String codigoFonte, int indiceAbsoluto) {
        super(codigoFonte, indiceAbsoluto);
    }

    public String getNome() {
        return "ausência de símbolo de aspas (\")";
    }
}
