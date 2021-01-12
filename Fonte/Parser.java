import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
    public void resolveCorpo() throws Erro {

        // utilizamos expressões regulares para várias verificações ao longo da função.
        Matcher comparador;
        ignoraWhiteSpace();

        // enquanto ainda há linhas do código fonte a serem avaliadas, verificamos se a próxima linha
        // se encaixa em algum dos contextos esperados (chamadas de funções, atribuições de funções,
        // loops, condicionais e atribuições de variáveis). se a linha não se encaixar em nenhum contexto,
        // jogamos um erro de token inesperado.
        while(indiceAbsoluto < comprimentoDoPrograma - 2) { // -1 por causa do índice 0 e -1 por causa do \n extra que adicionamos no Vali.java
            ignoraWhiteSpace();
        
            // regex para algo no formato "palavra palavra(" ou "palavra("
            comparador = Pattern.compile("(\\s*\\w\\s)?(\\w*\\s\\()").matcher(codigoFonte);

            // se entrar, é porque encontrou uma chamada ou definição de função (loops
            // inclusive)
            if(comparador.find(indiceAbsoluto) && comparador.start(indiceAbsoluto) == 0) {

                System.out.println("how come we don even have functions yet");
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
                    System.out.println("dsaasdasd");
                    // como a linha não se adequa a nenhum dos contextos possíveis, apenas dizemos que o token é inesperado.
                    throw new TokenInesperado(codigoFonte, indiceAbsoluto);
                }
                    
                System.out.println("omfg");
            }
        }
        System.out.println("execução com sucesso do programa.");
    }

    // TODO implementar isso.
    private void verificaAtribuicaoFuncao() throws Erro {
    }

    // resolve atribuições de variáveis, tanto criações quanto alterações.
    // joga um erro caso não encontre um ;.
    // retorna true caso encontre uma atribuição e false caso contrário.
    private boolean verificaAtribuicaoVariavel() throws Erro {
        Matcher comparador;
        ignoraWhiteSpace();

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

            System.out.println(codigoFonte.charAt(indiceAbsoluto));
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
                        Integer valorInteiro = Integer.valueOf(avaliaExpressaoDeInteiros(indiceAbsoluto, comparador.end() - 2));
                        Inteiro i = new Inteiro(nomeVariavel, valorInteiro);
                        Variavel.setVariavel(i, codigoFonte, indiceAbsoluto);
                        
                        System.out.println("eissos " + comparador.end());
                        indiceAbsoluto++; // considerando ";"
                        return true;
                    }
                }
            }
        }
        comparador = Pattern.compile("flutuante\\s").matcher(codigoFonte);
        if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            indiceAbsoluto += 9;

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
                // "flutuante a;").
                if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {

                    Variavel.setVariavel(new Flutuante(nomeVariavel, null), codigoFonte, indiceAbsoluto);

                    indiceAbsoluto += comparador.group().length();
                    return true;

                } else {
                    ignoraWhiteSpace();
                    comparador = Pattern.compile("=[^;]+;").matcher(codigoFonte);
                    // se entrar aqui, é porque o inteiro realmente receberá um valor (como em
                    // "flutuante a = 23;").
                    if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
                        indiceAbsoluto++; // considerando "="

                        // criamos uma variável e a salvamos na lista de variáveis.
                        Double valorFlutuante = Double.valueOf(avaliaExpressaoDeFlutuantes(indiceAbsoluto, comparador.end() - 2));
                        Flutuante i = new Flutuante(nomeVariavel, valorFlutuante);
                        Variavel.setVariavel(i, codigoFonte, indiceAbsoluto);
                        ignoraWhiteSpace();
                        indiceAbsoluto++; // considerando ";"
                        return true;
                    }
                }
            }
        }
        // agora procuramos por uma atribuição do tipo palavra.
        // primeiro, verificamos pela criação de uma palavra.
    
        comparador = Pattern.compile("palavra\\s+").matcher(codigoFonte);
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
                // "palavra a;").
                if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {

                    Variavel.setVariavel(new Palavra(nomeVariavel, null), codigoFonte, indiceAbsoluto);

                    indiceAbsoluto += comparador.group().length();
                    return true;
                } else {

                    ignoraWhiteSpace();
                    comparador = Pattern.compile("\\s*=\\s*").matcher(codigoFonte);
                    if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
                        indiceAbsoluto += comparador.group().length();
                        
                        // criamos uma variável e a salvamos na lista de variáveis.
                        // System.out.println("inicio " + indiceAbsoluto + " fim " + proximoPntVrgNaoContidoEmString(indiceAbsoluto));
                        String valorPalavra = String.valueOf(avaliaExpressaoDePalavras(indiceAbsoluto, proximoCharNaoContidoEmString(indiceAbsoluto - 1, '\"', false)));
                        Palavra i = new Palavra(nomeVariavel, valorPalavra);
                        Variavel.setVariavel(i, codigoFonte, indiceAbsoluto);

                        ignoraWhiteSpace();
                        
                        indiceAbsoluto++; // considerando ";"
                        return true;
                    
                    }
                }
            }
        }

        /* como não encontramos nenhuma criação de variável,
         * vamos verificar se há a alteração de uma variável
         * pré-existente.
         */
        System.out.println(codigoFonte.charAt(indiceAbsoluto));
        comparador = Pattern.compile("[a-zA-Z]+[_0-9]*\\s*=").matcher(codigoFonte);
        if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            String nomeDaVariavel = codigoFonte.substring(indiceAbsoluto, comparador.end() - 2).trim();
            Variavel var = Variavel.getVariavel(nomeDaVariavel);

            // se não conseguimos encontrar a variável, ela ainda não foi declarada.
            if(var == null)
                throw new VariavelInexistente(codigoFonte, indiceAbsoluto);

            indiceAbsoluto += comparador.group().length(); // considerando desde o nome da variável até o ;

            int indicePntVrg = proximoCharNaoContidoEmString(indiceAbsoluto, '\"', false);
            
            switch(var.tipo) {
                case PALAVRA:
                    var.valor = String.valueOf(avaliaExpressaoDePalavras(indiceAbsoluto, indicePntVrg - 1));
                    indiceAbsoluto++; // considerando ;
                    return true;
                case INTEIRO:
                    var.valor = Integer.valueOf(avaliaExpressaoDeInteiros(indiceAbsoluto, indicePntVrg - 1));
                    indiceAbsoluto++; // considerando ;
                    return true;
                case FLUTUANTE:
                    var.valor = Double.valueOf(avaliaExpressaoDeFlutuantes(indiceAbsoluto, indicePntVrg - 1));
                    indiceAbsoluto++; // considerando ;
                    return true;
                case BINARIO:
                    var.valor = Boolean.valueOf(analiseCondicional(indiceAbsoluto, indicePntVrg - 1));
                    indiceAbsoluto++; // considerando ;
                    return true;
            }
        }

        // não encontramos nenhuma atribuição. : (
        return false;
    }

    // retorna o índice do próximo ponto e vírgula garantido de não
    // pertencer à uma string/palavra. então, por exemplo,
    // ";" + ";";
    // retornaria 7. retorna -1 caso não encontre e caractereOpcional seja true.
    public int proximoCharNaoContidoEmString(int inicio, char c, boolean caractereOpcional) throws Erro {
        boolean numeroParDeAspas = true;
        int ultimoIndiceAspas = 0,
            ultimoIndicePntVrg = 0,
            offset = 0;
        while(!(numeroParDeAspas && ultimoIndicePntVrg > ultimoIndiceAspas)) {
            if(inicio + offset > comprimentoDoPrograma - 2) {
                if(!caractereOpcional)
                    throw new AusenciaAspas(codigoFonte, indiceAbsoluto);
                else
                    return -1;
            }
            if(codigoFonte.charAt(inicio + offset) == '\"') {
                numeroParDeAspas = !numeroParDeAspas;
                ultimoIndiceAspas = inicio + offset;
            } else if(codigoFonte.charAt(inicio + offset) == c)
                ultimoIndicePntVrg = inicio + offset;
            offset++;
        }
        return ultimoIndicePntVrg;
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

    // strings em Vali (por hora) podem apenas serem concatenadas por literais ou variáveis flutuantes.
    public String avaliaExpressaoDePalavras(int inicio, int fim) throws Erro {
        ignoraWhiteSpace();
        // o único operador possível para este tipo. concatena strings.
        // procura pelos formatos (entre ><)  >"kdjsdka" +<, >asdj234234 +< ou >"adjh+ajsfhadh" +<
        int indiceSinalConcatenacao = proximoCharNaoContidoEmString(inicio, '+', true);
        if(indiceSinalConcatenacao != -1 && indiceSinalConcatenacao <= fim) {
            String parteEsquerda, parteDireita;
            parteEsquerda = avaliaExpressaoDePalavras(inicio, indiceSinalConcatenacao - 1);
            indiceAbsoluto++;
            parteDireita = avaliaExpressaoDePalavras(indiceSinalConcatenacao + 1, fim);
            return parteDireita + parteEsquerda;
        }
        ignoraWhiteSpace();

        // verifica se encontramos uma " e podemos considerar a próxima sequência como string.
        if(codigoFonte.charAt(indiceAbsoluto) == '\"') {
            
            String str = "";
            
            indiceAbsoluto++; // considerando o " inicial
            while(indiceAbsoluto < comprimentoDoPrograma - 1 && codigoFonte.charAt(indiceAbsoluto) != '\"') {
                str += codigoFonte.charAt(indiceAbsoluto);
                indiceAbsoluto++;
            }
            
            // verificamos se o loop foi quebrado pelos motivos errados (falta de aspas).
            if(indiceAbsoluto > comprimentoDoPrograma - 2)
                throw new AusenciaAspas(codigoFonte, indiceAbsoluto - 1);

            ignoraWhiteSpace();

            // verificamos se ainda há caracteres sobrando nesta "folha" da expressão de palavras.
            if(indiceAbsoluto > fim)
                throw new TokenInesperado(codigoFonte, indiceAbsoluto);

            // erros possíveis verificados, se chegamos aqui é porque a string é completamente aceitável.
            return str;
        }

        // se não encontramos uma string, então tentaremos tratar esta "folha"
        // da expressão como uma expressão algébrica.
        return String.valueOf(avaliaExpressaoDeFlutuantes(inicio, fim));
    }

    // esta função recebe uma expressão aritmética simples e retorna o resultado
    // dela, independente variações de espaçamentos.
    // ex: "21+2*3" retorna 27.
    // início e fim precisam ser índices absolutos.
    // TODO implementar parenteses.
    public int avaliaExpressaoDeInteiros(int inicio, int fim) throws Erro {
        System.out.println("analisando " + codigoFonte.substring(inicio, fim));
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
        String valor = codigoFonte.substring(inicio, fim).trim();

        // primeiro verificamos se o resultado é um literal (número) e retorná-lo se for
        // o caso.
        comparador = Pattern.compile("\\s*\\d+\\s*").matcher(codigoFonte);
        if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            int resultado = Integer.parseInt(valor);
            
            System.out.println(comparador.group());
            indiceAbsoluto += comparador.group().length();
            
            System.out.println("indice pos " + indiceAbsoluto);
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
        comparador = Pattern.compile("(\\s*\\d+\\.\\d+\\s*)|(\\s*\\d+\\s*)").matcher(codigoFonte);
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
        if(indiceAbsoluto > comprimentoDoPrograma) {
        System.out.println("dude.");
            return;
        }
        char c = codigoFonte.charAt(indiceAbsoluto);
        while (indiceAbsoluto < comprimentoDoPrograma && (c == ' ' || c == '\n' || c == '\t')) {  
            indiceAbsoluto++;
            c = codigoFonte.charAt(indiceAbsoluto);
        }
    }

    // caso precisemos tratar de alguma forma o código fonte. possivelmente
    // descartável!
    public void setCodigoFonte(String codigoFonte) {
        if(this.codigoFonte == null)
            this.codigoFonte = codigoFonte;
    }
}
