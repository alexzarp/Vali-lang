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
    public String input;

    // contadores de caracteres. utilizaremos estes valores para formatar
    // a mensagem de erro em caso de erro, como se fossem cursores.
    private int indiceAbsoluto; // número total de caracteres lidos, incluindo \n.

    private int comprimentoDoPrograma;

    public Parser(String codigoFonte) {
        setInput(codigoFonte);
        // this.indiceLinha = 0;
        // this.indiceColuna = 0;
        // this.indiceAbsoluto = 0;
        this.comprimentoDoPrograma = codigoFonte.length();
    }

    public void analisa() throws Exception {
        Variavel.novoEscopo();
        resolveCorpo();
    }

    // resolve qualquer atribuição, com expressão algébrica e terminada em ;
    private void resolveCorpo() throws Exception {

        // utilizamos expressões regulares para várias verificações ao longo da função.
        Matcher comparador;

        while(indiceAbsoluto <= comprimentoDoPrograma) {

            // regex para algo no formato "palavra palavra(" ou "palavra("
            comparador = Pattern.compile("(\\s*\\w\\s+)?(\\w*\\s\\()").matcher(input);

            // se entrar, é porque encontrou uma chamada ou definição de função (loops
            // inclusive)
            if(comparador.find() && comparador.start(indiceAbsoluto) == 0) {

                // verificaSe();
                // verificaEnquanto();
                // verificaPara();

                // atribuicaoFuncao();

                // ps: lembrar de usar novoEscopo() e removeEscopo() toda vez que chamar uma
                // função

            } else {

                atribuicaoVariavel();
            }
        }

    }

    // TODO implementar isso.
    private void atribuicaoFuncao() throws Erro {

    }

    // resolve atribuições de variáveis, tanto criações quanto alterações.
    // joga um erro caso não encontre um ;.
    private void atribuicaoVariavel() throws Erro {
        Matcher comparador;
        Variavel.imprimeVariaveis();
        
        /*
         * primeiro, precisamos determinar se é uma criação ou atualização de variável.
         * começamos verificando se há uma criação.
         */

        ignoraWhiteSpace();

        // verifica primeiramente a criação de um inteiro.
        // incluímos espaços em branco pois "inteiro1", por exemplo, é um nome aceitável
        // de variável em Vali.
        comparador = Pattern.compile("inteiro\\s").matcher(input);
        if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            indiceAbsoluto += 7;

            ignoraWhiteSpace();

            // usamos regex para formatar a saída.
            // usamos as mesmas regras para nomeção de variáveis que o Java.
            comparador = Pattern.compile("[a-zA-Z]+[_0-9]*").matcher(input);
            
            // se não encontrar nada, é porque o próximo token é inválido.
            if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
                // age como uma forma de "next()" do Scanner.
                String nomeVariavel = comparador.group();
                indiceAbsoluto += nomeVariavel.length();

                comparador = Pattern.compile("\\s*;\\s*").matcher(input);

                // se entrar aqui, é porque a variável não está recebendo um valor (como em
                // "inteiro a;").
                if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {

                    Variavel.setVariavel(new Inteiro(nomeVariavel, null), input, indiceAbsoluto);

                    indiceAbsoluto += comparador.group().length();

                } else {
                    comparador = Pattern.compile("=.+;").matcher(input);
                    // se entrar aqui, é porque o inteiro realmente receberá um valor (como em
                    // "inteiro a = 23;").
                    
                    if (comparador.find(indiceAbsoluto) && comparador.start() == ++indiceAbsoluto) {
                        
                        // criamos uma variável e a salvamos na lista de variáveis.
                        Integer valorInteiro = Integer.valueOf(avaliaExpressaoInteiros(indiceAbsoluto + 1, indiceAbsoluto + comparador.group().length() - 1));
                        Inteiro i = new Inteiro(nomeVariavel, valorInteiro);
                        Variavel.setVariavel(i, input, indiceAbsoluto);

                        indiceAbsoluto += comparador.end();
                    }
                }
            }
        }
    }

    // analisa expressões booleanas.
    // TODO tratar variáveis binario e as keywords "verdadeiro" e "falso".
    public boolean analiseCondicional(int inicio, int fim) throws Erro {
        // // ▼▼▼▼▼ isso aqui tem que ter muito mais tratamento, eu ainda não domino a
        // // sintaxe do regex ▼▼▼▼▼
        // boolean elementar = input.matches("(?i)(\\w{1,}\\W{1,}\\w{1,})");
        // if (elementar) {
        //     // faça a operação
        // } else {
        //     // não é uma assinatura de elementar
        // }
        Matcher comparador = Pattern.compile("\\|").matcher(input);
        if(comparador.find(inicio) && comparador.end() < fim) {
            return analiseCondicional(inicio, comparador.start()) || analiseCondicional(comparador.end(), fim);   
        }

        comparador = Pattern.compile("&").matcher(input);
        if(comparador.find(inicio) && comparador.end() < fim) {
            return analiseCondicional(inicio, comparador.start()) && analiseCondicional(comparador.end(), fim);   
        }

        comparador = Pattern.compile(">|<|==|<=|>=|!=").matcher(input);
        if(comparador.find(inicio) && comparador.end() < fim) {
            String operacao = comparador.group();

            switch (operacao) {
                case "==":
                    return avaliaExpressaoInteiros(inicio, comparador.start()) ==
                            avaliaExpressaoInteiros(comparador.end(), fim);
                case ">":
                    return avaliaExpressaoInteiros(inicio, comparador.start()) >
                            avaliaExpressaoInteiros(comparador.end(), fim);

                case "<": 
                    return avaliaExpressaoInteiros(inicio, comparador.start()) <
                            avaliaExpressaoInteiros(comparador.end(), fim);
                            
                case "<=": 
                    return avaliaExpressaoInteiros(inicio, comparador.start()) <=
                            avaliaExpressaoInteiros(comparador.end(), fim);

                case ">=": 
                    return avaliaExpressaoInteiros(inicio, comparador.start()) >=
                            avaliaExpressaoInteiros(comparador.end(), fim);

                case "!=":
                return avaliaExpressaoInteiros(inicio, comparador.start()) !=
                        avaliaExpressaoInteiros(comparador.end(), fim);
            }
        }

        // como não foi encontrado nenhum valor esperado, apenas dizemos que o token é inválido.
        throw new TokenInesperado(input, indiceAbsoluto);
    }

    // esta função recebe uma expressão aritmética simples e retorna o resultado
    // dela, independente variações de espaçamentos.
    // ex: "21+2*3" retorna 27.
    // início e fim precisam ser índices absolutos.
    // TODO implementar parenteses.
    public int avaliaExpressaoInteiros(int inicio, int fim) throws Erro {

        Matcher comparador;

        ignoraWhiteSpace();

        // procuramos por uma soma ou subtração.
        comparador = Pattern.compile("[\\+-]").matcher(input);
        if (comparador.find(inicio) && comparador.end() <= fim) { // encontrou uma soma ou subtração.
            System.out.println(comparador.group());
            switch (comparador.group()) {
                case "+":
                           // parte esquerda da expressão.
                    return avaliaExpressaoInteiros(inicio, comparador.start())
                            // parte direita.
                            + avaliaExpressaoInteiros(comparador.end(), fim);
                case "-":
                           // parte esquerda da expressão.
                    return avaliaExpressaoInteiros(inicio, comparador.start() - 1)
                            // parte direita.
                            - avaliaExpressaoInteiros(comparador.end(), fim);
            }
        }

        // procuramos por um produto ou divisão (inteira).
        comparador = Pattern.compile("[\\*/]").matcher(input);
        if (comparador.find(inicio) && comparador.end() <= fim) { // encontrou uma multiplicação ou divisão.
            switch (comparador.group()) {
                case "*":
                           // parte esquerda da expressão.
                    return avaliaExpressaoInteiros(inicio, comparador.start())
                            // parte direita.
                            * avaliaExpressaoInteiros(comparador.end(), fim);
                case "-":
                           // parte esquerda da expressão.
                    return avaliaExpressaoInteiros(inicio, comparador.start())
                            // parte direita.
                            / avaliaExpressaoInteiros(comparador.end(), fim);
            }
        }

        /*
         * resolvidas todas as operações, podemos apenas tentar avaliar o resultado que
         * temos e retorná-lo.
         */
        String valor = input.substring(inicio, fim).trim();

        // primeiro verificamos se o resultado é um literal (número) e retorná-lo se for
        // o caso.
        comparador = Pattern.compile("\\d").matcher(input);
        if (comparador.find(indiceAbsoluto))
            return Integer.parseInt(valor);

        /*
         * resta apenas verificar se é uma chamada de função com retorno inteiro ou uma
         * variável do tipo inteiro. por hora apenas tratamos variáveis e não funções.
         */

        comparador = Pattern.compile("\\w").matcher(input); // mesmas regras de nomeação do Java.
        valor = comparador.group();
        Variavel var = Variavel.getVariavel(valor);

        // a variável não existe.
        if (var == null)
            throw new VariavelInexistente(input, indiceAbsoluto);

        // a variável existe, mas não é um inteiro.
        if (var.tipo != Tipos.INTEIRO)
            throw new AtribuicaoTipoIncompativel(input, indiceAbsoluto);

        // assumimos então que a variável existe e possui valor inteiro.
        return Integer.parseInt(var.valor.toString());

    }

    // pula todos os espaços em branco.
    private void ignoraWhiteSpace() {
        char c = input.charAt(indiceAbsoluto);
       while (c == ' ' || c == '\n' || c == '\t') {
            indiceAbsoluto++;
            c = input.charAt(indiceAbsoluto);
        }
    }

    // caso precisemos tratar de alguma forma o código fonte. possivelmente
    // descartável!
    public void setInput(String input) {
        this.input = input;
    }
}