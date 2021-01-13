package Fonte;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Condicoes {
    // analisa expressões booleanas.
    // TODO tratar variáveis binario, operador "!" e as keywords "verdadeiro" e "falso".
    public boolean analiseCondicional(int inicio, int fim) throws Erro {

        Matcher comparador = Pattern.compile("\\|").matcher(Parser.codigoFonte);
        if(comparador.find(inicio) && comparador.end() < fim) {
            return analiseCondicional(inicio, comparador.start()) || analiseCondicional(comparador.end(), fim);   
        }

        comparador = Pattern.compile("&").matcher(Parser.codigoFonte);
        if(comparador.find(inicio) && comparador.end() < fim) {
            return analiseCondicional(inicio, comparador.start()) && analiseCondicional(comparador.end(), fim);   
        }

        comparador = Pattern.compile(">|<|==|<=|>=|!=").matcher(Parser.codigoFonte);
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
        throw new TokenInesperado(Parser.codigoFonte, indiceAbsoluto);
    }
}
