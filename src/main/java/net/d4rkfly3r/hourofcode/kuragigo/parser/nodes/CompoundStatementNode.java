package net.d4rkfly3r.hourofcode.kuragigo.parser.nodes;

import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;

import java.util.List;

public class CompoundStatementNode implements Statement {

    private final String functionName;
    private final ParameterNode parameterNode;
    private final List<Statement> statements;

    public CompoundStatementNode(final String functionName, final ParameterNode parameterNode, final List<Statement> statements) {
        this.functionName = functionName;
        this.parameterNode = parameterNode;
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return this.statements;
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public ParameterNode getParameterNode() {
        return this.parameterNode;
    }

    @Override
    public String toString() {
        return "CompoundStatementNode{" +
                "functionName='" + this.functionName + '\'' +
                ", parameterNode=" + this.parameterNode +
                ", statements=" + this.statements +
                '}';
    }
}
