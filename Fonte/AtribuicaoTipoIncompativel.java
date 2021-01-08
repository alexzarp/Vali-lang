public class AtribuicaoTipoIncompativel extends Erro {
    private static final long serialVersionUID = 1L;

    public AtribuicaoTipoIncompativel(String codigoFonte, int indiceAbsoluto) {
        super(codigoFonte, indiceAbsoluto);
    }

    public String getNome() {
        return "atribuição de tipo incompatível";
    }
}
