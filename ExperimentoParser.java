public class ExperimentoParser {
    
    private String input;
    private int contLinha;
    
    public ExperimentoParser(String input, int contLinha) {
        setInput(input);
        this.contLinha = contLinha;
    }

    public void setInput(String input)
    {
        this.input = input;
    }

    // versao da funcao a ser chamada pelo main. chama a propria funcao com o valor inicial 0
    public int exprecaoAlgebricaEntreInteiros() {
        return exprecaoAlgebricaEntreInteiros(0);
    }

    // expressao generica entre inteiros. esperado qualquer tipo de expressao, terminada em ;
    private int exprecaoAlgebricaEntreInteiros(int numero) {
        System.out.println("Avaliando a str: " + input);
        int num = 0;
        int numDigitos = 1;
        // adiciona o num até que encontre um sinal
        while(!input.startsWith("+") && !input.startsWith("-") && !input.startsWith("/") && !input.startsWith("*") && !input.startsWith(";")) {
            num += ((int) input.charAt(0) * numDigitos);
            numDigitos++;
        }
        
        // remove a parte já avaliada da string
        input = input.substring(numDigitos, input.length());

        switch(input.charAt(0))
        {
            case '+':
                input = input.substring(1, input.length());
                return num + exprecaoAlgebricaEntreInteiros(num);
            case '-':
                input = input.substring(1, input.length());
                return num - exprecaoAlgebricaEntreInteiros(num);
            case '/':
                input = input.substring(1, input.length());
                return num / exprecaoAlgebricaEntreInteiros(num);
            case '*':
                input = input.substring(1, input.length());
                return num * exprecaoAlgebricaEntreInteiros(num);
            case ';':
                return num;
            default:
                System.out.println("Erro de sintaxe na linha " + contLinha + ", coluna " + contLinha + ".");
                return 0;

        }
    }
}