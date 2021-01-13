package Fonte;
public class PalavraReservada extends Erro {
    private static final long serialVersionUID = 1L;

    public PalavraReservada(String codigoFonte, int indiceAbsoluto) {
        super(codigoFonte, indiceAbsoluto);
    }

    public String getNome() {
        return "atribuição com uso de palavra reservada";
    }

    public String mensagemExtra() {
        return "Para saber mais sobre palavras reservadas, leia o Readme.md";
    }
}
