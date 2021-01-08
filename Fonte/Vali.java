import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Vali {
    public static void main(String[] args) {
        try {
            if(args[0].endsWith(".vali")) {

                // lê o arquivo como uma String.
                Parser p = new Parser(Files.readString(Paths.get(args[0]), StandardCharsets.US_ASCII));
                p.analisa();

            } else
                System.out.println("Arquivo .vali não encontrado.");

        } catch(Erro e) {
            e.printaLinhaFormatada();
            System.out.println(e.toString());

          // erros não esperados pelo interpretador.
        } catch(Exception e) {
            System.out.println(e.toString());
            System.out.println(e.getMessage());
        }
    }
}
