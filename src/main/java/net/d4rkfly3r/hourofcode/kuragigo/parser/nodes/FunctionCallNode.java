package net.d4rkfly3r.hourofcode.kuragigo.parser.nodes;

import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;

import java.util.List;

public class FunctionCallNode implements Statement {
    private final String functionName;
    private final List<Statement> parameters;

    public FunctionCallNode(final String functionName, final List<Statement> parameters) {
        this.functionName = functionName;
        this.parameters = parameters;
    }

    public List<Statement> getParameters() {
        return this.parameters;
    }

    public String getFunctionName() {
        return this.functionName;
    }

    @Override
    public String toString() {
        return "FunctionCallNode{" +
                "functionName='" + this.functionName + '\'' +
                ", parameters=" + this.parameters +
                '}';
    }
}
