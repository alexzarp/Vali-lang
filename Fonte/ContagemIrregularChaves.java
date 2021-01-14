public class ContagemIrregularChaves extends Erro {
    
    private static final long serialVersionUID = 1L;

    public ContagemIrregularChaves(String codigoFonte, int indiceAbsoluto) {
        super(codigoFonte, indiceAbsoluto);
    }

    public String getNome() {
        return "número irregular de chaves ({, })";
    }
}
