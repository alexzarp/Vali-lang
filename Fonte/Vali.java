package Fonte;

import java.io.File;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import Fonte.AnalizadorDeTexto;

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
                
                // formata codigo fonte para facilitar interpretacao
                linha = Ferramentas.removeEspaco(linha);
                
                // ignore linhas de comentário
                // TODO verificar se o comentario realmente começa com //
                if(!linha.startsWith("//"))
                    linhas.add(linha);
            }
            input.close();

            for (int i = 0; i < contadorDeLinhas; i++) {
                //Como o professor disse, toda análise será dentro desse for,
                //depois até podemos criar uma classe externa que faça isso e seja
                //chamad aqui.
                /*AnalizadorDeTexto analiza = new AnalizadorDeTexto();
                analiza.analizaIf();*/
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
}
