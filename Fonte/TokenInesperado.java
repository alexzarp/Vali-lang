package Fonte;
public class TokenInesperado extends Erro {
    private static final long serialVersionUID = 1L;

    public TokenInesperado(String codigoFonte, int indiceAbsoluto) {
        super(codigoFonte, indiceAbsoluto);
    }

    public String getNome() {
        return "token inesperado";
    }

}
