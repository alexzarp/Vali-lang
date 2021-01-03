import java.util.regex.Pattern;
import java.util.regex.Matcher;
/*  utilizaremos expressões regulares (regular expressions, ou regex) para facilitar
    a verificação de palavras chave e formatações no código fonte. para isso utilizaremos
    a classe Pattern, que compila um dado regex, e a Matcher, que funciona como um Scanner para
    um dado Pattern. o Scanner também funciona com Patterns, mas eu não consegui fazer com o
    Scanner tudo o que consegui fazer com o Matcher. utilizaremos o regex para: a) verificar
    a formatação de variáveis e de números, isto é, se detectarmos uma sequencia de caracteres
    possivelmente seguidos de números e underscore (_), então trataremos como uma variável, mas se
    por outro lado detectarmos uma sequência de caracteres encapsulada em aspas, trataremos como 
    uma string literal; b) para detectar palavras reservadas, como "inteiro", "enquanto", etc.;
    e c) para ignorar potenciais espaços vazios sem necessidade de especificar quais ou quantos
    nas duas coisas anteriores.
    para isso utilizaremos de alguns metacaracteres:
    - "\\w" se traduz para qualquer sequência de letras seguidos de números ou _.
        exs: "a", "ab2", "exemplo_23"
    - "\\s" significa qualquer white space, como "\n", "\t", " "
    - "+" significa que o valor anterior deve aparecer uma ou mais vezes.
        ex: para "a+b", "aaab" é válido mas "ab" não
    - "*" significa que o valor anterior deve aparecer zero ou mais vezes.
        ex: para "\\s*\\w\\s*", "  abc " é valido e "abc" também 
    - "a{n,m}" significa "a" de n à m ocorrências seguidas */
        
public class Parser {
    
    // input guarda todo o código fonte em uma única string.
    private String input;

    // contadores de caracteres. utilizaremos estes valores para formatar
    // a mensagem de erro em caso de erro, como se fossem cursores.
    private int indiceLinha;
    private int indiceColuna;
    private int indiceAbsoluto; // número total de caracteres lidos, incluindo \n.

    private int comprimentoDoPrograma;
    
    public Parser(String codigoFonte) {
        setInput(codigoFonte);
        this.indiceLinha = 0;
        this.indiceColuna = 0;
        this.indiceAbsoluto = 0;
        this.comprimentoDoPrograma = codigoFonte.length();
    }

    public void analisa() throws Exception {
        resolveCorpo();
    }

    // resolve qualquer atribuição, com expressão algébrica e terminada em ;
    private void resolveCorpo() throws Exception {

        // utilizamos expressões regulares para várias verificações ao longo da função.
        Pattern padrao;
        Matcher comparador;

        while(indiceAbsoluto <= comprimentoDoPrograma) {
            // regex para algo no formato "palavra palavra(" ou  "palavra("
            comparador = Pattern.compile("(\\s*\\w\\s+){0,1}(\\w*\\s\\()").matcher(input);
            
            // se entrar, é porque encontrou uma chamada ou definição de função (loops inclusive)
            if(comparador.find() && comparador.start(indiceAbsoluto) == 0) {
                // TODO implementar tratamento de função.
                // ps: lembrar de usar novoEscopo() e removeEscopo() toda vez que chamar uma função

            } else {

                /* primeiro, precisamos determinar se é
                uma criação ou atualização de variável. */

                ignoraWhiteSpace();

                // usamos regex para verificar a criação de um inteiro.
                // incluímos espaços em branco pois "inteiro1", por exemplo, é um nome aceitável de variável em Vali.
                comparador = Pattern.compile("inteiro\\s").matcher(input);
                if(comparador.matches() && comparador.find(indiceAbsoluto)) {

                    // não incluímos diretamente o white space nos contadores pois não sabemos qual foi utilizado.
                    indiceColuna += 7;
                    indiceAbsoluto += 7;

                    ignoraWhiteSpace();

                    // usamos regex para formatar a saída.
                    padrao = Pattern.compile("\\w\\s*"); // usamos as mesmas regras para nomeção de variáveis que o Java.
                    comparador = padrao.matcher(input);

                    // comparamos o índice da próxima incidência de um nome apropriado com o índice atual do programa. se coincidem,
                    // então a próxima palavra é uma variável de nome aceitável. seria o equivalente de um hasNext() do Scanner, mas
                    // especificamente para um nome.
                    if(comparador.matches() && comparador.start(indiceAbsoluto) == 0) {

                        padrao = Pattern.compile("\\w");
                        comparador = padrao.matcher(input);

                        // age como uma forma de "next()" do Scanner.
                        String nomeVariavel = comparador.group();
                        int comprimentoDoNome = nomeVariavel.length();
                        indiceAbsoluto += comprimentoDoNome;
                        indiceColuna += comprimentoDoNome;
                        
                        padrao = Pattern.compile("\\s*;\\s*");
                        comparador = padrao.matcher(input);

                        // se entrar aqui, é porque a variável não está recebendo um valor.
                        if(comparador.matches() && comparador.start(indiceAbsoluto) == 0) {
                            
                            Variavel.setVariavel(new Inteiro(nomeVariavel, null));

                            ignoraWhiteSpace();
                            // comprimento de ";" é 1
                            indiceAbsoluto++;
                            indiceColuna++;
                            ignoraWhiteSpace();
                        }


                    } else // se entrou aqui é porque não é uma atribuição ou chamada de função, então encontrou um caractere inesperado.
                        throw new Exception(); // TODO escolher tipo apropriado de erro para jogar
                }
            }

            
        }

    }

    // pula todos os espaços em branco e trata os contadores
    // conforme qual espaço em branco foi utilizado
    public void ignoraWhiteSpace() {
        char c = input.charAt(indiceAbsoluto);
        
        while(c == ' ' || c == '\n' || c == '\t') {
            if(c == ' ' || c == '\t')  {
                indiceColuna++;
                indiceAbsoluto++;
            } else if(c == '\n') { 
                indiceAbsoluto += indiceColuna;
                indiceColuna = 0;
                indiceLinha++;
            }
            c = input.charAt(indiceAbsoluto);
        }
    }

    // caso precisemos tratar de alguma forma o código fonte. possivelmente descartável!
    public void setInput(String input) {
        this.input = input;
    }
}