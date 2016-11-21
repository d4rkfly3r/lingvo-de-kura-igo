package net.d4rkfly3r.hourofcode.kuragigo.parser;

import net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens.IdentificationToken;
import net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens.NumberToken;
import net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens.StringToken;
import net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens.Token;
import net.d4rkfly3r.hourofcode.kuragigo.parser.nodes.*;
import net.d4rkfly3r.hourofcode.kuragigo.parser.operations.BinaryOperation;
import net.d4rkfly3r.hourofcode.kuragigo.parser.operations.EmptyOperation;
import net.d4rkfly3r.hourofcode.kuragigo.parser.operations.UnaryOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Parser {
    private static final Statement EMPTY = new EmptyOperation();
    private final Token[] tokens;
    private int tokenIndex;
    private Token currentToken;

    public Parser(final Token[] tokens) {
        this.tokens = tokens;
        this.tokenIndex = 0;
        this.currentToken = this.tokens[this.tokenIndex];
    }

    void syntaxError(final String cause) {
        String stringBuilder = "\n" +
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + "\n" +
                this.tokens[this.tokenIndex - 1] +
                "Error at token: " + this.currentToken + "\n" +
                "Cause: " + (cause == null ? "Unknown!" : cause) + "\n" +
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + "\n";
        System.out.flush();
        System.err.flush();
        System.err.println(stringBuilder);
        System.err.flush();
        System.exit(1);
    }

    private void intake(final Token.Type tokenType) {
        if (this.currentToken.getTokenType() == tokenType) {
            this.currentToken = this.tokens[++this.tokenIndex];
        } else {
            this.syntaxError("Expected " + tokenType + " got " + this.currentToken.getTokenType());
        }
    }

    private Token peek() {
        if (this.tokens.length < this.tokenIndex + 1) {
            return null;
        }
        return this.tokens[this.tokenIndex + 1];
    }

    public Map<String, CompoundStatementNode> parse() {
        List<VariableDeclarationNode> declarationNodes = this.declarations();

//        Statement rootStatement = this.block();
        List<CompoundStatementNode> functions = new ArrayList<>();
        while (this.currentToken.getTokenType() != Token.Type.EOF) {
            functions.add(this.block());
        }
        if (this.currentToken.getTokenType() != Token.Type.EOF) {
            this.syntaxError("Expected end of file!");
        }

        return functions.stream().collect(Collectors.toMap(compoundStatementNode -> compoundStatementNode.getFunctionName(), compoundStatementNode -> compoundStatementNode));
    }

    private CompoundStatementNode block() {
        return this.compoundStatement();
    }

    private List<VariableDeclarationNode> declarations() {
        List<VariableDeclarationNode> declarationNodes = new ArrayList<>();
        while (this.currentToken.getTokenType().isVariableType()) {
            declarationNodes.addAll(this.variableDeclarations());
            this.intake(Token.Type.SEMICOLON);
        }


        return declarationNodes;
    }

    private List<VariableDeclarationNode> variableDeclarations() {
        List<VariableNode> variableNodes = new ArrayList<>();
        TypeNode typeNode = this.typeSpecification();
        variableNodes.add(new VariableNode((IdentificationToken) this.currentToken));
        this.intake(Token.Type.IDENTIFICATION);
        while (this.currentToken.getTokenType() == Token.Type.COMMA) {
            this.intake(Token.Type.COMMA);
            variableNodes.add(new VariableNode((IdentificationToken) this.currentToken));
            this.intake(Token.Type.IDENTIFICATION);
        }

        return variableNodes.stream().map(variableNode -> new VariableDeclarationNode(variableNode, typeNode)).collect(Collectors.toList());
    }

    private TypeNode typeSpecification() {
        Token token = this.currentToken;
        switch (this.currentToken.getTokenType()) {
            case NUMBER:
                this.intake(Token.Type.NUMBER);
                break;
            case STRING:
                this.intake(Token.Type.STRING);
                break;
        }
        return new TypeNode(token);
    }

    private CompoundStatementNode compoundStatement() {
        this.intake(Token.Type.FUNCTION);
        VariableNode variableNode = this.variable();
        String functionName = variableNode.getName();
        this.intake(Token.Type.OPEN_PARENTHESIS);
        ParameterNode parameterNode = this.parameters();
        this.intake(Token.Type.OPEN_CURLY_BRACE);
        List<Statement> statements = this.statementList();
        this.intake(Token.Type.CLOSE_CURLY_BRACE);

        return new CompoundStatementNode(functionName, parameterNode, statements);
    }

    private List<Statement> statementList() {
        Statement statement = this.statement();
        List<Statement> results = new ArrayList<>();
        results.add(statement);

        while (this.currentToken.getTokenType() == Token.Type.SEMICOLON) {
            this.intake(Token.Type.SEMICOLON); // Rearrange to fix loops
            final Statement statement1 = this.statement();
            results.add(statement1);
        }

        return results;
    }

    private Statement statement() {
        switch (this.currentToken.getTokenType()) {
            case FUNCTION:
                return this.compoundStatement();
            case IDENTIFICATION:
                if (this.peek().getTokenType() == Token.Type.ASSIGN) {
                    return this.assignmentStatement();
                } else if (this.peek().getTokenType() == Token.Type.OPEN_PARENTHESIS) {
                    return this.functionCallStatement();
                }
            case FOR:
                return this.forStatement();
        }
        return this.empty();
    }

    private ForLoopNode forStatement() {
        this.intake(Token.Type.FOR);
        this.intake(Token.Type.OPEN_PARENTHESIS);
        final AssignmentNode assignmentStatement = this.assignmentStatement();
        this.intake(Token.Type.SEMICOLON);
        final BinaryOperation comparisonNode = this.binaryStatement();
        this.intake(Token.Type.SEMICOLON);
        final AssignmentNode changeStatement = this.assignmentStatement();
        this.intake(Token.Type.CLOSE_PARENTHESIS);
        this.intake(Token.Type.OPEN_CURLY_BRACE);
        List<Statement> statements = new ArrayList<>();
        Statement statement;
        while (!(statement = this.statement()).equals(EMPTY)) {
            this.intake(Token.Type.SEMICOLON);
            statements.add(statement);
        }

        this.intake(Token.Type.CLOSE_CURLY_BRACE);
        return new ForLoopNode(assignmentStatement, comparisonNode, changeStatement, statements);
    }

    private BinaryOperation binaryStatement() {
        final Statement leftStatement = this.expression();
        final Token operator = this.currentToken;
        this.intake(operator.getTokenType());
        final Statement rightStatement = this.expression();
        return new BinaryOperation(leftStatement, operator, rightStatement);
    }

    private FunctionCallNode functionCallStatement() {
        final String functionName = this.variable().getName();
        List<Statement> parameters = new ArrayList<>();
        this.intake(Token.Type.OPEN_PARENTHESIS);
        if (this.currentToken.getTokenType() != Token.Type.CLOSE_PARENTHESIS) {
            boolean moreParams = true;
            while (moreParams) {
                parameters.add(this.expression());
                if (this.currentToken.getTokenType() == Token.Type.CLOSE_PARENTHESIS) {
                    moreParams = false;
                    this.intake(Token.Type.CLOSE_PARENTHESIS);
                } else {
                    this.intake(Token.Type.COMMA);
                }
            }
        } else {
            this.intake(Token.Type.CLOSE_PARENTHESIS);
        }
        return new FunctionCallNode(functionName, parameters);
    }

    private Statement empty() {
        return EMPTY;
    }

    private AssignmentNode assignmentStatement() {
        VariableNode leftNode = this.variable();
        this.intake(Token.Type.ASSIGN);
        Statement rightStatement = this.expression();
        return new AssignmentNode(leftNode, rightStatement);
    }


    private ParameterNode parameters() {
        List<VariableDeclarationNode> parameters = new ArrayList<>();
        if (this.currentToken.getTokenType() != Token.Type.CLOSE_PARENTHESIS) {
            boolean moreParams = true;
            while (moreParams) {
                final TypeNode typeNode = this.typeSpecification();
                final VariableNode variable = this.variable();
                parameters.add(new VariableDeclarationNode(variable, typeNode));
                if (this.currentToken.getTokenType() == Token.Type.CLOSE_PARENTHESIS) {
                    moreParams = false;
                    this.intake(Token.Type.CLOSE_PARENTHESIS);
                } else {
                    this.intake(Token.Type.COMMA);
                }
            }
        } else {
            this.intake(Token.Type.CLOSE_PARENTHESIS);
        }
        return new ParameterNode(parameters);

    }

    private VariableNode variable() {
        VariableNode variableNode = new VariableNode((IdentificationToken) this.currentToken);
        this.intake(Token.Type.IDENTIFICATION);
        return variableNode;
    }

    private Statement factor() {
        final Token token = this.currentToken;
        switch (token.getTokenType()) {
            case OPEN_PARENTHESIS:
                this.intake(Token.Type.OPEN_PARENTHESIS);
                final Statement result = this.expression();
                this.intake(Token.Type.CLOSE_PARENTHESIS);
                return result;
            case NUMBER:
                this.intake(Token.Type.NUMBER);
                return new NumberNode((NumberToken) token);
            case PLUS:
                this.intake(Token.Type.PLUS);
                return new UnaryOperation(token, this.factor());
            case MINUS:
                this.intake(Token.Type.MINUS);
                return new UnaryOperation(token, this.factor());
            case SQRT:
                this.intake(Token.Type.SQRT);
                return new UnaryOperation(token, this.factor());
            case STRING:
                this.intake(Token.Type.STRING);
                return new StringNode((StringToken) token);
        }
        return this.variable();
    }

    private Statement term() {
        Statement rootStatement = this.factor();
        while (this.currentToken.getTokenType().isPrimaryMathOperator()) {
            final Token operatorToken = this.currentToken;
            switch (this.currentToken.getTokenType()) {
                case MULTIPLY:
                    this.intake(Token.Type.MULTIPLY);
                    break;
                case DIVIDE:
                    this.intake(Token.Type.DIVIDE);
                    break;
                case MODULO:
                    this.intake(Token.Type.MODULO);
                    break;
                case POWER:
                    this.intake(Token.Type.POWER);
                    break;
            }

            rootStatement = new BinaryOperation(rootStatement, operatorToken, this.factor());
        }
        return rootStatement;
    }

    private Statement expression() {
        Statement rootStatement = this.term();

        while (this.currentToken.getTokenType().isSecondaryMathOperator()) {
            Token operatorToken = this.currentToken;
            switch (this.currentToken.getTokenType()) {
                case PLUS:
                    this.intake(Token.Type.PLUS);
                    break;
                case MINUS:
                    this.intake(Token.Type.MINUS);
                    break;
            }
            rootStatement = new BinaryOperation(rootStatement, operatorToken, this.term());
        }

        return rootStatement;
    }

}
