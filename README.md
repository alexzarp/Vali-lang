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

Qualquer soma de tipos numérico deve ser feita antes de usar:
```
imprime();
```

|||
:-:|:-:
a = a + b; | imprime(a);

A linguagem não é Case Sensitive, você pode colocar espaços como quiser.


## **Tipos de variável:**
Tipos | Exemplo
:------:|:--------:
inteiro | 0
flutuante | 0.0
boleano | verdadeiro ou falso
palavra | "abc"


## **Sinais aritiméticos:**
Operador | Operação
:---------:|:----------:
\+ | Adição ou concatenação em caso do tipo palavra
\- | Subtração
/ | Divisão
\* | Multiplicação


## **Sinais condicionais:**
Sinal | Ação
:-----:|:--------:
== | Compara se valores são iguais
\> | Compara se o primeiro valor é maior que o segundo
< | Compara se o primeiro valor é menor que o segundo
<= | Compara se o primeiro valor é menor ou igual ao segundo
\>= | Compara se o primeiro valor é maior ou igual ao segundo
!= | Compara se os valores são diferentes
& | Compara se o primeiro e segundo valores são vardadeiros
ǀ | Compara se o primeiro ou o segundo valores são verdadeiros

Não funciona com palavra e binario

## **Declaração de variável:**
```
tipo variavel = valor;
```
|Declarações e valores iniciais|
:--:
inteiro varivel = 0;
flutuante variavel2 = 0.0;
boleano variavel3 = falso;
palavra variavel4 = "Olá mundo!";