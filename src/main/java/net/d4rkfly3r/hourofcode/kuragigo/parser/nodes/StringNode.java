package net.d4rkfly3r.hourofcode.kuragigo.parser.nodes;

import net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens.StringToken;
import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;

public class StringNode implements Statement {
    private final StringToken stringToken;

    public StringNode(final StringToken stringToken) {
        this.stringToken = stringToken;
    }

    public StringToken getStringToken() {
        return this.stringToken;
    }

    public String getStringData() {
        return this.stringToken.getStringData();
    }

    @Override
    public String toString() {
        return "StringNode{" +
                "stringToken=" + this.stringToken +
                '}';
    }
}
