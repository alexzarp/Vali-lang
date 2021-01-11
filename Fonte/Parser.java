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
    /*AtribuicaoVariavel atribuicao = new AtribuicaoVariavel();
    Tools tools = new Tools();
    Condicoes condicoes = new Condicoes();*/
            


    // codigoFonte guarda todo o código fonte em uma única string.
    private static String codigoFonte;
    public static String getCodigoFonte () {
        return codigoFonte;
    }// ← isso aqui não era um problema
    // caso precisemos tratar de alguma forma o código fonte. possivelmente
    // descartável!
    public static void setCodigoFonte(String codigoFontee) {
        codigoFonte = codigoFontee;
    }
    public static void sumCodigoFonte (String sum) {
        codigoFonte += sum;
    }




    // contadores de caracteres. utilizaremos estes valores para formatar
    // a mensagem de erro em caso de erro, como se fossem cursores.
    private static int indiceAbsoluto; // número total de caracteres lidos, incluindo \n.
    public static int getIndiceAbsoluto () {
        return indiceAbsoluto;
    }
    public static void setIndiceAbsoluto (int indiceAbsolutoo) {
        indiceAbsoluto = indiceAbsolutoo;
    }
    public static void sumIndiceAbsoluto (int sum) {
        indiceAbsoluto += sum;
    }
    //---------------------------------------------------------------------------------------
    private static int comprimentoDoPrograma;
    public static int getComprimentoDoPrograma () {
        return comprimentoDoPrograma;
    }
    public static void setComprimentoDoPrograma (int comprimentoDoProgramaa) {
        comprimentoDoPrograma = comprimentoDoProgramaa;
    }
    public static void sumComprimentoDoPrograma (int sum) {
        comprimentoDoPrograma += sum;
    }
   


    public Parser(String codigoFonte) {
        setCodigoFonte(codigoFonte);
        setIndiceAbsoluto(0);
        //this.indiceAbsoluto = 0;
        setComprimentoDoPrograma(codigoFonte.length());
        //this.comprimentoDoPrograma = codigoFonte.length();
    }

    public void analisa() throws Erro {
        Variavel.novoEscopo();
        escopo.resolveCorpo();
    }

    // TODO implementar isso.
    private void verificaAtribuicaoFuncao() throws Erro {
    }

}
