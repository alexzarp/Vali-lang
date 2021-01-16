![Imagem Logo](https://github.com/alexzarp/Vali-lang/blob/main/Utilidades/logo.png?raw=true)

#### Esta é a linguagem Vali-lang, produzida pelos alunos <a href="https://github.com/alexzarp">Alex Sandro Zarpelon</a>(<a href="mailto:alexszds@gmail.com">alexszds@gmail.com</a>), <a href="https://github.com/Brunadisner">Bruna Gabriela Disner</a>(<a href="mailto:bruna.disner@gmail.com">bruna.disner@gmail.com</a>), <a href="https://github.com/patitrev">Patrícia Trevisan</a>(<a href="mailto:patitrev@gmail.com">patitrev@gmail.com</a>) e <a href="https://github.com/rflgf">Rafael Fernandes</a>(<a href="mailto:rflgf0@gmail.com">rflgf0@gmail.com</a>).


## **Como rodar**
```
java -jar Vali.jar nomedoarquivo.vali
```
Necessita ter instalado Java JDK na máquina


## **A sintaxe funciona de forma que:**
Elemento | Exemplo
:------:|:--------:
se, equivalente a if | se(a < b) {faça;}
senao, equilente ao else | senao {faça;}
enquanto, equivalente a while | enquanto(a > b) {faça;}
imprime, equivalente a print | imprime("Olá mundo" + a); → "+" concatena elementos de qualquer tipo
tipo variavel = valor ou vazio; | inteiro num = 1;


Salvo caracteres dentro de aspas, a linguagem não é sensível aos caracteres quebra de linha, espaço e tab. Você pode formatar o código fonte com estes como quiser (porém outros caracteres como carriage return podem causar erros).


## **Tipos de variável:**
Tipos | Exemplo
:------:|:--------:
inteiro | 0
flutuante | 0.0
boleano | verdadeiro ou falso
palavra | "abc"

Conversões de tipos podem ser feitas implicitamente entre tipos específicos. Não há conversão explícita de tipos.
<br>
* Uma atribuição de variável do tipo ``flutuante`` pode envolver variáveis ``flutuante`` e ``inteiro`` em sua expressão. Mas a recíproca não é válida! Exemplo:

 ``
inteiro a = 2;
flutuante b = a + 2;
 ``

 é um código válido. Entretanto,

 ``
flutuante a = 2;
inteiro b = a + 2;
 ``

 resultará em um erro de compilação.

* Uma atribuição de tipo ``palavra`` pode utilizar variáveis de todos os outros tipos. **Entretanto, operações devem ser feitas previamente e salvadas em uma variável a parte.** Exemplo:

 ``flutuante a = 2 + 3; palavra b = "Isso será computado como 5: " + a;``

 é válido e ``b`` receberá o valor "Isso será computado como 5: 5". Entretanto,

 ``palavra b = "Isso não será computado como 5: " + 2 + 5;``

 também é válido, mas ``b`` guardará o valor "Isso não será computado como 5: 25". Em outras palavras, a única operação possível na atribuição de variáveis do tipo ``palavra`` é a concatenação de valores do tipo ``palavra``, e quaisquer outros valores, sejam literais ou armazenados em variávels, serão avaliados como tal. **Isto também é válido para o parâmetro do método ``imprime()``.** Exemplo:

 ``inteiro a = 2 + 2; imprime(a)``

 imprimirá "4" no terminal, enquanto

 ``imprime(2+2)``

 imprimirá "22".
 
 * Variáveis podem ser instanciadas e inicializadas em momentos diferentes. Entretanto, tentativas de leitura de variáveis não inicializadas causarão erros. Exemplos:
  `` inteiro a; a = 3; ``
  é válido. Entretanto,
  ``inteiro a; inteiro b = a + 1;``
  resultará em erro de compilação.

## **Sinais aritiméticos:**
Operador | Operação
:---------:|:----------:
\+ | Adição ou concatenação em caso do tipo ``palavra``
\- | Subtração
/ | Divisão
\* | Multiplicação

Utilize inteiro para divisões inteiras e flutuante para resultados racionais.


## **Sinais comparativos:**
Sinal | Ação
:-----:|:--------:
== | Compara se valores são iguais
\> | Compara se o primeiro valor é maior que o segundo
< | Compara se o primeiro valor é menor que o segundo
<= | Compara se o primeiro valor é menor ou igual ao segundo
\>= | Compara se o primeiro valor é maior ou igual ao segundo
!= | Compara se os valores são diferentes

Funcionam apenas entre valores do tipo ``inteiro`` e ``flutuante``.

## **Sinais condicionais:**
Sinal | Ação
:-----:|:--------:
& | Compara se o primeiro e segundo valores são verdadeiros
ǀ | Compara se o primeiro ou o segundo valores são verdadeiros

Funcionam apenas entre valores do tipo binario. Exemplos:

* ``binario a = 2 < 3 | 4 != 1;``
* ``se(verdadeiro & 2>3)``


## **Sinal unário:**
Sinal | Ação
:-----:|:--------:
! | Inverte o valor de um ``binario``. Deve ser utilizado como prefixo.

Exemplos:
* ``binario a = !2>3;``
* ``se(!falso)``

## **Declaração de variável:**
``
tipo variavel = valor;
``
|Declarações e valores iniciais|
:--:
inteiro varivel = 0;
flutuante variavel2 = 0.0;
binario variavel3 = falso;
palavra variavel4 = "Olá mundo!";
