import java.io.File;
import java.util.Scanner;
public class Vali {
    public static void main(String[] args)
    {
        try {
            String[] lines = new String[2000];
            int amount = 0;
            File file = new File(args[0]);
            Scanner input = new Scanner(file);

            while (input.hasNextLine())
            {
                String line = input.nextLine();
                lines[amount++] = line;
            }

            input.close();

            for (int i = 0; i < amount; i++)
            {
                System.out.println("Linha " + i + ": " + lines[i]);
            }
            
        } catch (Exception e)
        {
            System.out.println("Nao foi possivel abrir o arquivo.vali.");
            System.out.println("Ele existe mesmo?");
            e.printStackTrace();
        }
    }
}