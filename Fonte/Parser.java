import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Parser {

    // codigoFonte guarda todo o código a ser interpretado em uma única string.
    public String codigoFonte;

    // o cursor do parser código fonte.
    private int indiceAbsoluto;
    
    private int comprimentoDoPrograma;

    public Parser(String codigoFonte) {
        this.codigoFonte = codigoFonte;
        this.indiceAbsoluto = 0;
        this.comprimentoDoPrograma = codigoFonte.length();
    }

    public void analisa() throws Erro {
        Variavel.novoEscopo();
        resolveCorpo(indiceAbsoluto, comprimentoDoPrograma - 1);
    }

    // resolve qualquer atribuição, com expressão algébrica e terminada em ;
    public void resolveCorpo(int inicio, int fim) throws Erro {

        Matcher comparador;

        while (indiceAbsoluto < fim) {
            ignoraWhiteSpace();
            if(verificaComentario());
            else {
                // expressão regular para algo no formato "palavra("
                comparador = Pattern.compile("[a-zA-Z]+\\s*\\(").matcher(codigoFonte);
                // se entrar, é porque encontrou uma chamada ou definição de função (laços
                // e condicionais inclusive)
                if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
                    if(!(
                        verificaImprimeTexto() ||
                        verificaSe()           ||
                        verificaEnquanto()            
                        //  verificaPara()             
                      ))
                        throw new TokenInesperado(codigoFonte, indiceAbsoluto);

                } else {
                    // como não é nenhuma função, resta apenas testar se há uma atribuição.
                    if (!verificaAtribuicaoVariavel()) {
                        // como a linha não se adequa a nenhum dos contextos possíveis, apenas dizemos
                        // que o token é inesperado.
                        throw new TokenInesperado(codigoFonte, indiceAbsoluto);
                    }
                }
            }
        }
    }

    private boolean verificaComentario() {
        char c = codigoFonte.charAt(indiceAbsoluto);

        if (c == '/' || c == '\\') {
            while (c != '\n' && indiceAbsoluto < comprimentoDoPrograma - 1) {
                indiceAbsoluto++;
                c = codigoFonte.charAt(indiceAbsoluto);
            }
            indiceAbsoluto++;
            return true;
        } else
            return false;
    }

    private boolean verificaSe() throws Erro {
        Matcher comparador = Pattern.compile("se\\s*\\(").matcher(codigoFonte);
        if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            indiceAbsoluto = comparador.end(); // não consideramos o abre parênteses.
            int indiceFechaParenteses = proximoCharNaoContidoEmString(indiceAbsoluto, ')', false);
            boolean condicional = analiseCondicional(indiceAbsoluto, indiceFechaParenteses - 1);
            indiceAbsoluto++; // considerando )
            comparador = Pattern.compile("\\s*\\{\\s*").matcher(codigoFonte);
            if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
                indiceAbsoluto = comparador.end();
                int indiceFechaChaves = indiceParDeSinal('{');

                if(condicional) {
                    Variavel.novoEscopo();
                    resolveCorpo(indiceAbsoluto, indiceFechaChaves - 2);
                    Variavel.removeEscopo();
                }

                indiceAbsoluto = indiceFechaChaves + 1;

                // verificamos se há a existência de um bloco else.
                comparador = Pattern.compile("\\s*senao\\s*\\{\\s*").matcher(codigoFonte);
                if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
                    indiceAbsoluto = comparador.end();
                    indiceFechaChaves = indiceParDeSinal('{');
                    if(!condicional) {
                        Variavel.novoEscopo();
                        resolveCorpo(indiceAbsoluto, indiceFechaChaves - 2);
                        Variavel.removeEscopo();
                    }
                    indiceAbsoluto = indiceFechaChaves + 1;
                }
                
                return true;

            } else 
                throw new ContagemIrregularChaves(codigoFonte, indiceAbsoluto);
        }
        return false;
    }

    public boolean verificaEnquanto() throws Erro{
        //ignoraWhiteSpace();
        Matcher comparador = Pattern.compile("enquanto\\s*\\(").matcher(codigoFonte);
        if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            int iniciodobloco = comparador.end();
            indiceAbsoluto = comparador.end(); // não consideramos o abre parênteses.
            int indiceFechaParenteses = proximoCharNaoContidoEmString(indiceAbsoluto, ')', false);
            boolean condicional = analiseCondicional(indiceAbsoluto, indiceFechaParenteses - 1);
            indiceAbsoluto++; // considerando )
            comparador = Pattern.compile("\\s*\\{\\s*").matcher(codigoFonte);

            if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
                indiceAbsoluto = comparador.end();
                int indiceFechaChaves = indiceParDeSinal('{');    
            
                while (condicional) {
                    indiceAbsoluto = comparador.end();
                    Variavel.novoEscopo();
                    resolveCorpo(indiceAbsoluto, indiceFechaChaves - 2);
                    Variavel.removeEscopo();
                    indiceAbsoluto = iniciodobloco;
                    condicional = analiseCondicional(indiceAbsoluto, indiceFechaParenteses - 1);
                }
                indiceAbsoluto = indiceFechaChaves + 1;     
                    
                return true;
            }
            else 
                throw new ContagemIrregularChaves(codigoFonte, indiceAbsoluto);
        
        }
        return false;
    }

    // O nosso print(); que se chama imprime();
    public boolean verificaImprimeTexto() throws Erro {
        boolean retorno;
        Matcher comparador = Pattern.compile("imprime\\s*\\(").matcher(codigoFonte);
        if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {

            retorno = true;
            indiceAbsoluto += comparador.group().length();
            int indiceParenteses = proximoCharNaoContidoEmString(indiceAbsoluto, ')', false);
            String s = avaliaExpressaoDePalavras(indiceAbsoluto, indiceParenteses - 1);
            System.out.println(s);
            
            ignoraWhiteSpace();
            indiceAbsoluto = proximoCharNaoContidoEmString(indiceParenteses, ';', false) + 1;
        } else {
            retorno = false;
        }
        return retorno;
    }

    // verifica e resolve atribuições de variáveis, tanto criações quanto alterações.
    // retorna true caso encontre uma atribuição e false caso contrário.
    private boolean verificaAtribuicaoVariavel() throws Erro {
        return verificaCriacaoInteiro()    ||
               verificaCriacaoFlutuante()  ||
               verificaCriacaoPalavra()    ||
               verificaCriacaoBinario()    ||
               verificaAlteracaoVariavel();
    }

    private boolean verificaCriacaoInteiro() throws Erro {
        Matcher comparador = Pattern.compile("inteiro\\s").matcher(codigoFonte);
        if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {

            indiceAbsoluto += 7;

            ignoraWhiteSpace();

            // usamos regex para formatar a saída.
            // usamos as mesmas regras para nomeção de variáveis que o Java.
            comparador = Pattern.compile("[a-zA-Z]+\\w*").matcher(codigoFonte);

            // se não encontrar nada, é porque o próximo token é inválido.
            if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
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
                        ignoraWhiteSpace();
                        indiceAbsoluto = comparador.end(); // considerando ";"
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean verificaCriacaoFlutuante() throws Erro {
        Matcher comparador = Pattern.compile("flutuante\\s").matcher(codigoFonte);
        if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            indiceAbsoluto += 9;

            ignoraWhiteSpace();

            // usamos regex para formatar a saída.
            // usamos as mesmas regras para nomeção de variáveis que o Java.
            comparador = Pattern.compile("[a-zA-Z]+\\w*").matcher(codigoFonte);

            // se não encontrar nada, é porque o próximo token é inválido.
            if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
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
                        Double valorFlutuante = Double
                                .valueOf(avaliaExpressaoDeFlutuantes(indiceAbsoluto, comparador.end() - 2));
                        Flutuante i = new Flutuante(nomeVariavel, valorFlutuante);
                        Variavel.setVariavel(i, codigoFonte, indiceAbsoluto);
                        ignoraWhiteSpace();
                        indiceAbsoluto = comparador.end(); // considerando ";"
                        return true;
                    }
                }
            }
        }
        return false;
    }
 
    private boolean verificaCriacaoPalavra() throws Erro {
        Matcher comparador = Pattern.compile("palavra\\s+").matcher(codigoFonte);
        if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            indiceAbsoluto += 7;

            ignoraWhiteSpace();

            // usamos regex para formatar a saída.
            // usamos as mesmas regras para nomeção de variáveis que o Java.
            comparador = Pattern.compile("[a-zA-Z]+\\w*").matcher(codigoFonte);

            // se não encontrar nada, é porque o próximo token é inválido.
            if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
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
                    if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
                        indiceAbsoluto += comparador.group().length();

                        int indiceDeSinal = proximoCharNaoContidoEmString(indiceAbsoluto, ';', false);

                        // criamos uma variável e a salvamos na lista de variáveis.
                        String valorPalavra = String.valueOf(avaliaExpressaoDePalavras(indiceAbsoluto,
                                indiceDeSinal - 1));
                                
                        Palavra i = new Palavra(nomeVariavel, valorPalavra);
                        Variavel.setVariavel(i, codigoFonte, indiceAbsoluto);

                        indiceAbsoluto = indiceDeSinal + 1; // considerando ";"
                        return true;

                    }
                }
            }
        }
        return false;
    }

    private boolean verificaCriacaoBinario() throws Erro {
        Matcher comparador = Pattern.compile("binario\\s+").matcher(codigoFonte);
        if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            indiceAbsoluto += 8;

            ignoraWhiteSpace();

            // usamos regex para formatar a saída.
            // usamos as mesmas regras para nomeção de variáveis que o Java.
            comparador = Pattern.compile("[a-zA-Z]+\\w*").matcher(codigoFonte);

            // se não encontrar nada, é porque o próximo token é inválido.
            if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
                // age como uma forma de "next()" do Scanner.
                String nomeVariavel = comparador.group();
                indiceAbsoluto += nomeVariavel.length();

                comparador = Pattern.compile("\\s*;\\s*").matcher(codigoFonte);

                // se entrar aqui, é porque a variável não está recebendo um valor (como em
                // "palavra a;").
                if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {

                    Variavel.setVariavel(new Binario(nomeVariavel, null), codigoFonte, indiceAbsoluto);

                    indiceAbsoluto += comparador.group().length();
                    return true;
                } else {
                    ignoraWhiteSpace();
                    comparador = Pattern.compile("\\s*=\\s*").matcher(codigoFonte);
                    if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
                        indiceAbsoluto += comparador.group().length();

                        int indiceDeSinal = proximoCharNaoContidoEmString(indiceAbsoluto, ';', false);

                        // criamos uma variável e a salvamos na lista de variáveis.
                        Boolean valorPalavra = Boolean.valueOf(analiseCondicional(indiceAbsoluto,
                                indiceDeSinal - 1));
                                
                        Binario i = new Binario(nomeVariavel, valorPalavra);
                        Variavel.setVariavel(i, codigoFonte, indiceAbsoluto);

                        indiceAbsoluto = indiceDeSinal + 1; // considerando ";"
                        return true;

                    }
                }
            }
        }
        return false;
    }

    private boolean verificaAlteracaoVariavel() throws Erro {
        Matcher comparador = Pattern.compile("[a-zA-Z]+\\w*\\s*=").matcher(codigoFonte);
        if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            String nomeDaVariavel = codigoFonte.substring(indiceAbsoluto, comparador.end() - 2).trim();
            Variavel var = Variavel.getVariavel(nomeDaVariavel);

            // se não conseguimos encontrar a variável, ela ainda não foi declarada.
            if (var == null)
                throw new VariavelInexistente(codigoFonte, indiceAbsoluto);

            indiceAbsoluto += comparador.group().length(); // considerando desde o nome da variável até o ;
            ignoraWhiteSpace();
            int indicePntVrg = proximoCharNaoContidoEmString(indiceAbsoluto, ';', false);

            switch (var.tipo) {
                case PALAVRA:
                    var.valor = String.valueOf(avaliaExpressaoDePalavras(indiceAbsoluto, indicePntVrg - 1));
                    indiceAbsoluto = indicePntVrg + 1;
                    return true;
                case INTEIRO:
                    var.valor = Integer.valueOf(avaliaExpressaoDeInteiros(indiceAbsoluto, indicePntVrg - 1));
                    indiceAbsoluto = indicePntVrg + 1;
                    return true;
                case FLUTUANTE:
                    var.valor = Double.valueOf(avaliaExpressaoDeFlutuantes(indiceAbsoluto, indicePntVrg - 1));
                    indiceAbsoluto = indicePntVrg + 1;
                    return true;
                case BINARIO:
                    var.valor = Boolean.valueOf(analiseCondicional(indiceAbsoluto, indicePntVrg - 1));
                    indiceAbsoluto = indicePntVrg + 1;
                    return true;
            }
        }
        return false;
    }

    // analisa expressões booleanas.
    public boolean analiseCondicional(int inicio, int fim) throws Erro {
        ignoraWhiteSpace();

        Matcher comparador = Pattern.compile("\\|").matcher(codigoFonte);
        if (comparador.find(inicio) && comparador.end() <= fim) {
            boolean ladoEsquerdo = analiseCondicional(inicio, comparador.start() - 1);
            indiceAbsoluto++;
            boolean ladoDireito = analiseCondicional(comparador.end(), fim);
            return ladoEsquerdo || ladoDireito;
        }

        comparador = Pattern.compile("&").matcher(codigoFonte);
        if (comparador.find(inicio) && comparador.end() <= fim) {
            boolean ladoEsquerdo = analiseCondicional(inicio, comparador.start() - 1);
            indiceAbsoluto++;
            boolean ladoDireito = analiseCondicional(comparador.end(), fim);
            return ladoEsquerdo && ladoDireito;
        }

        comparador = Pattern.compile("(<=)|(>=)|>|<|(==)|(!=)").matcher(codigoFonte);
        if (comparador.find(inicio) && comparador.end() <= fim) {
            String operacao = comparador.group();
            double ladoEsquerdo = avaliaExpressaoDeFlutuantes(inicio, comparador.start() - 1);
            indiceAbsoluto += operacao.length();
            double ladoDireito = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
            // como doubles são levemente inconsistentes, usamos o compare para garantir
            // uma comparação mais provável de estar correta.
            int resultadoComparacao = Double.compare(ladoEsquerdo, ladoDireito);
            switch (operacao) {
                case "==":
                    return resultadoComparacao == 0;
                case ">":
                    return resultadoComparacao > 0;
                case "<":
                    return resultadoComparacao < 0;
                case "<=":
                    return resultadoComparacao <= 0;
                case ">=":
                    return resultadoComparacao >= 0;
                case "!=":
                    return resultadoComparacao != 0;
            }
        }

        // por último, verficamos se há uma tentativa de inverter o valor de uma folha.
        comparador = Pattern.compile("!\\s*").matcher(codigoFonte);
        if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            indiceAbsoluto += comparador.group().length();
            return !analiseCondicional(comparador.end(), fim);
        }

        // primeiro verificamos se a "folha" é um literal.
        comparador = Pattern.compile("(verdadeiro|falso)\\s*").matcher(codigoFonte);
        if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            indiceAbsoluto += comparador.group().length();	
            switch(comparador.group().trim()) {
                case "verdadeiro":
                    return true;
                case "falso":
                    return false;
            }
        }

        // verificamos se a "folha" é uma variável.
        comparador = Pattern.compile("\\s*[a-zA-Z]+\\w*\\s*").matcher(codigoFonte); // mesmas regras de nomeação do Java.
        if(comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            String valor = comparador.group().trim();
            Variavel var = Variavel.getVariavel(valor);
            // a variável não existe.
            if (var == null)
                throw new VariavelInexistente(codigoFonte, indiceAbsoluto);

            indiceAbsoluto += comparador.group().length();
            // agora que sabemos que a variável existe, apenas retornamos seu valor em
            // string.
            return Boolean.parseBoolean(var.valor.toString());
        } else
            throw new TokenInesperado(codigoFonte, indiceAbsoluto);
    }

    public String avaliaExpressaoDePalavras(int inicio, int fim) throws Erro {
        ignoraWhiteSpace();
        // + é o único operador possível para este tipo. concatena strings.
        // procura pelos seguintes padrões e os concatena:
        // "conteudoDeString"
        // nomeDeVariavel +
        // "conteudoDeString" +
        int indiceSinalConcatenacao = proximoCharNaoContidoEmString(inicio, '+', true);
        if (indiceSinalConcatenacao != -1 && indiceSinalConcatenacao <= fim) {
            String parteEsquerda, parteDireita;
            parteEsquerda = avaliaExpressaoDePalavras(inicio, indiceSinalConcatenacao - 1);
            indiceAbsoluto = indiceSinalConcatenacao + 1;
            parteDireita = avaliaExpressaoDePalavras(indiceSinalConcatenacao + 1, fim);
            return parteEsquerda + parteDireita;
        }

        // verifica se encontramos aspas duplas e podemos considerar a
        // próxima sequência como string.
        if (codigoFonte.charAt(inicio) == '\"') {
            
            String str = "";
            inicio++; // considerando o " inicial
            while (inicio < comprimentoDoPrograma - 1 &&
                   codigoFonte.charAt(inicio) != '\"') {
                str += codigoFonte.charAt(inicio);
                inicio++;
            }

            // verificamos se o loop foi quebrado pelos motivos errados (falta de aspas).
            if (inicio > comprimentoDoPrograma - 1)
                throw new AusenciaAspas(codigoFonte, inicio - 1);

            ignoraWhiteSpace();

            // verificamos se ainda há caracteres não-esperados sobrando
            // nesta "folha" da expressão.
            if (inicio > fim)
                throw new TokenInesperado(codigoFonte, inicio);

            // erros possíveis verificados, se chegamos aqui é porque a string é
            // completamente aceitável.
            return str;
        }

        // se não encontramos nenhuma string literal, trataremos essa "folha" como uma
        // variável.
        Matcher comparador = Pattern.compile("\\s*[a-zA-Z]+\\w*\\s*").matcher(codigoFonte); // mesmas regras de nomeação do Java.
        comparador.find(indiceAbsoluto);
        String valor = comparador.group().trim();
        Variavel var = Variavel.getVariavel(valor);
        // a variável não existe.
        if (var == null)
            throw new VariavelInexistente(codigoFonte, indiceAbsoluto);

        indiceAbsoluto += comparador.group().length();
        // agora que sabemos que a variável existe, apenas retornamos seu valor em
        // string.
        if(var.tipo == Tipos.BINARIO) {
            switch(var.valor.toString()) {
                case "true":
                    return "verdadeiro";
                case "false":
                    return "falso";
            }
        }
        return var.valor.toString();

    }

    // esta função recebe uma expressão aritmética simples e retorna o resultado
    // dela, independente variações de espaçamentos.
    // ex: "21+2*3" retorna 27.
    // início e fim precisam ser índices absolutos.
    // TODO implementar parenteses.
    public int avaliaExpressaoDeInteiros(int inicio, int fim) throws Erro {
        Matcher comparador;

        ignoraWhiteSpace();
    
        // procuramos por uma soma ou subtração (mais especificamente, como operação binária).
        // procura por algo no formato a+ ou a-, a sendo qualquer char que não seja operação.
        comparador = Pattern.compile("[^\\+\\*-/\\s]\\s*[\\+-]").matcher(codigoFonte);
        if (comparador.find(indiceAbsoluto) && comparador.end() <= fim) {
            int parteEsquerda, parteDireita;
            int offsetOperacao = comparador.group().length() - 1;
            switch (comparador.group().charAt(offsetOperacao)) { // capturamos o último elemento do grupo, isto é, a operação em si.
                case '+':
                    parteEsquerda = avaliaExpressaoDeInteiros(indiceAbsoluto, indiceAbsoluto + offsetOperacao);
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeInteiros(comparador.end(), fim);
                    return parteEsquerda + parteDireita;
                case '-':
                    parteEsquerda = avaliaExpressaoDeInteiros(indiceAbsoluto, indiceAbsoluto + offsetOperacao);
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeInteiros(comparador.end(), fim);
                    return parteEsquerda - parteDireita;
            }
        }

        // procuramos por um produto ou divisão (inteira).
        comparador = Pattern.compile("[\\*/]").matcher(codigoFonte);
        if (comparador.find(indiceAbsoluto) && comparador.end() <= fim) { // encontrou uma multiplicação ou divisão.
            int parteEsquerda, parteDireita;
            switch (comparador.group()) {
                case "*":
                    parteEsquerda = avaliaExpressaoDeInteiros(indiceAbsoluto, comparador.start() - 1);
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeInteiros(comparador.end(), fim);
                    return parteEsquerda * parteDireita;
                case "/":
                    parteEsquerda = avaliaExpressaoDeInteiros(indiceAbsoluto, comparador.start() - 1);
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeInteiros(comparador.end(), fim);
                    return parteEsquerda / parteDireita;
            }
        }
    
        // agora procuramos por operações unárias.
        comparador = Pattern.compile("-\\s*\\w+").matcher(codigoFonte);
        if (comparador.find(inicio) && comparador.start() <= fim) { // encontrou uma soma ou subtração.
            indiceAbsoluto++; // considerando o -
            return -avaliaExpressaoDeInteiros(indiceAbsoluto, fim);
        }

        /*
         * resolvidas todas as operações, podemos apenas tentar avaliar o resultado que
         * temos e retorná-lo.
         */
        String valor = codigoFonte.substring(inicio, fim + 1).trim();

        // primeiro verificamos se o resultado é um literal (número) e retorná-lo se for
        // o caso.
        comparador = Pattern.compile("\\s*\\d+\\s*").matcher(codigoFonte);
        if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            int resultado = Integer.parseInt(valor);

            indiceAbsoluto += comparador.group().length();

            return resultado;
        }

        /*
         * resta apenas verificar se é uma chamada de função com retorno inteiro ou uma
         * variável do tipo inteiro. por hora apenas tratamos variáveis e não funções.
         */
        System.out.println("aqyuekle oiyutro gurpo: " + codigoFonte.substring(indiceAbsoluto, fim  + 1));
        comparador = Pattern.compile("\\s*[a-zA-Z]+\\w*\\s*").matcher(codigoFonte); // mesmas regras de nomeação do Java.
        comparador.find(indiceAbsoluto);
        valor = comparador.group().trim();
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

    public double avaliaExpressaoDeFlutuantes(int inicio, int fim) throws Erro {

        Matcher comparador;

        ignoraWhiteSpace();

       // procuramos por uma soma ou subtração (mais especificamente, como operação binária).
        // procura por algo no formato a+ ou a-, a sendo qualquer char que não seja operação.
        comparador = Pattern.compile("[^\\+\\*-/\\s]\\s*[\\+-]").matcher(codigoFonte);
        if (comparador.find(indiceAbsoluto) && comparador.end() <= fim) {
            double parteEsquerda, parteDireita;
            int offsetOperacao = comparador.group().length() - 1;
            switch (comparador.group().charAt(offsetOperacao)) { // capturamos o último elemento do grupo, isto é, a operação em si.
                case '+':
                    parteEsquerda = avaliaExpressaoDeFlutuantes(indiceAbsoluto, indiceAbsoluto + offsetOperacao);
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
                    return parteEsquerda + parteDireita;
                case '-':
                    parteEsquerda = avaliaExpressaoDeFlutuantes(indiceAbsoluto, indiceAbsoluto + offsetOperacao);
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
                    return parteEsquerda - parteDireita;
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
                    return parteEsquerda * parteDireita;
                case "/":
                    parteEsquerda = avaliaExpressaoDeFlutuantes(inicio, comparador.start() - 1);
                    indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
                    return parteEsquerda / parteDireita;
            }
        }

        // agora procuramos por operações unárias.
        comparador = Pattern.compile("-\\s*\\w+").matcher(codigoFonte);
        if (comparador.find(inicio) && comparador.start() <= fim) { // encontrou uma soma ou subtração.
            indiceAbsoluto++; // considerando o -
            return -avaliaExpressaoDeInteiros(indiceAbsoluto, fim);
        }
        
        /*
         * resolvidas todas as operações, podemos apenas tentar avaliar o resultado que
         * temos e retorná-lo.
         */
        String valor = codigoFonte.substring(inicio, fim + 1).trim();

        // primeiro verificamos se o resultado é um literal (número) e retorná-lo se for
        // o caso.
        comparador = Pattern.compile("(\\s*\\d+\\.\\d+\\s*)|(\\s*\\d+\\s*)").matcher(codigoFonte);
        if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {
            double resultado = Double.parseDouble(valor);
            indiceAbsoluto += comparador.group().length();
            return resultado;
        }

        /*
         * resta apenas verificar se é uma chamada de função com retorno flutuante ou
         * uma variável do tipo flutuante. por hora apenas tratamos variáveis e não
         * funções.
         */
        comparador = Pattern.compile("\\s*[a-zA-Z]+\\w*\\s*").matcher(codigoFonte); // mesmas regras de nomeação do Java.
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

    // considera que o char anterior é um { ou  ( e retorna o índice do } ou ) respectivo.
    // joga erros caso não encontre.
    private int indiceParDeSinal(char sinal) throws Erro {
        char sinalContrario = sinal == '{'? '}': ')';
        int paridadeChave = 1,
            offset = 0;
        boolean contidoEmAspas = false;
        while(paridadeChave != 0 && indiceAbsoluto + offset < comprimentoDoPrograma - 1) {
            if(codigoFonte.charAt(indiceAbsoluto + offset) == sinal && !contidoEmAspas)
                paridadeChave++;
            else if(codigoFonte.charAt(indiceAbsoluto + offset) == sinalContrario && !contidoEmAspas)
                paridadeChave--;
            else if(codigoFonte.charAt(indiceAbsoluto + offset) == '\"')
                contidoEmAspas = !contidoEmAspas;
            offset++;
        }

        if(indiceAbsoluto + offset > comprimentoDoPrograma - 1)
            if(sinal == '{')
                throw new ContagemIrregularChaves(codigoFonte, indiceAbsoluto);
            else
                throw new NumeroInadequadoParenteses(codigoFonte, indiceAbsoluto);
        return indiceAbsoluto + (offset);
    }

    // retorna o índice do próximo ponto e vírgula garantido de não
    // pertencer à uma string/palavra. então, por exemplo,
    // ";" + ";";
    // retornaria 7. retorna -1 caso não encontre e caractereOpcional seja true.
    public int proximoCharNaoContidoEmString(int inicio, char c, boolean caractereOpcional) throws Erro {
        boolean numeroParDeAspas = true;
        int ultimoIndiceAspas = 0, ultimoIndicePntVrg = 0, offset = 0;
        while (!(numeroParDeAspas && ultimoIndicePntVrg > ultimoIndiceAspas)) {
            if (inicio + offset > comprimentoDoPrograma - 1) {
                if (!caractereOpcional) {
                    switch(c) {
                        case '\"':
                            throw new AusenciaAspas(codigoFonte, indiceAbsoluto);
                        case ';':
                            throw new AusenciaPontoEVirgula(codigoFonte, indiceAbsoluto);
                        case ')':
                            throw new AusenciaParenteses(codigoFonte, indiceAbsoluto);
                    }                    
                }
                else
                    return -1;
            }
            if (codigoFonte.charAt(inicio + offset) == '\"') {
                numeroParDeAspas = !numeroParDeAspas;
                ultimoIndiceAspas = inicio + offset;
            } else if (codigoFonte.charAt(inicio + offset) == c)
                ultimoIndicePntVrg = inicio + offset;
            offset++;
        }
        return ultimoIndicePntVrg;
    }

    // pula todos os espaços em branco.
    private void ignoraWhiteSpace() {
        if (indiceAbsoluto > comprimentoDoPrograma) {
            return;
        }
        char c = codigoFonte.charAt(indiceAbsoluto);
        while (indiceAbsoluto < comprimentoDoPrograma && (c == ' ' || c == '\n' || c == '\t')) {
            indiceAbsoluto++;
            c = codigoFonte.charAt(indiceAbsoluto);
        }
    }
}