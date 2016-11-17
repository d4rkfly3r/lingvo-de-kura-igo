package net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens;

public class SymbolToken extends IdentificationToken {
    public static final SymbolToken EOF = new SymbolToken(Type.EOF);
    private final Type symbol;

    public SymbolToken(final Type symbol) {
        super(symbol, symbol.name());
        this.symbol = symbol;
    }

    public Type getSymbol() {
        return this.symbol;
    }

    @Override
    public String toString() {
        return "SymbolToken{" +
                "symbol=" + this.symbol +
                '}';
    }
}
