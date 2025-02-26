package com.dp9v.loxinterpreter;

public record Token(TokenType type, String lexeme, Object literal, int line) {

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", lexeme='" + lexeme + '\'' +
                ", literal=" + literal +
                '}';
    }
}
