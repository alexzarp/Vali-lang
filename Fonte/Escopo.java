public class Escopo {
    AtribuicaoVariavel atribuicao = new AtribuicaoVariavel();
    // resolve qualquer atribuição, com expressão algébrica e terminada em ;
    public void resolveCorpo() throws Erro {

        // utilizamos expressões regulares para várias verificações ao longo da função.
        Matcher comparador;
        ignoraWhiteSpace();

        // enquanto ainda há linhas do código fonte a serem avaliadas, verificamos se a próxima linha
        // se encaixa em algum dos contextos esperados (chamadas de funções, atribuições de funções,
        // loops, condicionais e atribuições de variáveis). se a linha não se encaixar em nenhum contexto,
        // jogamos um erro de token inesperado.
        while(indiceAbsoluto <= comprimentoDoPrograma - 2) { // -1 por causa do índice 0 e -1 por causa do \n extra que adicionamos no Vali.java
        ignoraWhiteSpace();
            // regex para algo no formato "palavra palavra(" ou "palavra("
            comparador = Pattern.compile("(\\s*\\w\\s)?(\\w*\\s\\()").matcher(codigoFonte);

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
                if(!atribuicao.verificaAtribuicaoVariavel()) {
                    // como a linha não se adequa a nenhum dos contextos possíveis, apenas dizemos que o token é inesperado.
                    throw new TokenInesperado(codigoFonte, indiceAbsoluto);
                }
                    
            }
        }
        System.out.println("execução com sucesso do programa.");
    }
}
