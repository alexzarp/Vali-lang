import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Vali {
    public static String codigoFonte;
    public static void main(String[] args) {
        try {
            if(args[0].endsWith(".vali")) {
                Scanner s = new Scanner(Paths.get(args[0]));
                //String codigoFonte = "";

                while(s.hasNextLine()) {
                    codigoFonte += s.nextLine();
                    codigoFonte += "\n";
                }
                    
                // lê o arquivo como uma String.
                Parser p = new Parser(codigoFonte);
                // Parser p = new Parser("inteiro a = 2;\nflutuante b = a;");
                // Parser p = new Parser(Files.readString(Paths.get(args[0]), StandardCharsets.US_ASCII));
                p.analisa();
                Variavel.imprimeVariaveis();
            } else
                System.out.println("Arquivo .vali não encontrado.");

        } catch(Erro e) {
            e.printaLinhaFormatada();
            System.out.println(e.toString());

          // erros não esperados pelo interpretador.
        } 
        catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
}
