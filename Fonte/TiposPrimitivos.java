package Fonte;

// Decidimos que a sintaxe possível será em português.
public abstract class TiposPrimitivos { // Essa classe será abstrata porque nenhum programa usará ela diretamente.
    protected String intId;
    protected int intValor;

    protected String floatId;
    protected float floatValor; // Pode ser que nem cheguemos a usar float por ser de menor precisão.

    protected String charId;
    protected char charValor;

    protected String doubleId;
    protected double doubleValor;

    protected String booleanId;
    protected boolean boleanoValor;

    protected String stringId;
    protected String stringValor;

    /*------------------------------------------------------------------*/
    protected int getValorInt() { // Set e Get para o tipo inteiro.
        return this.intValor;
    }

    protected void setValorInt(int valor) {
        this.intValor = valor;
    }

    protected String getIdInt() {
        return this.intId;
    }

    protected void setIdInt(String id) {
        this.intId = id;
    }

    /*------------------------------------------------------------------*/
    protected float getValorFloat() { // Set e Get para o tipo float.
        return this.floatValor;
    }

    protected void setValorFloat(float valor) {
        this.floatValor = valor;
    }

    protected String getIdFloat() {
        return this.floatId;
    }

    protected void setIdFloar(String id) {
        this.floatId = id;
    }

    /*------------------------------------------------------------------*/
    protected char getValorChar() { // Ser e Get para o tipo char.
        return this.charValor;
    }

    protected void setValorChar(char valor) {
        this.charValor = valor;
    }

    protected String getIdChar() {
        return this.charId;
    }

    protected void setIdChar(String id) {
        this.charId = id;
    }

    /*------------------------------------------------------------------*/
    protected double getValorDouble() { // Set e Get para o tipo double.
        return this.doubleValor;
    }

    protected void setValorDouble(double valor) {
        this.doubleValor = valor;
    }

    protected String getIdDouble() {
        return this.doubleId;
    }

    protected void setIdDouble(String id) {
        this.doubleId = id;
    }

    /*------------------------------------------------------------------*/
    protected boolean getValorBoleano() { // Set e Get para o tipo boolean.
        return this.boleanoValor;
    }

    protected void setValorBoleano(boolean valor) {
        this.boleanoValor = valor;
    }

    protected String getIdBoleano() {
        return this.booleanId;
    }

    protected void setIdBoleano(String id) {
        this.booleanId = id;
    }

    /*------------------------------------------------------------------*/
    protected String getValorString() { // Set e Get para o tipo String.
        return this.stringValor;
    }

    protected void setValorString(String valor) {
        this.stringValor = valor;
    }

    protected String getIdString() {
        return this.stringId;
    }

    protected void setIdString(String id) {
        this.stringId = id;
    }
}
