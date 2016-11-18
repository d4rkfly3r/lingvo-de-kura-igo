package net.d4rkfly3r.hourofcode.kuragigo.parser.nodes;

import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;

import java.util.List;

public class BlockNode implements Statement {

    private final List<VariableDeclarationNode> declarationList;
    private final CompoundStatementNode compoundStatementNode;

    public BlockNode(final List<VariableDeclarationNode> declarationList, final CompoundStatementNode compoundStatementNode) {
        this.declarationList = declarationList;
        this.compoundStatementNode = compoundStatementNode;
    }

    public List<VariableDeclarationNode> getDeclarationList() {
        return this.declarationList;
    }

    public CompoundStatementNode getCompoundStatementNode() {
        return this.compoundStatementNode;
    }

    @Override
    public String toString() {
        return "BlockNode{" +
                "declarationList=" + this.declarationList +
                ", compoundStatementNode=" + this.compoundStatementNode +
                '}';
    }
}
