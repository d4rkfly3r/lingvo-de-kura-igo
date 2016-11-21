package net.d4rkfly3r.hourofcode.kuragigo;

import net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens.Token;
import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;
import net.d4rkfly3r.hourofcode.kuragigo.parser.nodes.*;
import net.d4rkfly3r.hourofcode.kuragigo.parser.operations.BinaryOperation;
import net.d4rkfly3r.hourofcode.kuragigo.parser.operations.EmptyOperation;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Interpreter {

    private final HashMap<Token.Type, List<Overload>> overloads = new HashMap<>();
    //    private final HashMap<String, Object> GLOBAL_SCOPE = new HashMap<>();
    private final MethodHandles.Lookup lookup;
    private final Map<String, CompoundStatementNode> functions = new HashMap<>();
    private final Map<String, Function<Object[], Object>> javaFunctions = new HashMap<>();
    private HashMap<String, Object> currentScope = new HashMap<>();
    private Animator animator;

    {
        final List<Overload> multiplyList = new ArrayList<>();
        multiplyList.add(new Overload<Double, Double>(Double.class, Double.class, Double.class, (o, o2) -> o * o2));
        this.overloads.put(Token.Type.MULTIPLY, multiplyList);

        final List<Overload> addList = new ArrayList<>();
        addList.add(new Overload<Double, Double>(Double.class, Double.class, Double.class, (o, o2) -> o + o2));
        this.overloads.put(Token.Type.PLUS, addList);

        final List<Overload> subtractList = new ArrayList<>();
        subtractList.add(new Overload<Double, Double>(Double.class, Double.class, Double.class, (o, o2) -> o - o2));
        this.overloads.put(Token.Type.MINUS, subtractList);

        final List<Overload> divideList = new ArrayList<>();
        divideList.add(new Overload<Double, Double>(Double.class, Double.class, Double.class, (o, o2) -> o / o2));
        this.overloads.put(Token.Type.DIVIDE, divideList);

        final List<Overload> moduloList = new ArrayList<>();
        moduloList.add(new Overload<Double, Double>(Double.class, Double.class, Double.class, (o, o2) -> o % o2));
        this.overloads.put(Token.Type.MODULO, moduloList);

        final List<Overload> powerList = new ArrayList<>();
        powerList.add(new Overload<Double, Double>(Double.class, Double.class, Double.class, (o, o2) -> Math.pow(o, o2)));
        this.overloads.put(Token.Type.POWER, powerList);

        this.javaFunctions.put("createEntity", parameters -> {
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = this.visit((Statement) parameters[i]);
            }
            this.animator.createEntity((String) parameters[0], (String) parameters[1]);
            return null;
        });
        this.javaFunctions.put("wait", parameters -> {
            try {
                final Object visit = this.visit((Statement) parameters[0]);
                final long millis = ((Number) visit).longValue();
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });
        this.javaFunctions.put("moveEntity", parameters -> {
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = this.visit((Statement) parameters[i]);
            }
            this.animator.updateEntityX((String) parameters[0], ((Number) parameters[1]).intValue());
            this.animator.updateEntityY((String) parameters[0], ((Number) parameters[2]).intValue());
            return null;
        });
        this.javaFunctions.put("rotateEntity", parameters -> {
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = this.visit((Statement) parameters[i]);
            }
            this.animator.updateEntityRotation((String) parameters[0], ((Number) parameters[1]).intValue());
            return null;
        });
        this.javaFunctions.put("resizeEntity", parameters -> {
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = this.visit((Statement) parameters[i]);
            }
            this.animator.resizeEntity((String) parameters[0], ((Number) parameters[1]).intValue(), ((Number) parameters[2]).intValue());
            return null;
        });
    }

    public Interpreter(final Map<String, CompoundStatementNode> functions) {
        this.animator = new Animator();
        this.functions.putAll(functions);
        this.lookup = MethodHandles.lookup().in(Interpreter.class);
    }

    public void interpret() {
        this.visit(this.functions.get("main"));
        this.animator.close();
    }

    private Object visit(final Statement statement) {
        if (statement instanceof EmptyOperation) {
            return null;
        }
        try {
            MethodHandle methodHandle = this.lookup.findVirtual(Interpreter.class, "visit" + statement.getClass().getSimpleName(), MethodType.methodType(Object.class, statement.getClass()));
            return methodHandle.invoke(this, statement);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    private Object visitStringNode(final StringNode statement) {
        return statement.getStringData();
    }

    private Object visitVariableNode(final VariableNode variableNode) {
        String variableName = variableNode.getName();
        if (this.currentScope.containsKey(variableName)) {
            return this.visit((Statement) this.currentScope.get(variableName));
        } else {
            return null;
            //TODO Fancy error handling?
        }
    }

    private Object visitFunctionCallNode(final FunctionCallNode functionCallNode) {
        if (this.functions.containsKey(functionCallNode.getFunctionName())) {
            this.currentScope = new HashMap<>();
            final CompoundStatementNode statement = this.functions.get(functionCallNode.getFunctionName());
            for (int i = 0; i < functionCallNode.getParameters().size(); i++) {
                this.currentScope.put(statement.getParameterNode().getParameters().get(i).getVariableNode().getName(), functionCallNode.getParameters().get(i));
            }
            this.visit(statement);
        } else if (this.javaFunctions.containsKey(functionCallNode.getFunctionName())) {
            this.javaFunctions.get(functionCallNode.getFunctionName()).apply(functionCallNode.getParameters().toArray());
        } else {
            throw new RuntimeException("No such function exists!");
        }
        return null;
    }

    private Object visitCompoundStatementNode(final CompoundStatementNode compoundStatementNode) {
        compoundStatementNode.getStatements().forEach(this::visit);
        return null;
    }

    private Object visitAssignmentNode(final AssignmentNode assignNode) {
        String variableName = assignNode.getVariableNode().getName();
        final Object visit = this.visit(assignNode.getStatement());
        this.currentScope.put(variableName, visit);
        return null;
    }

    private Object visitNumberNode(final NumberNode numberNode) {
        return numberNode.getToken().getNumberData();
    }

    private Object visitBinaryOperation(final BinaryOperation binaryOperation) {
        final Token.Type tokenType = binaryOperation.getOperatorToken().getTokenType();
        Object leftStatement = binaryOperation.getLeftStatement();
        Object rightStatement = binaryOperation.getRightStatement();
        if (this.overloads.containsKey(tokenType)) {
            if (leftStatement instanceof VariableNode) {
                leftStatement = this.currentScope.get(((VariableNode) leftStatement).getName());
            }
            if (rightStatement instanceof VariableNode) {
                rightStatement = this.currentScope.get(((VariableNode) rightStatement).getName());
            }
            final Object finalLeftStatement = leftStatement;
            final Object finalRightStatement = rightStatement;
            Optional<Overload> optOverload = this.overloads.get(tokenType)
                    .stream()
                    .filter(overload -> overload.getObjectOneType().equals(finalLeftStatement.getClass()) && overload.getObjectTwoType().equals(finalRightStatement.getClass()))
                    .findFirst();
            return optOverload.isPresent() ? optOverload.get().transformer.apply(leftStatement, rightStatement) : null;
        }

        return null;
    }

    private static final class Overload<T1, T2> {
        private final Class<?> objectOneType, objectTwoType, returnType;
        private final BiFunction<T1, T2, Object> transformer;

        private Overload(Class<?> objectOneType, Class<?> objectTwoType, Class<?> returnType, BiFunction<T1, T2, Object> transformer) {
            this.objectOneType = objectOneType;
            this.objectTwoType = objectTwoType;
            this.returnType = returnType;
            this.transformer = transformer;
        }

        Class<?> getObjectOneType() {
            return this.objectOneType;
        }

        Class<?> getObjectTwoType() {
            return this.objectTwoType;
        }

        public Class<?> getReturnType() {
            return this.returnType;
        }

        public BiFunction<T1, T2, Object> getTransformer() {
            return this.transformer;
        }
    }

}
