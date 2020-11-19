public class Main {
    public static void main(String[] args) {
        String input = "1+3";
        ExperimentoParser parser = new ExperimentoParser(input, 0);
        System.out.println(parser.exprecaoAlgebricaEntreInteiros());
    }
}
