package net.d4rkfly3r.hourofcode.kuragigo.parser.operations;

import net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens.Token;
import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;

public class UnaryOperation implements Statement {
    private final Token operatorToken;
    private final Statement expression;

    public UnaryOperation(final Token operatorToken, final Statement expression) {
        this.operatorToken = operatorToken;
        this.expression = expression;
    }

    public Token getOperatorToken() {
        return this.operatorToken;
    }

    public Statement getExpression() {
        return this.expression;
    }

    @Override
    public String
    toString() {
        return "UnaryOperation{" +
                "operatorToken=" + this.operatorToken +
                ", expression=" + this.expression +
                '}';
    }
}
