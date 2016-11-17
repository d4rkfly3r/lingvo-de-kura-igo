package net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens;

public class IdentificationToken extends Token {

    private final String name;

    public IdentificationToken(final String name) {
        super(Type.IDENTIFICATION);
        this.name = name;
    }

    IdentificationToken(final Type symbol, final String name) {
        super(symbol);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "IdentificationToken{" +
                "name='" + this.name + '\'' +
                '}';
    }
}
