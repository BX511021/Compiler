package Token;

import Node.SYMBOL;

public class Token {
    private SYMBOL symbol;
    private String value;
    private Integer line;

    public Token(SYMBOL symbol, String value, Integer line) {
        this.symbol = symbol;
        this.value = value;
        this.line = line;
    }

    public Integer getLine() {
        return line;
    }

    public String getValue() {
        return value;
    }

    public SYMBOL getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return this.line + ": " +
                this.symbol + " " + this.value;
    }
}
