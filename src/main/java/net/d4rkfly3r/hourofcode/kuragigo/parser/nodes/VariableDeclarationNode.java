package net.d4rkfly3r.hourofcode.kuragigo.parser.nodes;

import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;

public class VariableDeclarationNode implements Statement {


    private final VariableNode variableNode;
    private final TypeNode typeNode;

    public VariableDeclarationNode(final VariableNode variableNode, final TypeNode typeNode) {
        this.variableNode = variableNode;
        this.typeNode = typeNode;
    }

    public VariableNode getVariableNode() {
        return this.variableNode;
    }

    public TypeNode getTypeNode() {
        return this.typeNode;
    }

    @Override
    public String toString() {
        return "VariableDeclarationNode{" +
                "variableNode=" + this.variableNode +
                ", typeNode=" + this.typeNode +
                '}';
    }
}
