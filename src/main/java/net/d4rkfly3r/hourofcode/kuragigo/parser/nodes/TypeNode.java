package net.d4rkfly3r.hourofcode.kuragigo.parser.nodes;

import net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens.Token;
import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;

public class TypeNode implements Statement {
    private final Token token;

    public TypeNode(final Token token) {
        this.token = token;
    }

    public Token getToken() {
        return this.token;
    }

    @Override
    public String toString() {
        return "TypeNode{" +
                "token=" + this.token +
                '}';
    }
}
