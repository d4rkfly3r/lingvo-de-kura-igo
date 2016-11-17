package net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens;

public class NumberToken extends Token {
    private final Double numberData;

    public NumberToken(final Double numberData) {
        super(Type.NUMBER);
        this.numberData = numberData;
    }

    public Double getNumberData() {
        return this.numberData;
    }

    @Override
    public String toString() {
        return "NumberToken{" +
                "numberData=" + this.numberData +
                '}';
    }
}
