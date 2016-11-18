package net.d4rkfly3r.hourofcode.kuragigo.parser.operations;

import net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens.Token;
import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;

public class BinaryOperation implements Statement {
    private final Statement leftStatement;
    private final Token operatorToken;
    private final Statement rightStatement;

    public BinaryOperation(Statement leftStatement, Token operatorToken, Statement rightStatement) {
        this.leftStatement = leftStatement;
        this.operatorToken = operatorToken;
        this.rightStatement = rightStatement;
    }

    public Statement getLeftStatement() {
        return this.leftStatement;
    }

    public Token getOperatorToken() {
        return this.operatorToken;
    }

    public Statement getRightStatement() {
        return this.rightStatement;
    }

    @Override
    public String toString() {
        return "BinaryOperation{" +
                "leftStatement=" + this.leftStatement +
                ", operatorToken=" + this.operatorToken +
                ", rightStatement=" + this.rightStatement +
                '}';
    }
}
