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
                    Parser.indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeInteiros(comparador.end(), fim);
                    return parteDireita + parteEsquerda;
                case "-":
                    parteEsquerda = avaliaExpressaoDeInteiros(inicio, comparador.start() - 1);
                    Parser.indiceAbsoluto++;
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
                    Parser.indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeInteiros(comparador.end(), fim);
                    return parteDireita * parteEsquerda;
                case "/":
                    parteEsquerda = avaliaExpressaoDeInteiros(inicio, comparador.start() - 1);
                    Parser.indiceAbsoluto++;
                    parteDireita = avaliaExpressaoDeInteiros(comparador.end(), fim);
                    return parteDireita / parteEsquerda;
            }
        }

        /*
         * resolvidas todas as operações, podemos apenas tentar avaliar o resultado que
         * temos e retorná-lo.
         */
        String valor = Vali.codigoFonte.substring(inicio, fim + 1).trim();

        // primeiro verificamos se o resultado é um literal (número) e retorná-lo se for
        // o caso.
        comparador = Pattern.compile("\\s*\\d\\s*").matcher(Vali.codigoFonte);
        if(comparador.find(Parser.indiceAbsoluto)) {
            int resultado = Integer.parseInt(valor);
            Parser.indiceAbsoluto += comparador.group().length();
            return resultado;
        }

        /*
         * resta apenas verificar se é uma chamada de função com retorno inteiro ou uma
         * variável do tipo inteiro. por hora apenas tratamos variáveis e não funções.
         */

        comparador = Pattern.compile("\\s*\\w\\s*").matcher(Vali.codigoFonte); // mesmas regras de nomeação do Java.
        comparador.find(Parser.indiceAbsoluto);
        valor = comparador.group();
        Variavel var = Variavel.getVariavel(valor.trim());

        // a variável não existe.
        if (var == null)
            throw new VariavelInexistente(Vali.codigoFonte, Parser.indiceAbsoluto);

        // a variável existe, mas não é um inteiro.
        if (var.tipo != Tipos.INTEIRO)
            throw new AtribuicaoTipoIncompativel(Vali.codigoFonte, Parser.indiceAbsoluto);

        // assumimos então que a variável existe e possui valor inteiro.
        
        Parser.indiceAbsoluto += comparador.group().length();
        return Integer.parseInt(var.valor.toString());
    }


    public double avaliaExpressaoDeFlutuantes(int inicio, int fim) throws Erro {
        Tools tools = new Tools();
        Matcher comparador;
    
            tools.ignoraWhiteSpace();
    
            // procuramos por uma soma ou subtração.
            comparador = Pattern.compile("[\\+-]").matcher(Vali.codigoFonte);
            if (comparador.find(inicio) && comparador.end() <= fim) { // encontrou uma soma ou subtração.
                double parteEsquerda, parteDireita;
                switch (comparador.group()) {
                    case "+":
                        parteEsquerda = avaliaExpressaoDeFlutuantes(inicio, comparador.start() - 1);
                        Parser.indiceAbsoluto++;
                        parteDireita = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
                        return parteDireita + parteEsquerda;
                    case "-":
                        parteEsquerda = avaliaExpressaoDeFlutuantes(inicio, comparador.start() - 1);
                        Parser.indiceAbsoluto++;
                        parteDireita = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
                        return parteDireita - parteEsquerda;
                }
            }
    
            // procuramos por um produto ou divisão (inteira).
            comparador = Pattern.compile("[\\*/]").matcher(Vali.codigoFonte);
            if (comparador.find(inicio) && comparador.end() <= fim) { // encontrou uma multiplicação ou divisão.
                double parteEsquerda, parteDireita;
                switch (comparador.group()) {
                    case "*":
                        parteEsquerda = avaliaExpressaoDeFlutuantes(inicio, comparador.start() - 1);
                        Parser.indiceAbsoluto++;
                        parteDireita = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
                        return parteDireita * parteEsquerda;
                    case "/":
                        parteEsquerda = avaliaExpressaoDeFlutuantes(inicio, comparador.start() - 1);
                        Parser.indiceAbsoluto++;
                        parteDireita = avaliaExpressaoDeFlutuantes(comparador.end(), fim);
                        return parteDireita / parteEsquerda;
                }
            }
    
            /*
             * resolvidas todas as operações, podemos apenas tentar avaliar o resultado que
             * temos e retorná-lo.
             */
            String valor = Vali.codigoFonte.substring(inicio, fim + 1).trim();
    
            // primeiro verificamos se o resultado é um literal (número) e retorná-lo se for
            // o caso.
            comparador = Pattern.compile("\\s*\\d\\s*").matcher(Vali.codigoFonte);
        if(comparador.find(Parser.indiceAbsoluto) && comparador.start() == Parser.indiceAbsoluto) {
                double resultado = Double.parseDouble(valor.toString().trim());
                Parser.indiceAbsoluto += comparador.group().length();
                return resultado;
            }
    
            /*
             * resta apenas verificar se é uma chamada de função com retorno flutuante ou uma
             * variável do tipo flutuante. por hora apenas tratamos variáveis e não funções.
             */
    
            comparador = Pattern.compile("\\s*\\w\\s*").matcher(Vali.codigoFonte); // mesmas regras de nomeação do Java.
            comparador.find(Parser.indiceAbsoluto);
            valor = comparador.group().trim();
            Variavel var = Variavel.getVariavel(valor);
    
            // a variável não existe.
            if (var == null)
            throw new VariavelInexistente(Vali.codigoFonte, Parser.indiceAbsoluto);
    
            // a variável existe, mas não é um inteiro.
            if (var.tipo != Tipos.FLUTUANTE && var.tipo != Tipos.INTEIRO)
                throw new AtribuicaoTipoIncompativel(Vali.codigoFonte, Parser.indiceAbsoluto);
                
            // assumimos então que a variável existe e possui valor inteiro.
            Parser.indiceAbsoluto += comparador.group().length();
    
            return Double.parseDouble(var.valor.toString());
    
        }
}
