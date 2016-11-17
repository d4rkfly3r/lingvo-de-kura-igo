package net.d4rkfly3r.hourofcode.kuragigo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Kuragigo {

    private final Scanner scanner;
    private boolean running;
    private File buildDir;

    public Kuragigo() {
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
        System.out.println("Goodbye...");
    }

    private void handleUserInput() {
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
            final String scriptName = this.scanner.nextLine(); // Case Sensitive I think!
            final File scriptFile = new File(Configuration.SCRIPTS_FOLDER_FILE, scriptName);
            if (!scriptFile.exists()) {
                System.out.println("Invalid Script Name!\n");
                this.runScriptFromFile();
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

        } else {
            System.out.println("Invalid Input!\n");
            this.runScriptFromFile();
        }

    }

    private void handleCode(final String code) {
        System.out.println(code);

    }

//    private void handleTokens(final Token[] tokens) {
//
//    }

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

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
