import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class Main {
    public static void main(String[] args) {

        String s = "23+1==23|1>23";
        Parser p = new Parser(s);
        
        try {
            // System.out.println(Integer.parseInt(Integer.valueOf(2).toString()));
            System.out.println(p.analiseElementar(0, s.length()));
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }
}
