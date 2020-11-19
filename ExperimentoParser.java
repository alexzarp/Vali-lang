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
    // por enquanto, apenas faz operacoes da esquerda para a direita
    private int exprecaoAlgebricaEntreInteiros(int numero) {
        int num = 0;
        int numDigitos = 1;

        // reconstrói o num até que encontre alguma coisa que não seja número
        while(Character.isDigit(input.charAt(0))) {
            num += Integer.parseInt(String.valueOf(input.charAt(0))) * numDigitos;
            input = input.substring(numDigitos, input.length());
            numDigitos++;
            
        }
        
        // remove a parte já avaliada da string

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