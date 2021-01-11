public class AtribuicaoVariavel {
    // resolve atribuições de variáveis, tanto criações quanto alterações.
    // joga um erro caso não encontre um ;.

    public boolean verificaAtribuicaoVariavel() throws Erro {
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
                        Integer valorInteiro = Integer.valueOf(avaliaExpressaoDeInteiros(indiceAbsoluto, comparador.end() - 2));
                        Inteiro i = new Inteiro(nomeVariavel, valorInteiro);
                        Variavel.setVariavel(i, codigoFonte, indiceAbsoluto);
                        ignoraWhiteSpace();
                        
                        indiceAbsoluto++; // considerando ";"
                        //System.out.println(codigoFonte.charAt(indiceAbsoluto));
                        return true;
                    }
                }
            }
        } else {
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
                    // "inteiro a;").
                    if (comparador.find(indiceAbsoluto) && comparador.start() == indiceAbsoluto) {

                        Variavel.setVariavel(new Flutuante(nomeVariavel, null), codigoFonte, indiceAbsoluto);

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
        }
        
        return false;
    }
}
