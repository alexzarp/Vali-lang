import java.io.File;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
public class Vali {
    public static void main(String[] args)
    {
        boolean exe = false;
        try {
            List<String> linhas = new ArrayList<String>();
            int contadorDeLinhas = 0;
            File arquivo = new File(args[0]);
            if(!args[0].endsWith(".vali")) {
                exe = true;
            }
            Scanner input = new Scanner(arquivo);

            while (input.hasNextLine()) {
                String linha = input.nextLine();
                contadorDeLinhas++;
                linhas.add(linha);
            }

            input.close();

            for (int i = 0; i < contadorDeLinhas; i++) {
//              System.out.println("Linha " + i + ": " + lines[i]);
                
            	if (!linha[i].equals(' ')) {
                    
                }

                
            }
            
        } catch (Exception e) { 
            if (exe) {
                System.out.println("Arquivo .vali não encontrado.");
            }

            else {
                System.out.println("Nao foi possivel abrir o arquivo.vali " + args[0] + ".");
                System.out.println("Ele existe mesmo?");
                e.printStackTrace();
            }
        }
    }
    private void removeEspaco(String str)
    {
        boolean ehString = false;

        for(int i = 0; i < str.length(); i++)
            if(str.charAt(i) == '\"')
                ehString = !ehString;
            if(!ehString && str.charAt(i) == ' ')
                



    }
}