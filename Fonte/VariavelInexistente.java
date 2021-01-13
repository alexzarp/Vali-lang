package Fonte;
public class VariavelInexistente extends Erro {

    private static final long serialVersionUID = 1L;

    public VariavelInexistente(String codigoFonte, int indiceAbsoluto) {
        super(codigoFonte, indiceAbsoluto);
    }

    public String getNome() {
        return "variável inexistente";
    }
}
