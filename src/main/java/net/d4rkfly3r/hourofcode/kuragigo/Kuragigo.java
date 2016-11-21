package net.d4rkfly3r.hourofcode.kuragigo;

import net.d4rkfly3r.hourofcode.kuragigo.lexer.Lexer;
import net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens.SymbolToken;
import net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens.Token;
import net.d4rkfly3r.hourofcode.kuragigo.parser.Parser;
import net.d4rkfly3r.hourofcode.kuragigo.parser.nodes.CompoundStatementNode;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Kuragigo {

    private final Scanner scanner;
    private boolean running;
    private File buildDir;
    private long lastTime = 0L;
    private String scriptName; // Case Sensitive I think!

    private Kuragigo() {
        this.scanner = new Scanner(System.in);
    }

    public static void main(final String[] args) {

        final Kuragigo kuragigo = new Kuragigo();
        kuragigo.setup();
        while (kuragigo.isRunning()) {
            kuragigo.handleUserInput();
        }
        kuragigo.cleanup();
    }

    private void cleanup() {
        System.out.println("Cleaning up...");
        System.out.println();
        this.scanner.close();
        if (Configuration.CLEANUP_BUILD_FOLDER) {
            if (this.buildDir.exists()) {
                this.buildDir.deleteOnExit();
            }
        }
        System.out.println("Goodbye...");
    }

    private void handleUserInput() {
        this.scriptName = null;
        System.out.println("Please enter `file`, `input`, or `quit` to signify your action.");
        final String userInput = this.scanner.nextLine().toLowerCase();
        switch (userInput) {
            case "file":
                this.runScriptFromFile();
                break;
            case "input":
                this.runScriptFromInput();
                break;
            case "quit":
                this.running = false;
                break;
            default:
                System.out.println("Invalid Input!");
                System.out.println();
                this.handleUserInput();
                return;
        }
        System.out.println();
        System.out.println();
        System.out.println();
    }

    private void runScriptFromInput() {
        System.out.println("Please enter the code. Enter an `@` symbol on its own line to signify the end of the script!");
        String code = "";
        String line;
        while (!(line = this.scanner.nextLine()).equalsIgnoreCase("@")) {
            code += line;
        }
        this.handleCode(code);
    }

    private void runScriptFromFile() {
        System.out.println("Please enter `raw` or `compiled`:");
        final String userInput = this.scanner.nextLine();
        if (userInput.equalsIgnoreCase("raw")) {
            System.out.println("Please enter the file name, excluding the extension:");
            this.scriptName = this.scanner.nextLine() + ".kuragigo";
            final File scriptFile = new File(Configuration.SCRIPTS_FOLDER_FILE, this.scriptName);
            if (!scriptFile.exists()) {
                System.out.println("Invalid Script Name!\n");
                this.runScriptFromFile();
                return;
            }

            String code;
            try {
                byte[] data;
                try (final FileInputStream fis = new FileInputStream(scriptFile)) {
                    data = new byte[(int) scriptFile.length()];
                    fis.read(data);
                    fis.close();
                }

                code = new String(data, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            this.handleCode(code);

        } else if (userInput.equalsIgnoreCase("compiled")) {
            System.out.println("Please enter the file name, excluding the extension:");
            this.scriptName = this.scanner.nextLine() + ".kuragigo";

            Map<String, CompoundStatementNode> tree = null;
            try (FileInputStream fis = new FileInputStream(new File(this.buildDir, this.scriptName + ".konstruu")); ObjectInputStream ois = new ObjectInputStream(fis)) {
                tree = (Map<String, CompoundStatementNode>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            this.handleTree(tree);

        } else {
            System.out.println("Invalid Input!\n");
            this.runScriptFromFile();
        }
    }

    private void handleCode(final String code) {
        this.lastTime = System.currentTimeMillis();
        final Lexer lexer = new Lexer(code);
        final ArrayList<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = lexer.getNextToken()) != SymbolToken.EOF) {
            tokens.add(token);
            System.out.println(token);
        }
        tokens.add(SymbolToken.EOF);
        this.handleTokens(tokens.toArray(new Token[tokens.size()]));
    }

    private void handleTokens(final Token[] tokens) {
        if (Configuration.DEBUG) {
            for (Token token : tokens) {
                System.out.println(token);
            }
        }
        Parser parser = new Parser(tokens);
        final Map<String, CompoundStatementNode> tree = parser.parse();
        System.out.println("Compile and parsing took: " + (System.currentTimeMillis() - this.lastTime) + " milliseconds.");
        this.saveCompiled(tree);
        this.handleTree(tree);
    }

    private void handleTree(Map<String, CompoundStatementNode> tree) {
        Interpreter interpreter = new Interpreter(tree);
        interpreter.interpret();
    }

    private void saveCompiled(Map<String, CompoundStatementNode> tree) {
        if (this.scriptName != null) {
            try (FileOutputStream fos = new FileOutputStream(new File(this.buildDir, this.scriptName + ".konstruu")); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(tree);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setup() {
        this.running = true;
        if (!Configuration.SCRIPTS_FOLDER_FILE.exists()) {
            if (Configuration.SCRIPTS_FOLDER_FILE.mkdirs()) {
                System.out.println("Scripts Folder Successfully Created!");
            } else {
                System.out.println("Scripts Folder Was NOT Created!");
            }
        }
        this.buildDir = new File(Configuration.SCRIPTS_FOLDER_FILE, "build");
        if (!this.buildDir.exists()) {
            if (this.buildDir.mkdirs()) {
                System.out.println("Scripts Build Folder Successfully Created!");
            } else {
                System.out.println("Scripts Build Folder Was NOT Created!");
            }
        }
    }

    private boolean isRunning() {
        return this.running;
    }

    public void setRunning(final boolean running) {
        this.running = running;
    }
}
