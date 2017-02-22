package net.d4rkfly3r.hourofcode.kuragigo.compiler;

import net.d4rkfly3r.hourofcode.kuragigo.Configuration;
import net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens.Token;
import net.d4rkfly3r.hourofcode.kuragigo.parser.Statement;
import net.d4rkfly3r.hourofcode.kuragigo.parser.nodes.*;
import net.d4rkfly3r.hourofcode.kuragigo.parser.operations.BinaryOperation;

import javax.tools.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Compiler {
    private final Map<String, ClassStatementNode> tree;
    private final File buildDir;
    private final String scriptName;

    public Compiler(final Map<String, ClassStatementNode> tree, final File buildDir, final String scriptName) {
        this.tree = tree;
        this.buildDir = buildDir;
        this.scriptName = scriptName.split("\\.", 2)[0];
        System.out.println(Configuration.GSON.toJson(tree));
    }

    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
        }
    }

    public void compile() {
        final File outputFolder = new File(buildDir, scriptName);
        outputFolder.mkdirs();

        System.out.println("Outputting Java files to: " + outputFolder);

        tree.values().parallelStream().forEach(classStatementNode -> {
            final String className = classStatementNode.getClassName();

            final File outputJavaFile = new File(outputFolder, className + ".java");
            try {
                outputJavaFile.createNewFile();

                try (final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputJavaFile))) {
                    bufferedWriter.write("package " + scriptName + ";");
                    bufferedWriter.write("import kuragigo.*;");
                    bufferedWriter.write("public class " + className + " {");

                    for (final CompoundStatementNode compoundStatementNode : classStatementNode.getFunctions().values()) {
                        final String functionName = compoundStatementNode.getFunctionName();
                        bufferedWriter.write("public void " + functionName + "(");
                        final List<VariableDeclarationNode> parameters = compoundStatementNode.getParameterNode().getParameters();
                        for (int i = 0; i < parameters.size(); i++) {
                            final VariableDeclarationNode variableDeclarationNode = parameters.get(i);
                            bufferedWriter.write(parseTokenTypeToString(variableDeclarationNode.getTypeNode().getToken().getTokenType()) + " " + variableDeclarationNode.getVariableNode().getName());
                            if (i + 1 < parameters.size()) {
                                bufferedWriter.write(", ");
                            }
                        }
                        bufferedWriter.write(") {");


                        for (final Statement statement : compoundStatementNode.getStatements()) {
                            parseStatement(statement, bufferedWriter);
                        }


                        bufferedWriter.write("}");
                    }

                    bufferedWriter.write("}");
                    bufferedWriter.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Running java compiler...");
        final String oldPath = System.setProperty("java.home", "C:\\Program Files\\Java\\jdk1.8.0_71");
        try {
            JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(null, Locale.ENGLISH, Charset.defaultCharset());
            final List<File> options = new ArrayList<>();
            options.add(new File("."));
            fileManager.setLocation(StandardLocation.CLASS_PATH, options);
            options.clear();
            options.add(buildDir.getAbsoluteFile());
            fileManager.setLocation(StandardLocation.SOURCE_PATH, options);
            final Iterable<? extends JavaFileObject> javaFileObjectsFromFiles = fileManager.getJavaFileObjectsFromFiles(tree.values().parallelStream().map(classStatementNode -> new File(new File(buildDir, scriptName), classStatementNode.getClassName() + ".java")).collect(Collectors.toList()));
            final JavaCompiler.CompilationTask task = javaCompiler.getTask(null, fileManager, null, null, null, javaFileObjectsFromFiles);
            System.out.println(task.call());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.setProperty("java.home", oldPath);


    }

    private void parseStatement(final Statement statement, final BufferedWriter bufferedWriter) throws IOException {
        if (statement instanceof FunctionCallNode) {
            final FunctionCallNode functionCallNode = (FunctionCallNode) statement;
            bufferedWriter.write(functionCallNode.getFunctionName() + "(");
            for (int i = 0; i < functionCallNode.getParameters().size(); i++) {
                final Statement statement1 = functionCallNode.getParameters().get(i);
                bufferedWriter.write(parseStatementToString(statement1));
                if (i + 1 < functionCallNode.getParameters().size()) {
                    bufferedWriter.write(", ");
                }
            }
            bufferedWriter.write(");");
        }
        if (statement instanceof ForLoopNode) {
            final ForLoopNode forLoopNode = (ForLoopNode) statement;
            bufferedWriter.write("for ( ");
            bufferedWriter.write("Double " + forLoopNode.getAssignmentNode().getVariableNode().getName() + " = " + parseStatementToString(forLoopNode.getAssignmentNode().getStatement()));
            bufferedWriter.write("; " + parseStatementToString(forLoopNode.getComparisonNode().getLeftStatement()) + parseTokenTypeToString(forLoopNode.getComparisonNode().getOperatorToken().getTokenType()) + parseStatementToString(forLoopNode.getComparisonNode().getRightStatement()));
            bufferedWriter.write("; " + forLoopNode.getChangeNode().getVariableNode().getName() + " = " + parseStatementToString(forLoopNode.getChangeNode().getStatement()));

            bufferedWriter.write(") {");

            for (Statement statement1 : forLoopNode.getStatements()) {
                this.parseStatement(statement1, bufferedWriter);
            }

            bufferedWriter.write("}");
        }
    }

    private String parseStatementToString(final Statement statement) {
        if (statement instanceof NumberNode) {
            return ((NumberNode) statement).getToken().getNumberData().toString();
        } else if (statement instanceof StringNode) {
            return "\"" + ((StringNode) statement).getStringData() + "\"";
        } else if (statement instanceof VariableNode) {
            return ((VariableNode) statement).getName();
        } else if (statement instanceof BinaryOperation) {
            final BinaryOperation binaryOperation = (BinaryOperation) statement;
            return parseStatementToString(binaryOperation.getLeftStatement()) + parseTokenTypeToString(binaryOperation.getOperatorToken().getTokenType()) + parseStatementToString(binaryOperation.getRightStatement());
        } else {
            System.out.println(statement);
        }
        return "";
    }

    private String parseTokenTypeToString(Token.Type variableDeclarationNode) {
        switch (variableDeclarationNode) {
            case NUMBER:
                return "Double";
            case STRING:
                return "String";
            case OPEN_ANGLE_BRACKET:
                return "<";
            case CLOSE_ANGLE_BRACKET:
                return ">";
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case MODULO:
                return "%";
            case MULTIPLY:
                return "*";
            case DIVIDE:
                return "/";

            default:
                return "!@!";
        }
    }
}
