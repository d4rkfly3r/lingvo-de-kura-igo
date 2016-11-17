package net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens;

public class OperatorToken extends Token {
    private final Type operator;

    public OperatorToken(final Type operator) {
        super(operator);
        this.operator = operator;
    }

    public Type getOperator() {
        return this.operator;
    }

    @Override
    public String toString() {
        return "OperatorToken{" +
                "operator=" + this.operator +
                '}';
    }
}
