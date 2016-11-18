package net.d4rkfly3r.hourofcode.kuragigo.parser.nodes;

import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;

public class AssignmentNode implements Statement {
    private final VariableNode variableNode;
    private final Statement statement;

    public AssignmentNode(final VariableNode variableNode, final Statement statement) {
        this.variableNode = variableNode;
        this.statement = statement;
    }

    public VariableNode getVariableNode() {
        return this.variableNode;
    }

    public Statement getStatement() {
        return this.statement;
    }

    @Override
    public String toString() {
        return "AssignmentNode{" +
                "variableNode=" + this.variableNode +
                ", statement=" + this.statement +
                '}';
    }
}
