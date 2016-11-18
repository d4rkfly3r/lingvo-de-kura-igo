package net.d4rkfly3r.hourofcode.kuragigo.parser.nodes;

import net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens.IdentificationToken;
import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;

public class VariableNode implements Statement {

    private final IdentificationToken token;

    public VariableNode(final IdentificationToken token) {

        this.token = token;
    }

    public IdentificationToken getToken() {
        return this.token;
    }

    public String getName() {
        return this.token.getName();
    }

    @Override
    public String toString() {
        return "VariableNode{" +
                "token=" + this.token +
                '}';
    }
}
