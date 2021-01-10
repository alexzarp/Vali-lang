import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public abstract class Variavel extends Primitivo {

    // guarda as variaveis, um escopo por índice
    static private List<Map<String, Variavel>> variaveis = new ArrayList<Map<String, Variavel>>();

    // determina qual é o índice atual da lista de variáveis.
    static private int escopoAtual = -1;

    // nome da variável no código fonte. caso seja null,
    // então é um literal ou índice de um vetor
    public String nome;

    protected Object valor;

    static final String palavrasReservadas[] = {"inteiro", "palavra", "flutuante", "vazio", "binario",
                                                "para", "enquanto", "imprime", "escaneia", "se", "senao",
                                                "verdadeiro", "falso"};

    public Variavel(String nome, Tipos tipo, Object valor) {
        super(tipo, valor);
        this.nome = nome;
        this.valor = valor;
    }
    
    abstract void setValor(Object valor) throws Erro;

    public static Variavel getVariavel(String nome) {
        Variavel v = null;

        for(Map<String, Variavel> variaveisDesteEscopo : variaveis) {
            if(variaveisDesteEscopo.containsKey(nome))
                v = variaveisDesteEscopo.get(nome);

            if(v != null)
                break;
        }
        return v;
    }

    public static void setVariavel(Variavel variavel, String codigoFonte, int indiceAbsoluto) throws Erro {

        // se entrar aqui, é porque uma variável de mesmo nome já existe.
        if(getVariavel(variavel.nome) != null)
            // TODO escolher tipo adequado de erro para jogar.
            throw new RedefinicaoVariavelExistente(codigoFonte, indiceAbsoluto);
        
        // se entrar aqui, é porque a variável é uma palavra reservada.
        if(verificaPalavraReservada(variavel.nome))
            // TODO jogar erro de tipo apropriado.
            throw new PalavraReservada(codigoFonte, indiceAbsoluto);

        variaveis.get(escopoAtual).put(variavel.nome, variavel);

    }

    // esta função precisa ser chamada toda vez que entramos em um novo escopo,
    // a fim de alterar a visibilidade das variáveis que serão criadas.
    public static void novoEscopo() {
        escopoAtual++;
        variaveis.add(new HashMap<String, Variavel>());
    }
    
    // removemos as variáveis que não serão mais acessadas por motivos de visibilidade.
    public static void removeEscopo() {
        escopoAtual--;
        variaveis.remove(escopoAtual);
    }

    // retorna true se a string for palavra reservada.
    // false se não for.
    private static boolean verificaPalavraReservada(String s) {
        for(String palavraReservada : palavrasReservadas) {
            // System.out.println("comparando " + s + " com " + palavraReservada + " e tendo valor " + s.equals(palavraReservada));
            if(s.equals(palavraReservada))
                return true;
        }
        return false;
    }

    // imprime todas as variáveis criadas desde o início do programa.
    public static void imprimeVariaveis() {
        System.out.println("Imprimindo todas as variáveis:");
        for(Map<String, Variavel> variaveisDesteEscopo : variaveis) {
            for(Variavel v : variaveisDesteEscopo.values()) {
                v.imprimeVariavel();
            }
        }
    }

    public void imprimeVariavel() {
        System.out.println("\tvariável " + nome + " do tipo " + tipo + " e de valor " + valor.toString());
    }

}
