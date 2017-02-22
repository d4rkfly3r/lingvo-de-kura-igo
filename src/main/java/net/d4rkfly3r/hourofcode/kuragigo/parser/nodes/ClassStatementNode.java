package net.d4rkfly3r.hourofcode.kuragigo.parser.nodes;

import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;

import java.util.Map;

public class ClassStatementNode implements Statement {
    private final String className;
    private final Map<String, CompoundStatementNode> functions;

    public ClassStatementNode(final String className, final Map<String, CompoundStatementNode> functions) {

        this.className = className;
        this.functions = functions;
    }

    public Map<String, CompoundStatementNode> getFunctions() {
        return functions;
    }

    public String getClassName() {
        return className;
    }
}
