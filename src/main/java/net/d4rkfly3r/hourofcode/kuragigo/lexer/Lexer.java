package net.d4rkfly3r.hourofcode.kuragigo.lexer;

import net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens.*;

import java.util.HashMap;

public class Lexer {

    private static final HashMap<String, IdentificationToken> RESERVED_KEYWORDS = new HashMap<>();

    static {
        RESERVED_KEYWORDS.put("function", new SymbolToken(Token.Type.FUNCTION));
        RESERVED_KEYWORDS.put("number", new SymbolToken(Token.Type.NUMBER));
        RESERVED_KEYWORDS.put("string", new SymbolToken(Token.Type.STRING));
        RESERVED_KEYWORDS.put("sqrt", new SymbolToken(Token.Type.SQRT));
        RESERVED_KEYWORDS.put("for", new SymbolToken(Token.Type.FOR));
    }

    private final String inputText;
    private Character currentCharacter;
    private Token currentToken;
    private int position;
    private int currentLine;
    private int currentLinePosition;
    private String[] lines;

    public Lexer(final String inputText) {
        this.inputText = inputText;
        this.position = 0;
        this.currentLine = 0;
        this.currentLinePosition = 0;
        this.currentToken = null;
        this.currentCharacter = this.inputText.charAt(this.position);
        this.lines = this.inputText.split("\\n");
    }

    private void syntaxError(final String cause) {
        final String errorString = "\n" +
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + "\n" +
                this.lines[this.currentLine] + "\n" +
                "\n" +
                "Error at position: " + (this.currentLinePosition) + " on line: " + (this.currentLine + 1) + "\n" +
                "Cause: " + (cause == null ? "Unknown!" : cause) + "\n" +
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + "\n";
        System.out.flush();
        System.err.flush();
        System.err.println(errorString);
        System.err.flush();
        System.exit(1);
    }

    public Token getNextToken() {
        while (this.currentCharacter != null) {

            this.skipWhitespace();
            if (this.currentCharacter == null) {
                break;
            }

            if (Character.isDigit(this.currentCharacter)) {
                return this.number();
            }

            if (Character.isLetter(this.currentCharacter)) {
                return this.identification();
            }

            switch (this.currentCharacter) {
                case '`':
                    this.advance();
                    this.skipComment();
                    break;
                case '"':
                    this.advance();
                    return this.string();
                case ':':
                    this.advance();
                    return new SymbolToken(Token.Type.COLON);
                case ',':
                    this.advance();
                    return new SymbolToken(Token.Type.COMMA);
                case '=':
                    this.advance();
                    if (this.currentCharacter != '=') {
                        return new SymbolToken(Token.Type.ASSIGN);
                    }
                    break;
                case ';':
                    this.advance();
                    return new SymbolToken(Token.Type.SEMICOLON);
                case '+':
                    this.advance();
                    return new OperatorToken(Token.Type.PLUS);
                case '-':
                    this.advance();
                    this.skipWhitespace();
                    if (this.currentCharacter == '-') {
                        return new OperatorToken(Token.Type.PLUS);
                    }
                    return new OperatorToken(Token.Type.MINUS);
                case '*':
                    this.advance();
                    return new OperatorToken(Token.Type.MULTIPLY);
                case '/':
                    this.advance();
                    return new OperatorToken(Token.Type.DIVIDE);
                case '^':
                    this.advance();
                    return new OperatorToken(Token.Type.POWER);
                case '%':
                    this.advance();
                    return new OperatorToken(Token.Type.MODULO);
                case '(':
                    this.advance();
                    return new SymbolToken(Token.Type.OPEN_PARENTHESIS);
                case ')':
                    this.advance();
                    return new SymbolToken(Token.Type.CLOSE_PARENTHESIS);
                case '{':
                    this.advance();
                    return new SymbolToken(Token.Type.OPEN_CURLY_BRACE);
                case '}':
                    this.advance();
                    return new SymbolToken(Token.Type.CLOSE_CURLY_BRACE);
                case '[':
                    this.advance();
                    return new SymbolToken(Token.Type.OPEN_SQUARE_BRACKET);
                case ']':
                    this.advance();
                    return new SymbolToken(Token.Type.CLOSE_SQUARE_BRACKET);
                case '<':
                    this.advance();
                    return new SymbolToken(Token.Type.OPEN_ANGLE_BRACKET);
                case '>':
                    this.advance();
                    return new SymbolToken(Token.Type.CLOSE_ANGLE_BRACKET);
                default:
                    this.syntaxError("Invalid operator!");

            }

        }

        return SymbolToken.EOF;
    }

    private void advance() {
        Character nextCharacter;
        if ((nextCharacter = this.peek()) != null) {
            this.position++;
            this.currentLinePosition++;
            this.currentCharacter = nextCharacter;
        } else {
            this.currentCharacter = null;
        }
    }

    private Character peek() {
        if (this.position + 1 > this.inputText.length() - 1) {
            return null;
        }
        return this.inputText.charAt(this.position + 1);
    }

    private Character reversePeek() {
        if (this.position - 1 < 0) {
            return null;
        }
        return this.inputText.charAt(this.position - 1);
    }

    private void skipWhitespace() {
        while (this.currentCharacter != null && Character.isWhitespace(this.currentCharacter)) {
            if (this.currentCharacter == '\n') {
                this.currentLine++;
                this.currentLinePosition = 0;
            }
            this.advance();
        }
    }

    private void skipComment() {
        while (this.currentCharacter != null && this.currentCharacter != '`') {
            this.advance();
        }
        if (this.currentCharacter != null) {
            this.advance();
        }
    }

    private IdentificationToken identification() {
        String resultString = "";
        while (this.currentCharacter != null && Character.isLetterOrDigit(this.currentCharacter)) {
            resultString += this.currentCharacter;
            this.advance();
        }

        return RESERVED_KEYWORDS.getOrDefault(resultString, new IdentificationToken(resultString));
    }

    private NumberToken number() {
        String resultString = "";
        while (this.currentCharacter != null && Character.isDigit(this.currentCharacter)) {
            resultString += this.currentCharacter;
            this.advance();
        }

        if (this.currentCharacter != null && this.currentCharacter == '.') {
            resultString += this.currentCharacter;
            this.advance();

            while (this.currentCharacter != null && Character.isDigit(this.currentCharacter)) {
                resultString += this.currentCharacter;
                this.advance();
            }
        }

        return new NumberToken(new Double(resultString));
    }

    private StringToken string() {
        String resultString = "";
        boolean shouldContinue = true;
        while (this.currentCharacter != null && shouldContinue) {
            resultString += this.currentCharacter;
            this.advance();
            //noinspection ConstantConditions
            if (this.currentCharacter == '"' && this.reversePeek() != '\\') shouldContinue = false;
        }
        this.advance();
        return new StringToken(resultString);
    }
}
