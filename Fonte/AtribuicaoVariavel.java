import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AtribuicaoVariavel {
    Tools tools = new Tools();
    // resolve atribuições de variáveis, tanto criações quanto alterações.
    // joga um erro caso não encontre um ;.

    public boolean verificaAtribuicaoVariavel() throws Erro {
        Matcher comparador;
        AvaliacaoDeExpessoes avalia = new AvaliacaoDeExpessoes();
        // Variavel.imprimeVariaveis();
        
        /*
         * primeiro, precisamos determinar se é uma criação ou atualização de variável.
         * começamos verificando se há uma criação.
         */


        // verifica primeiramente a criação de um inteiro.
        // incluímos espaços em branco pois "inteiro1", por exemplo, é um nome aceitável
        // de variável em Vali.
        
        comparador = Pattern.compile("inteiro\\s").matcher(Parser.getCodigoFonte());
        if(comparador.find(Parser.getIndiceAbsoluto()) && comparador.start() == Parser.getIndiceAbsoluto()) {
            Parser.sumIndiceAbsoluto(7);

            tools.ignoraWhiteSpace();

            // usamos regex para formatar a saída.
            // usamos as mesmas regras para nomeção de variáveis que o Java.
            comparador = Pattern.compile("[a-zA-Z]+[_0-9]*").matcher(Parser.getCodigoFonte());
            
            // se não encontrar nada, é porque o próximo token é inválido.
            if(comparador.find(Parser.getIndiceAbsoluto()) && comparador.start() == Parser.getIndiceAbsoluto()) {
                // age como uma forma de "next()" do Scanner.
                String nomeVariavel = comparador.group();
                Parser.sumIndiceAbsoluto(nomeVariavel.length());

                comparador = Pattern.compile("\\s*;\\s*").matcher(Parser.getCodigoFonte());

                // se entrar aqui, é porque a variável não está recebendo um valor (como em
                // "inteiro a;").
                if (comparador.find(Parser.getIndiceAbsoluto()) && comparador.start() == Parser.getIndiceAbsoluto()) {

                    Variavel.setVariavel(new Inteiro(nomeVariavel, null), Parser.getCodigoFonte(), Parser.getIndiceAbsoluto());

                    Parser.sumIndiceAbsoluto (comparador.group().length());
                    return true;

                } else {
                    tools.ignoraWhiteSpace();
                    comparador = Pattern.compile("=[^;]+;").matcher(Parser.getCodigoFonte());
                    // se entrar aqui, é porque o inteiro realmente receberá um valor (como em
                    // "inteiro a = 23;").
                    if (comparador.find(Parser.getIndiceAbsoluto()) && comparador.start() == Parser.getIndiceAbsoluto()) {
                        Parser.sumIndiceAbsoluto(1); // considerando "="

                        // criamos uma variável e a salvamos na lista de variáveis.
                        Integer valorInteiro = Integer.valueOf(avalia.avaliaExpressaoDeInteiros(Parser.getIndiceAbsoluto(), comparador.end() - 2));
                        Inteiro i = new Inteiro(nomeVariavel, valorInteiro);
                        Variavel.setVariavel(i, Parser.getCodigoFonte(), Parser.getIndiceAbsoluto());
                        tools.ignoraWhiteSpace();
                        
                        Parser.sumIndiceAbsoluto(1); // considerando ";"
                        //System.out.println(Vali.codigoFonte.charAt(Parser.indiceAbsoluto));
                        return true;
                    }
                }
            }
        } else {
            comparador = Pattern.compile("flutuante\\s").matcher(Parser.getCodigoFonte());
            if(comparador.find(Parser.getIndiceAbsoluto()) && comparador.start() == Parser.getIndiceAbsoluto()) {
                Parser.sumIndiceAbsoluto(9);

                tools.ignoraWhiteSpace();

                // usamos regex para formatar a saída.
                // usamos as mesmas regras para nomeção de variáveis que o Java.
                comparador = Pattern.compile("[a-zA-Z]+[_0-9]*").matcher(Parser.getCodigoFonte());
                
                // se não encontrar nada, é porque o próximo token é inválido.
                if(comparador.find(Parser.getIndiceAbsoluto()) && comparador.start() == Parser.getIndiceAbsoluto()) {
                    // age como uma forma de "next()" do Scanner.
                    String nomeVariavel = comparador.group();
                    Parser.sumIndiceAbsoluto(nomeVariavel.length());

                    comparador = Pattern.compile("\\s*;\\s*").matcher(Parser.getCodigoFonte());

                    // se entrar aqui, é porque a variável não está recebendo um valor (como em
                    // "inteiro a;").
                    if (comparador.find(Parser.getIndiceAbsoluto()) && comparador.start() == Parser.getIndiceAbsoluto()) {

                        Variavel.setVariavel(new Flutuante(nomeVariavel, null), Parser.getCodigoFonte(), Parser.getIndiceAbsoluto());

                        Parser.sumIndiceAbsoluto(comparador.group().length());
                        return true;

                    } else {
                        tools.ignoraWhiteSpace();
                        comparador = Pattern.compile("=[^;]+;").matcher(Parser.getCodigoFonte());
                        // se entrar aqui, é porque o inteiro realmente receberá um valor (como em
                        // "inteiro a = 23;").
                        if (comparador.find(Parser.getIndiceAbsoluto()) && comparador.start() == Parser.getIndiceAbsoluto()) {
                            Parser.sumIndiceAbsoluto(1); // considerando "="

                            // criamos uma variável e a salvamos na lista de variáveis.
                            Double valorFlutuante = Double.valueOf(avalia.avaliaExpressaoDeFlutuantes(Parser.getIndiceAbsoluto(), comparador.end() - 2));
                            Flutuante i = new Flutuante(nomeVariavel, valorFlutuante);
                            Variavel.setVariavel(i, Parser.getCodigoFonte(), Parser.getIndiceAbsoluto());
                            tools.ignoraWhiteSpace();
                            Parser.sumIndiceAbsoluto(1); // considerando ";"
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
}
