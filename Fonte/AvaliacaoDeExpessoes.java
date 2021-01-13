package Fonte;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AvaliacaoDeExpessoes {
    // esta função recebe uma expressão aritmética simples e retorna o resultado
    // dela, independente variações de espaçamentos.
    // ex: "21+2*3" retorna 27.
    // início e fim precisam ser índices absolutos.
    // TODO implementar parenteses.
    public int avaliaExpressaoDeInteiros(int inicio, int fim) throws Erro {

        Matcher comparador;
        Tools tools = new Tools();

        tools.ignoraWhiteSpace();

        // procuramos por uma soma ou subtração.                                      estou em dúvida se esta variável pode ser a mesma
        comparador = Pattern.compile("[\\+-]").matcher(Parser.getCodigoFonte()); //←  no Parser e no Vali, da mesma forma, na linha 36
        if (comparador.find(inicio) && comparador.end() <= fim) { // encontrou uma soma ou subtração.
            int parteEsquerda, parteDireita;
            switch (comparador.group()) {
                case "+":
                    parteEsquerda = avaliaExpressaoDeInteiros(inicio, comparador.start() - 1);
                    Parser.sumIndiceAbsoluto(1);
                    parteDireita = avaliaExpressaoDeInteiros(comparador.end(), fim);
                    return parteDireita + parteEsquerda;
                case "-":
                    parteEsquerda = avaliaExpressaoDeInteiros(inicio, comparador.start() - 1);
                    Parser.sumIndiceAbsoluto(1);
                    parteDireita = avaliaExpressaoDeInteiros(comparador.end(), fim);
                    return parteDireita - parteEsquerda;
            }
        }

        // procuramos por um produto ou divisão (inteira).
        comparador = Pattern.compile("[\\*/]").matcher(Parser.getCodigoFonte());
        if (comparador.find(inicio) && comparador.end() <= fim) { // encontrou uma multiplicação ou divisão.
            int parteEsquerda, parteDireita;
            switch (comparador.group()) {
                case "*":
                    parteEsquerda = avaliaExpressaoDeInteiros(inicio, comparador.start() - 1);
                    Parser.sumIndiceAbsoluto(1);
                    parteDireita = avaliaExpressaoDeInteiros(comparador.end(), fim);
                    return parteDireita * parteEsquerda;
                case "/":
                    parteEsquerda = avaliaExpressaoDeInteiros(inicio, comparador.start() - 1);
                    Parser.sumIndiceAbsoluto(1);
                    parteDireita = avaliaExpressaoDeInteiros(comparador.end(), fim);
                    return parteDireita / parteEsquerda;
            }
        }

        /*
         * resolvidas todas as operações, podemos apenas tentar avaliar o resultado que
         * temos e retorná-lo.
         */
        String valor = Parser.getCodigoFonte().substring(inicio, fim + 1).trim();

        // primeiro verificamos se o resultado é um literal (número) e retorná-lo se for
        // o caso.
        comparador = Pattern.compile("\\s*\\d\\s*").matcher(Parser.getCodigoFonte());
        if(comparador.find(Parser.getIndiceAbsoluto())) {
            int resultado = Integer.parseInt(valor);
            Parser.sumIndiceAbsoluto(comparador.group().length());
            return resultado;
        }

        /*
         * resta apenas verificar se é uma chamada de função com retorno inteiro ou uma
         * variável do tipo inteiro. por hora apenas tratamos variáveis e não funções.
         */

        comparador = Pattern.compile("\\s*\\w\\s*").matcher(Parser.getCodigoFonte()); // mesmas regras de nomeação do Java.
        comparador.find(Parser.getIndiceAbsoluto());
        valor = comparador.group();
        Variavel var = Variavel.getVariavel(valor.trim());

        // a variável não existe.
        if (var == null)
            throw new VariavelInexistente(Parser.getCodigoFonte(), Parser.getIndiceAbsoluto());

        // a variável existe, mas não é um inteiro.
        if (var.tipo != Tipos.INTEIRO)
            throw new AtribuicaoTipoIncompativel(Parser.getCodigoFonte(), Parser.getIndiceAbsoluto());

        // assumimos então que a variável existe e possui valor inteiro.
        
        Parser.sumIndiceAbsoluto(comparador.group().length());
        return Integer.parseInt(var.valor.toString());
    }


    public double avaliaExpressaoDeFlutuantes(int inicio, int fim) throws Erro {
        Tools tools = new Tools();
        Matcher comparador;
    
            tools.ignoraWhiteSpace();
    
            // procuramos por uma soma ou subtração.
            comparador = Pattern.compile("[\\+-]").matcher(Parser.getCodigoFonte());
            if (comparador.find(inicio) && comparador.end() <= fim) { // encontrou uma soma ou subtração.
                double parteEsquerda, parteDireita;
                switch (comparador.group()) {
                    case "+":
                        parteEsquerda = avaliaExpressaoDeFlutuantes(inicio, comparador.start() - 1);
                        Parser.sumIndiceAbsoluto(1);
                        parteDireita = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
                        return parteDireita + parteEsquerda;
                    case "-":
                        parteEsquerda = avaliaExpressaoDeFlutuantes(inicio, comparador.start() - 1);
                        Parser.sumIndiceAbsoluto(1);
                        parteDireita = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
                        return parteDireita - parteEsquerda;
                }
            }
    
            // procuramos por um produto ou divisão (inteira).
            comparador = Pattern.compile("[\\*/]").matcher(Parser.getCodigoFonte());
            if (comparador.find(inicio) && comparador.end() <= fim) { // encontrou uma multiplicação ou divisão.
                double parteEsquerda, parteDireita;
                switch (comparador.group()) {
                    case "*":
                        parteEsquerda = avaliaExpressaoDeFlutuantes(inicio, comparador.start() - 1);
                        Parser.sumIndiceAbsoluto(1);
                        parteDireita = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
                        return parteDireita * parteEsquerda;
                    case "/":
                        parteEsquerda = avaliaExpressaoDeFlutuantes(inicio, comparador.start() - 1);
                        Parser.sumIndiceAbsoluto(1);
                        parteDireita = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
                        return parteDireita / parteEsquerda;
                }
            }
            /*
             * resolvidas todas as operações, podemos apenas tentar avaliar o resultado que
             * temos e retorná-lo.
             */
            String valor = Parser.getCodigoFonte().substring(inicio, fim + 1).trim();
    
            // primeiro verificamos se o resultado é um literal (número) e retorná-lo se for
            // o caso.
            comparador = Pattern.compile("\\s*\\d\\s*").matcher(Parser.getCodigoFonte());

        if(comparador.find(Parser.getIndiceAbsoluto()) && comparador.start() == Parser.getIndiceAbsoluto()) {
                double resultado = Double.parseDouble(valor.toString().trim());
                Parser.sumIndiceAbsoluto(comparador.group().length());
                return resultado;
            }
    
            /*
             * resta apenas verificar se é uma chamada de função com retorno flutuante ou uma
             * variável do tipo flutuante. por hora apenas tratamos variáveis e não funções.
             */
    
            comparador = Pattern.compile("\\s*\\w\\s*").matcher(Parser.getCodigoFonte()); // mesmas regras de nomeação do Java.
            comparador.find(Parser.getIndiceAbsoluto());
            valor = comparador.group().trim();
            Variavel var = Variavel.getVariavel(valor);
    
            // a variável não existe.
            if (var == null)
            throw new VariavelInexistente(Parser.getCodigoFonte(), Parser.getIndiceAbsoluto());
    
            // a variável existe, mas não é um inteiro.
            if (var.tipo != Tipos.FLUTUANTE && var.tipo != Tipos.INTEIRO)
                throw new AtribuicaoTipoIncompativel(Parser.getCodigoFonte(), Parser.getIndiceAbsoluto());
                
            // assumimos então que a variável existe e possui valor inteiro.
            Parser.sumIndiceAbsoluto(comparador.group().length());
    
            return Double.parseDouble(var.valor.toString());
    
        }
}
