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
        // this.indiceLinha = 0;
        // this.indiceColuna = 0;
        // this.indiceAbsoluto = 0;
        // this.comprimentoDoPrograma = codigoFonte.length();
    }

    public void analisa() throws Exception {
        resolveCorpo();
    }

    // resolve qualquer atribuição, com expressão algébrica e terminada em ;
    private void resolveCorpo() throws Exception {

        // utilizamos expressões regulares para várias verificações ao longo da função.
        Matcher comparador;

        while (indiceAbsoluto <= comprimentoDoPrograma) {

            // regex para algo no formato "palavra palavra(" ou "palavra("
            comparador = Pattern.compile("(\\s*\\w\\s+)?(\\w*\\s\\()").matcher(input);

            // se entrar, é porque encontrou uma chamada ou definição de função (loops
            // inclusive)
            if (comparador.find() && comparador.start(indiceAbsoluto) == 0) {

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
    private void atribuicaoFuncao() throws Exception {

    }

    private void atribuicaoVariavel() throws Exception {
        Pattern padrao;
        Matcher comparador;

        /*
         * primeiro, precisamos determinar se é uma criação ou atualização de variável.
         */

        ignoraWhiteSpace();

        // usamos regex para verificar a criação de um inteiro.
        // incluímos espaços em branco pois "inteiro1", por exemplo, é um nome aceitável
        // de variável em Vali.
        comparador = Pattern.compile("inteiro\\s").matcher(input);
        if (comparador.matches() && comparador.find(indiceAbsoluto)) {

            // não incluímos diretamente o white space nos contadores pois não sabemos qual
            // foi utilizado.
            indiceColuna += 7;
            indiceAbsoluto += 7;

            ignoraWhiteSpace();

            // usamos regex para formatar a saída.
            padrao = Pattern.compile("\\w\\s*"); // usamos as mesmas regras para nomeção de variáveis que o Java.
            comparador = padrao.matcher(input);

            // comparamos o índice da próxima incidência de um nome apropriado com o índice
            // atual do programa. se coincidem,
            // então a próxima palavra é uma variável de nome aceitável. seria o equivalente
            // de um hasNext() do Scanner, mas
            // especificamente para um nome.
            if (comparador.matches() && comparador.start(indiceAbsoluto) == 0) {

                padrao = Pattern.compile("\\w");
                comparador = padrao.matcher(input);

                // age como uma forma de "next()" do Scanner.
                String nomeVariavel = comparador.group();
                int comprimentoDoNome = nomeVariavel.length();
                indiceAbsoluto += comprimentoDoNome;
                indiceColuna += comprimentoDoNome;

                padrao = Pattern.compile("\\s*;\\s*");
                comparador = padrao.matcher(input);

                // se entrar aqui, é porque a variável não está recebendo um valor (como em
                // "inteiro a;").
                if (comparador.matches() && comparador.start(indiceAbsoluto) == 0) {

                    Variavel.setVariavel(new Inteiro(nomeVariavel, null));

                    ignoraWhiteSpace();
                    // comprimento de ";" é 1
                    indiceAbsoluto++;
                    indiceColuna++;
                    ignoraWhiteSpace();
                } else {
                    padrao = Pattern.compile("\\s*=\\s*");
                    comparador = padrao.matcher(input);

                    // se entrar aqui, é porque o inteiro realmente receberá um valor (como em
                    // "inteiro a = alguma coisa;")
                    if (comparador.matches() && comparador.start(indiceAbsoluto) == 0) {
                        // Variavel.setVariavel(new Inteiro(nomeVariavel,
                        // Integer.valueOf(avaliaExpressaoInteiros())));
                    }
                }
            }
        }
    }

    // analisa expressões contidas entre parênteses "(a < b)", usado em if e while
<<<<<<< HEAD
    public boolean analiseElementar() throws Exception {
        // // ▼▼▼▼▼ isso aqui tem que ter muito mais tratamento, eu ainda não domino a
        // // sintaxe do regex ▼▼▼▼▼
        // boolean elementar = input.matches("(?i)(\\w{1,}\\W{1,}\\w{1,})");
        // if (elementar) {
        //     // faça a operação
        // } else {
        //     // não é uma assinatura de elementar
        // }

        Matcher comparador = Pattern.compile(">|<|==|<=|>=|\\||&|!=").matcher(input);
        if(comparador.find()) {
            String ladoEsquerdo = input.substring(0, comparador.start());
            String ladoDireito = input.substring(comparador.end(), input.length());
            System.out.println("comparando " + ladoEsquerdo + " e " + ladoDireito);
            String operacao = comparador.group();
            switch(operacao) {
                case "==":
                    return avaliaExpressaoInteiros(0, comparador.start()) ==
                           avaliaExpressaoInteiros(comparador.end(), input.length());
                case ">":
                    return avaliaExpressaoInteiros(0, comparador.start()) >
                           avaliaExpressaoInteiros(comparador.end(), input.length());
                
            }
=======
    public void analiseElementar() throws Exception {
        // ▼▼▼▼▼ isso aqui tem que ter muito mais tratamento, eu ainda não domino a
        // sintaxe do regex ▼▼▼▼▼

        // Matcher m = p.matcher("trabalho==0"); ←EXEMPLO
        Pattern p = Pattern.compile("(\\w>|<|==|<=|>=|\\||&|!=\\w)");
        Matcher condicao = p.matcher(/* Aqui deve haver algum índice, pois isso ainda não faz sentido */input);
        // comparador.group();
        // comparador.start();
        if (condicao.find() /* Quero apenas extratir um boolean aqui, não tenho certeza se é assim */) {

            if (condicao.find() == '<') {

            }

        } else {
            // não é um condicional
>>>>>>> e86f31f5ff5d5da5254ee529c83e1ec9b298ce9b
        }
        
        throw new Exception();
    }
    /*
     * Matcher comparador =
     * Pattern.compile("[={2}<=?>=?={2}\\|{2}&{2}(!=)]").matcher();
     * comparador.group() comparador.start()
     */

    // pula todos os espaços em branco e trata os contadores
    // conforme qual espaço em branco foi utilizado
    private void ignoraWhiteSpace() {
        char c = input.charAt(indiceAbsoluto);

        while (c == ' ' || c == '\n' || c == '\t') {
            if (c == ' ' || c == '\t') {
                indiceColuna++;
                indiceAbsoluto++;
            } else if (c == '\n') {
                indiceAbsoluto++;
                indiceColuna = 0;
                indiceLinha++;
            }
            c = input.charAt(indiceAbsoluto);
        }
    }

    // esta função recebe uma expressão aritmética simples e retorna o resultado
    // dela, independente variações de espaçamentos.
    // ex: "21+2*3" retorna 27.
    // início e fim precisam ser índices absolutos.
    // TODO implementar parenteses.
    public int avaliaExpressaoInteiros(int inicio, int fim) throws Exception {

        System.out.println("avaliando " + input.substring(inicio, fim) + " de comprimento "
                + input.substring(inicio, fim).length());
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
                    return avaliaExpressaoInteiros(inicio, comparador.start() - 1)
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
            throw new VariavelInexistente(indiceLinha, indiceColuna);

        // a variável existe, mas não é um inteiro.
        if (var.tipo != Tipos.INTEIRO)
            throw new Exception();

        // assumimos então que a variável existe e possui valor inteiro.
        return Integer.parseInt(var.valor.toString());

    }

    // caso precisemos tratar de alguma forma o código fonte. possivelmente
    // descartável!
    public void setInput(String input) {
        this.input = input;
    }
}