public class NumeroInadequadoParenteses extends Erro {
    
    private static final long serialVersionUID = 1L;

    public NumeroInadequadoParenteses(String codigoFonte, int indiceAbsoluto) {
        super(codigoFonte, indiceAbsoluto);
    }

    public String getNome() {
        return "número inadequado de parênteses";
    }
}
