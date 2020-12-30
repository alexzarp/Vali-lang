import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

// ps: essa classe por hora não faz nada de útil. o código ainda está sob construção e é apenas um protótipo
public class Parser {

    ArrayList<String> linhas;

    // guarda as variaveis
    private Map<String, Variavel> variaveis;
    
    public Parser(String[] linhas) {
        this.linhas = formata(linhas);
        variaveis = new HashMap<String, Variavel>();
    }

    // retorna vetor de linhas pronta para serem avaliadas
    ArrayList<String> formata(String[] linhas) {

        ArrayList<String> linhasRetorno = new ArrayList<String>();

        for(String linha : linhas) {

            int indiceUltimaQuebra = 0,
                indiceCaractereAtual = 0;

            for(char caractere : linha.toCharArray()) {

                if(caractere == '{') {
                    linhasRetorno.add(linha.substring(indiceUltimaQuebra, indiceCaractereAtual));
                    indiceUltimaQuebra = indiceCaractereAtual;

                } else if(caractere == '}' || caractere == ';') {
                    linhasRetorno.add(linha.substring(indiceUltimaQuebra, indiceCaractereAtual + 1));
                    indiceUltimaQuebra = indiceCaractereAtual;
                }
                indiceCaractereAtual++;
            }
        }
        return linhasRetorno;
    }

    public void imprimeLinhasFormatadas() {
        for(String s : linhas) {
            System.out.println(s);
        }
    }
}
