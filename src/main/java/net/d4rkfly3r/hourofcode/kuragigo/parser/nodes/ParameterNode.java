package net.d4rkfly3r.hourofcode.kuragigo.parser.nodes;

import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;

import java.util.List;

public class ParameterNode implements Statement {
    private final List<VariableDeclarationNode> parameters;

    public ParameterNode(final List<VariableDeclarationNode> parameters) {

        this.parameters = parameters;
    }

    public List<VariableDeclarationNode> getParameters() {
        return this.parameters;
    }

    @Override
    public String toString() {
        return "ParameterNode{" +
                "parameters=" + this.parameters +
                '}';
    }
}
