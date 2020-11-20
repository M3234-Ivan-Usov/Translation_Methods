package ru.ifmo.rain.usov.recursive;

import java.text.ParseException;
import java.util.Map;

/**
 * Allows to extract lexemes ignoring whitespaces
 */
public class LexicalAnalyser {
    private char [] expression;
    private int length;
    public int position;
    public String lexeme;
    public Token token;

    /**
     * Matches key words with their tokens
     */
    private static final Map<String, Token> tokenType = Map.ofEntries(
            Map.entry("or", Token.OR),
            Map.entry("and", Token.AND),
            Map.entry("xor", Token.XOR),
            Map.entry("not", Token.NOT),
            Map.entry("$", Token.END),
            Map.entry("(", Token.LEFT),
            Map.entry(")", Token.RIGHT)
    );

    /**
     * @param expression Append '$' to detect end of expression
     */
    public LexicalAnalyser(String expression) {
        this.expression = (expression + '$').toCharArray();
        this.length = expression.length();
        this.position = 0;
        this.token = null;
    }

    /**
     * Determine next token type
     * @throws ParseException If unknown lexeme occurs
     */
    public void nextToken() throws ParseException {
        lexeme = nextLexeme();
        token = tokenType.getOrDefault(lexeme, (lexeme.length() == 1)? Token.VARIABLE : Token.ERROR);
        if (token == Token.ERROR) {
            throw new ParseException("Unexpected lexeme: " + lexeme, position);
        }
    }

    /**
     * Extract next lexeme ignoring whitespaces
     * @return String without whitespaces, guaranteed at least one symbol
     */
    private String nextLexeme() {
        while (position != length && Character.isWhitespace(expression[position])) {
            position++;
        }
        int oldPosition = position;
        int lexemeLength = 0;
        while (position != length && Character.isLetter(expression[position])) {
            position++;
            lexemeLength++;
        }
        if (position == oldPosition) {
            position++;
            return String.valueOf(expression[oldPosition]);
        }
        return new String(expression, oldPosition, lexemeLength);
    }




}
