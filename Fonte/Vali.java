import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Vali {
    public static void main(String[] args) {
        try {
            if(args[0].endsWith(".vali")) {
                Scanner s = new Scanner(Paths.get(args[0]));
                //String Parser.codigoFonte = "";

                while(s.hasNextLine()) {
                    // Aqui eu criei um setter que soma, isso é ruim?
                    //Parser.codigoFonte += s.nextLine();
                    Parser.sumCodigoFonte(s.nextLine());
                    //Parser.codigoFonte += "\n";
                    Parser.sumCodigoFonte("\n");
                }
                    
                // lê o arquivo como uma String.
                Parser p = new Parser(Parser.getCodigoFonte());
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
