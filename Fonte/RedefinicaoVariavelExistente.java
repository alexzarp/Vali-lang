public class RedefinicaoVariavelExistente extends Erro {
    private static final long serialVersionUID = 1L;

    public RedefinicaoVariavelExistente(String codigoFonte, int indiceAbsoluto) {
        super(codigoFonte, indiceAbsoluto);
    }

    public String getNome() {
        return "redefinição de variável pré-existente";
    }

}
