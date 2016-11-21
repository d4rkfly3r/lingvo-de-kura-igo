package net.d4rkfly3r.hourofcode.kuragigo.parser.nodes;

import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;
import net.d4rkfly3r.hourofcode.kuragigo.parser.operations.BinaryOperation;

import java.util.List;

public class ForLoopNode implements Statement {

    private final AssignmentNode assignmentNode;
    private final BinaryOperation comparisonNode;
    private final AssignmentNode changeNode;
    private final List<Statement> statements;

    public ForLoopNode(final AssignmentNode assignmentNode, final BinaryOperation comparisonNode, final AssignmentNode changeNode, final List<Statement> statements) {
        this.assignmentNode = assignmentNode;
        this.comparisonNode = comparisonNode;
        this.changeNode = changeNode;
        this.statements = statements;
    }

    public AssignmentNode getAssignmentNode() {
        return this.assignmentNode;
    }

    public BinaryOperation getComparisonNode() {
        return this.comparisonNode;
    }

    public AssignmentNode getChangeNode() {
        return this.changeNode;
    }

    public List<Statement> getStatements() {
        return this.statements;
    }

    @Override
    public String toString() {
        return "ForLoopNode{" +
                "assignmentNode=" + this.assignmentNode +
                ", comparisonNode=" + this.comparisonNode +
                ", changeNode=" + this.changeNode +
                ", statements=" + this.statements +
                '}';
    }
}
