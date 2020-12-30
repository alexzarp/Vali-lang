public class Main {
    public static void main(String[] args) {
        String v[] = {"alaal(){ad}", "adasdad;;d} ", "{ adasdad;"};
        Parser p = new Parser(v);
        p.imprimeLinhasFormatadas();
    }
    
}
