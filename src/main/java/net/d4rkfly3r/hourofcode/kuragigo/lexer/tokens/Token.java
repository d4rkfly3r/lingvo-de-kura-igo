package net.d4rkfly3r.hourofcode.kuragigo.lexer.tokens;

import java.io.Serializable;

public abstract class Token implements Serializable {


    private final Type tokenType;

    public Token(final Type tokenType) {
        this.tokenType = tokenType;
    }

    public Type getTokenType() {
        return this.tokenType;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenType=" + this.tokenType +
                '}';
    }

    public enum Type {
        EOF(Devsi.CONTROL),
        FUNCTION(Devsi.CONTROL),
        SEMICOLON(Devsi.CONTROL),
        ASSIGN(Devsi.CONTROL),
        FOR(Devsi.CONTROL),

        NUMBER(Devsi.DATA),
        STRING(Devsi.DATA),

        IDENTIFICATION(Devsi.CONTROL),
        OPEN_PARENTHESIS(Devsi.CONTROL),
        CLOSE_PARENTHESIS(Devsi.CONTROL),
        OPEN_CURLY_BRACE(Devsi.CONTROL),
        CLOSE_CURLY_BRACE(Devsi.CONTROL),
        OPEN_SQUARE_BRACKET(Devsi.CONTROL),
        CLOSE_SQUARE_BRACKET(Devsi.CONTROL),
        OPEN_ANGLE_BRACKET(Devsi.CONTROL),
        CLOSE_ANGLE_BRACKET(Devsi.CONTROL),
        COLON(Devsi.OTHER),

        COMMA(Devsi.OTHER),
        PERIOD(Devsi.OTHER),
        PLUS(Devsi.SECONDARY_OPERATOR),

        MINUS(Devsi.SECONDARY_OPERATOR),
        MULTIPLY(Devsi.PRIMARY_OPERATOR),
        DIVIDE(Devsi.PRIMARY_OPERATOR),
        MODULO(Devsi.PRIMARY_OPERATOR),
        POWER(Devsi.PRIMARY_OPERATOR),
        SQRT(Devsi.PRIMARY_OPERATOR),
        CLASS(Devsi.CONTROL);


        private final Devsi devsi;

        Type(final Devsi devsi) {
            this.devsi = devsi;
        }

        public boolean isPrimaryMathOperator() {
            return this.devsi == Devsi.PRIMARY_OPERATOR;
        }

        public boolean isSecondaryMathOperator() {
            return this.devsi == Devsi.SECONDARY_OPERATOR;
        }

        public boolean isVariableType() {
            return this.devsi == Devsi.DATA;
        }

        public enum Devsi {
            PRIMARY_OPERATOR,
            SECONDARY_OPERATOR,
            DATA,
            CONTROL,
            OTHER,
        }
    }

}
