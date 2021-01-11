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
    // instancia das classes externas
    Escopo escopo = new Escopo();
    AtribuicaoVariavel atribuicao = new AtribuicaoVariavel();

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
        escopo.resolveCorpo();
    }

    // TODO implementar isso.
    private void verificaAtribuicaoFuncao() throws Erro {
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
                    return avaliaExpressaoDeInteiros(inicio, comparador.start()) ==
                            avaliaExpressaoDeInteiros(comparador.end(), fim);
                case ">":
                    return avaliaExpressaoDeInteiros(inicio, comparador.start()) >
                            avaliaExpressaoDeInteiros(comparador.end(), fim);

                case "<": 
                    return avaliaExpressaoDeInteiros(inicio, comparador.start()) <
                            avaliaExpressaoDeInteiros(comparador.end(), fim);
                            
                case "<=": 
                    return avaliaExpressaoDeInteiros(inicio, comparador.start()) <=
                            avaliaExpressaoDeInteiros(comparador.end(), fim);

                case ">=": 
                    return avaliaExpressaoDeInteiros(inicio, comparador.start()) >=
                            avaliaExpressaoDeInteiros(comparador.end(), fim);

                case "!=":
                return avaliaExpressaoDeInteiros(inicio, comparador.start()) !=
                        avaliaExpressaoDeInteiros(comparador.end(), fim);
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
    public int avaliaExpressaoDeInteiros(int inicio, int fim) throws Erro {

        Matcher comparador;

        ignoraWhiteSpace();

        // procuramos por uma soma ou subtração.
        comparador = Pattern.compile("[\\+-]").matcher(codigoFonte);
        if (comparador.find(inicio) && comparador.end() <= fim) { // encontrou uma soma ou subtração.
            int parteEsquerda, parteDireita;
            switch (comparador.group()) {
                case "+":
                    parteEsquerda = avaliaExpressaoDeInteiros(inicio, comparador.start() - 1);
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeInteiros(comparador.end(), fim);
                    return parteDireita + parteEsquerda;
                case "-":
                    parteEsquerda = avaliaExpressaoDeInteiros(inicio, comparador.start() - 1);
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeInteiros(comparador.end(), fim);
                    return parteDireita - parteEsquerda;
            }
        }

        // procuramos por um produto ou divisão (inteira).
        comparador = Pattern.compile("[\\*/]").matcher(codigoFonte);
        if (comparador.find(inicio) && comparador.end() <= fim) { // encontrou uma multiplicação ou divisão.
            int parteEsquerda, parteDireita;
            switch (comparador.group()) {
                case "*":
                    parteEsquerda = avaliaExpressaoDeInteiros(inicio, comparador.start() - 1);
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeInteiros(comparador.end(), fim);
                    return parteDireita * parteEsquerda;
                case "/":
                    parteEsquerda = avaliaExpressaoDeInteiros(inicio, comparador.start() - 1);
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeInteiros(comparador.end(), fim);
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
        comparador = Pattern.compile("\\s*\\d\\s*").matcher(codigoFonte);
        if(comparador.find(indiceAbsoluto)) {
            int resultado = Integer.parseInt(valor);
            indiceAbsoluto += comparador.group().length();
            return resultado;
        }

        /*
         * resta apenas verificar se é uma chamada de função com retorno inteiro ou uma
         * variável do tipo inteiro. por hora apenas tratamos variáveis e não funções.
         */

        comparador = Pattern.compile("\\s*\\w\\s*").matcher(codigoFonte); // mesmas regras de nomeação do Java.
        comparador.find(indiceAbsoluto);
        valor = comparador.group();
        Variavel var = Variavel.getVariavel(valor.trim());

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

    public double avaliaExpressaoDeFlutuantes(int inicio, int fim) throws Erro {
	
	Matcher comparador;

        ignoraWhiteSpace();

        // procuramos por uma soma ou subtração.
        comparador = Pattern.compile("[\\+-]").matcher(codigoFonte);
        if (comparador.find(inicio) && comparador.end() <= fim) { // encontrou uma soma ou subtração.
            double parteEsquerda, parteDireita;
            switch (comparador.group()) {
                case "+":
                    parteEsquerda = avaliaExpressaoDeFlutuantes(inicio, comparador.start() - 1);
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
                    return parteDireita + parteEsquerda;
                case "-":
                    parteEsquerda = avaliaExpressaoDeFlutuantes(inicio, comparador.start() - 1);
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
                    return parteDireita - parteEsquerda;
            }
        }

        // procuramos por um produto ou divisão (inteira).
        comparador = Pattern.compile("[\\*/]").matcher(codigoFonte);
        if (comparador.find(inicio) && comparador.end() <= fim) { // encontrou uma multiplicação ou divisão.
            double parteEsquerda, parteDireita;
            switch (comparador.group()) {
                case "*":
                    parteEsquerda = avaliaExpressaoDeFlutuantes(inicio, comparador.start() - 1);
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
                    return parteDireita * parteEsquerda;
                case "/":
                    parteEsquerda = avaliaExpressaoDeFlutuantes(inicio, comparador.start() - 1);
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
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
        comparador = Pattern.compile("\\s*\\d\\s*").matcher(codigoFonte);
	if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            double resultado = Double.parseDouble(valor.toString().trim());
            indiceAbsoluto += comparador.group().length();
            return resultado;
        }

        /*
         * resta apenas verificar se é uma chamada de função com retorno flutuante ou uma
         * variável do tipo flutuante. por hora apenas tratamos variáveis e não funções.
         */

        comparador = Pattern.compile("\\s*\\w\\s*").matcher(codigoFonte); // mesmas regras de nomeação do Java.
        comparador.find(indiceAbsoluto);
        valor = comparador.group().trim();
        Variavel var = Variavel.getVariavel(valor);

        // a variável não existe.
        if (var == null)
	    throw new VariavelInexistente(codigoFonte, indiceAbsoluto);

        // a variável existe, mas não é um inteiro.
        if (var.tipo != Tipos.FLUTUANTE && var.tipo != Tipos.INTEIRO)
            throw new AtribuicaoTipoIncompativel(codigoFonte, indiceAbsoluto);
            
        // assumimos então que a variável existe e possui valor inteiro.
        indiceAbsoluto += comparador.group().length();

        return Double.parseDouble(var.valor.toString());

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
