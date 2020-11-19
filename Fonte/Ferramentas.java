package Fonte;

public class Ferramentas {
    public static void removeEspaco(String str)
    {
        StringBuilder strBuilder = new StringBuilder(); // criado um StringBuilder para poder remover os caracteres especificos
        strBuilder.append(str);
        boolean ehString = false;

        for(int i = 0; i < strBuilder.length(); i++) {
            if(str.charAt(i) == '\"')
                ehString = !ehString;
            if(!ehString && str.charAt(i) == ' ')
                strBuilder.deleteCharAt(i);
        }

        str = strBuilder.toString();
    }
}
