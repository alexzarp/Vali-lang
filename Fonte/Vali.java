/*
Este é um interpretador de texto capaz de ler uma sintaxe própria como linguagem de programação, compilá-la e rodá-la
usando o Java. Tem como objetivo compor uma nota na disciplina de Programação I no Curso de Ciência na UFFS.

Autores desse programa: Alex Sandro Zarpelon, alexszds@gmail.com
                        Bruna Gabriela Disner, bruna.disner@gmail.com
                        Patrícia Trevisan, patitrev@gmail.com
                        Rafael Fernandes, rflgf0@gmail.com
*/

import java.nio.file.Paths;
import java.util.Scanner;

public class Vali {

    public static void main(String[] args) {
        try {
            if(args[0].endsWith(".vali")) {
                Scanner s = new Scanner(Paths.get(args[0]));
                String codigoFonte = "";

                while(s.hasNextLine()) {
                    codigoFonte += s.nextLine();
                    codigoFonte += "\n";
                }
                
                // lê o arquivo como uma String.
                Parser p = new Parser(codigoFonte);
                p.analisa();
                Variavel.imprimeVariaveis();
            } else
                System.out.println("Arquivo .vali não encontrado.");

        } catch(Erro e) {
            e.printaLinhaFormatada();
            System.out.println(e.toString());

        } 
        // erros não esperados pelo interpretador.
        catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
}
