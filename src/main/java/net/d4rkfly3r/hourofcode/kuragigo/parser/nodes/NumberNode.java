package net.d4rkfly3r.hourofcode.kuragigo.parser.nodes;

import net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens.NumberToken;
import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;

public class NumberNode implements Statement {
    private final NumberToken token;

    public NumberNode(final NumberToken token) {
        this.token = token;
    }

    public NumberToken getToken() {
        return this.token;
    }

    @Override
    public String toString() {
        return "NumberNode{" +
                "token=" + this.token +
                '}';
    }
}
