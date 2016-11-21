package net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens;

import net.d4rkfly3r.hourofcode.kuragigo.parser.nodes.NumberNode;

public class NumberToken extends Token {
    private final Number numberData;

    public NumberToken(final Number numberData) {
        super(Type.NUMBER);
        this.numberData = numberData;
    }

    public Number getNumberData() {
        return this.numberData;
    }

    @Override
    public String toString() {
        return "NumberToken{" +
                "numberData=" + this.numberData +
                '}';
    }
}
