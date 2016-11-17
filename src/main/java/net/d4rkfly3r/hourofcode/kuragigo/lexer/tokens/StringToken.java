package net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens;

public class StringToken extends Token {
    private final String stringData;

    public StringToken(final String stringData) {
        super(Type.STRING);
        this.stringData = stringData;
    }

    public String getStringData() {
        return this.stringData;
    }

    @Override
    public String toString() {
        return "StringToken{" +
                "stringData='" + this.stringData + '\'' +
                '}';
    }
}
