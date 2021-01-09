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

    // codigoFonte guarda todo o código fonte em uma única string.
    public String codigoFonte;

    // contadores de caracteres. utilizaremos estes valores para formatar
    // a mensagem de erro em caso de erro, como se fossem cursores.
    private int indiceAbsoluto; // número total de caracteres lidos, incluindo \n.

    private int comprimentoDoPrograma;

    public Parser(String codigoFonte) {
        setCodigoFonte(codigoFonte);
        this.indiceAbsoluto = 0;
        this.comprimentoDoPrograma = codigoFonte.length();
    }

    public void analisa() throws Erro {
        Variavel.novoEscopo();
        resolveCorpo();
    }

    // resolve qualquer atribuição, com expressão algébrica e terminada em ;
    private void resolveCorpo() throws Erro {

        // utilizamos expressões regulares para várias verificações ao longo da função.
        Matcher comparador;
        ignoraWhiteSpace();

        // enquanto ainda há linhas do código fonte a serem avaliadas, verificamos se a próxima linha
        // se encaixa em algum dos contextos esperados (chamadas de funções, atribuições de funções,
        // loops, condicionais e atribuições de variáveis). se a linha não se encaixar em nenhum contexto,
        // jogamos um erro de token inesperado.
        while(indiceAbsoluto < comprimentoDoPrograma - 1) {
        ignoraWhiteSpace();
            // regex para algo no formato "palavra palavra(" ou "palavra("
            comparador = Pattern.compile("(\\s*\\w\\s+)?(\\w*\\s\\()").matcher(codigoFonte);

            // se entrar, é porque encontrou uma chamada ou definição de função (loops
            // inclusive)
            if(comparador.find() && comparador.start(indiceAbsoluto) == 0) {

                // verificaSe();
                // verificaEnquanto();
                // verificaPara();
                // verificaImprime();

                // verificaAtribuicaoFuncao();

                // ps: lembrar de usar novoEscopo() e removeEscopo() toda vez que chamar uma
                // função, seja um loop, if, ou função declarada no código.

            } else {

                // como não é nenhuma função, resta apenas testar se há uma atribuição.
                if(!verificaAtribuicaoVariavel()) {
                    // como a linha não se adequa a nenhum dos contextos possíveis, apenas dizemos que o token é inesperado.
                    throw new TokenInesperado(codigoFonte, indiceAbsoluto);
                }
                    
            }
        }
        System.out.println("execução com sucesso do programa.");
    }

    // TODO implementar isso.
    private void verificaAtribuicaoFuncao() throws Erro {
    }

    // resolve atribuições de variáveis, tanto criações quanto alterações.
    // joga um erro caso não encontre um ;.
    private boolean verificaAtribuicaoVariavel() throws Erro {
        Matcher comparador;
        // Variavel.imprimeVariaveis();
        
        /*
         * primeiro, precisamos determinar se é uma criação ou atualização de variável.
         * começamos verificando se há uma criação.
         */


        // verifica primeiramente a criação de um inteiro.
        // incluímos espaços em branco pois "inteiro1", por exemplo, é um nome aceitável
        // de variável em Vali.
        
        comparador = Pattern.compile("inteiro\\s").matcher(codigoFonte);
        if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            indiceAbsoluto += 7;

            ignoraWhiteSpace();

            // usamos regex para formatar a saída.
            // usamos as mesmas regras para nomeção de variáveis que o Java.
            comparador = Pattern.compile("[a-zA-Z]+[_0-9]*").matcher(codigoFonte);
            
            // se não encontrar nada, é porque o próximo token é inválido.
            if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
                // age como uma forma de "next()" do Scanner.
                String nomeVariavel = comparador.group();
                indiceAbsoluto += nomeVariavel.length();

                comparador = Pattern.compile("\\s*;\\s*").matcher(codigoFonte);

                // se entrar aqui, é porque a variável não está recebendo um valor (como em
                // "inteiro a;").
                if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {

                    Variavel.setVariavel(new Inteiro(nomeVariavel, null), codigoFonte, indiceAbsoluto);

                    indiceAbsoluto += comparador.group().length();
                    return true;

                } else {
                    ignoraWhiteSpace();
                    comparador = Pattern.compile("=[^;]+;").matcher(codigoFonte);
                    // se entrar aqui, é porque o inteiro realmente receberá um valor (como em
                    // "inteiro a = 23;").
                    if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
                        indiceAbsoluto++; // considerando "="

                        // criamos uma variável e a salvamos na lista de variáveis.
                        Integer valorInteiro = Integer.valueOf(avaliaExpressaoInteiros(indiceAbsoluto, comparador.end() - 2));
                        Inteiro i = new Inteiro(nomeVariavel, valorInteiro);
                        Variavel.setVariavel(i, codigoFonte, indiceAbsoluto);
                        ignoraWhiteSpace();
                        indiceAbsoluto++; // considerando ";"
                        return true;
                    }
                }
            }
        } else {
            // fazer aqui tratamento de outros tipos.
            comparador = Pattern.compile("outros tipos").matcher(codigoFonte);
            if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
                
            }
            /*
                depois de todos os ifs
             */
            
            // se entrou aqui, é porque não encontrou atribuição de nenhum formato adequado.
            
        }
        
        return false;
    }

    // analisa expressões booleanas.
    // TODO tratar variáveis binario, operador "!" e as keywords "verdadeiro" e "falso".
    public boolean analiseCondicional(int inicio, int fim) throws Erro {

        Matcher comparador = Pattern.compile("\\|").matcher(codigoFonte);
        if(comparador.find(inicio) && comparador.end() < fim) {
            return analiseCondicional(inicio, comparador.start()) || analiseCondicional(comparador.end(), fim);   
        }

        comparador = Pattern.compile("&").matcher(codigoFonte);
        if(comparador.find(inicio) && comparador.end() < fim) {
            return analiseCondicional(inicio, comparador.start()) && analiseCondicional(comparador.end(), fim);   
        }

        comparador = Pattern.compile(">|<|==|<=|>=|!=").matcher(codigoFonte);
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
        throw new TokenInesperado(codigoFonte, indiceAbsoluto);
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
        comparador = Pattern.compile("[\\+-]").matcher(codigoFonte);
        if (comparador.find(inicio) && comparador.end() <= fim) { // encontrou uma soma ou subtração.
            int parteEsquerda, parteDireita;
            switch (comparador.group()) {
                case "+":
                    parteEsquerda = avaliaExpressaoInteiros(inicio, comparador.start());
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoInteiros(comparador.end(), fim);
                    return parteDireita + parteEsquerda;
                case "-":
                    parteEsquerda = avaliaExpressaoInteiros(inicio, comparador.start());
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoInteiros(comparador.end(), fim);
                    return parteDireita - parteEsquerda;
            }
        }

        // procuramos por um produto ou divisão (inteira).
        comparador = Pattern.compile("[\\*/]").matcher(codigoFonte);
        if (comparador.find(inicio) && comparador.end() <= fim) { // encontrou uma multiplicação ou divisão.
            int parteEsquerda, parteDireita;
            switch (comparador.group()) {
                case "*":
                    parteEsquerda = avaliaExpressaoInteiros(inicio, comparador.start());
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoInteiros(comparador.end(), fim);
                    return parteDireita * parteEsquerda;
                case "/":
                    parteEsquerda = avaliaExpressaoInteiros(inicio, comparador.start());
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoInteiros(comparador.end(), fim);
                    return parteDireita / parteEsquerda;
            }
        }

        /*
         * resolvidas todas as operações, podemos apenas tentar avaliar o resultado que
         * temos e retorná-lo.
         */
        String valor = codigoFonte.substring(inicio, fim + 1).trim();

        // primeiro verificamos se o resultado é um literal (número) e retorná-lo se for
        // o caso.
        comparador = Pattern.compile("\\d").matcher(codigoFonte);
        if(comparador.find(indiceAbsoluto)) {
            int resultado = Integer.parseInt(valor);
            indiceAbsoluto += comparador.group().length();
            return resultado;
        }

        /*
         * resta apenas verificar se é uma chamada de função com retorno inteiro ou uma
         * variável do tipo inteiro. por hora apenas tratamos variáveis e não funções.
         */

        comparador = Pattern.compile("\\w").matcher(codigoFonte); // mesmas regras de nomeação do Java.
        valor = comparador.group();
        Variavel var = Variavel.getVariavel(valor);

        // a variável não existe.
        if (var == null)
            throw new VariavelInexistente(codigoFonte, indiceAbsoluto);

        // a variável existe, mas não é um inteiro.
        if (var.tipo != Tipos.INTEIRO)
            throw new AtribuicaoTipoIncompativel(codigoFonte, indiceAbsoluto);

        // assumimos então que a variável existe e possui valor inteiro.
        indiceAbsoluto += comparador.group().length();
        return Integer.parseInt(var.valor.toString());

    }

    // pula todos os espaços em branco.
    private void ignoraWhiteSpace() {
        char c = codigoFonte.charAt(indiceAbsoluto);
       while (indiceAbsoluto < comprimentoDoPrograma && (c == ' ' || c == '\n' || c == '\t')) {
            indiceAbsoluto++;
            c = codigoFonte.charAt(indiceAbsoluto);
        }
    }

    // caso precisemos tratar de alguma forma o código fonte. possivelmente
    // descartável!
    public void setCodigoFonte(String codigoFonte) {
        this.codigoFonte = codigoFonte;
    }
}