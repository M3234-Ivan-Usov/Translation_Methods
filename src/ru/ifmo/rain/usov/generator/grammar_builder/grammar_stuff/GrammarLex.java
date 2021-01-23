package ru.ifmo.rain.usov.generator.grammar_builder.grammar_stuff;

import java.text.ParseException;
import java.util.Map;

public class GrammarLex {
    private char [] expression;
    private int length;
    private boolean marked;
    private int lastMark;
    private int lastStart;
    private int lastEnd;
    public int position;
    public String lexeme;
    public GrammarItem token;

    static Map<String, GrammarItem> tokenMap = Map.ofEntries(
            Map.entry("\\d", GrammarItem.DIGIT),
            Map.entry("\\l", GrammarItem.LOWER),
            Map.entry("\\u", GrammarItem.UPPER),
            Map.entry("\\w", GrammarItem.LETTER),
            Map.entry("\\s", GrammarItem.SPACE),
            Map.entry("\\t", GrammarItem.TAB),
            Map.entry("\\n", GrammarItem.NEWLINE),
            Map.entry("\\e", GrammarItem.EPS),
            Map.entry("?", GrammarItem.QUESTION),
            Map.entry("*", GrammarItem.WILDCARD),
            Map.entry("+", GrammarItem.PLUS),
            Map.entry("(", GrammarItem.LEFT_BRACKET),
            Map.entry(")", GrammarItem.RIGHT_BRACKET),
            Map.entry("->", GrammarItem.ARROW),
            Map.entry(";", GrammarItem.END_RULE),
            Map.entry("$", GrammarItem.END_GRAMMAR)
    );

    public GrammarLex(String expression) {
        this.expression = (expression + " $").toCharArray();
        this.length = this.expression.length;
        this.position = 0;
        this.marked = false;
        this.token = null;
    }

    public String readHeader() throws ParseException {
        lexeme = nextLexeme();
        if (!lexeme.equals("grammar")) { throw new ParseException("Illegal header", 0); }
        String name  = nextLexeme();
        if (token != GrammarItem.NON_TERM) { throw new ParseException("Illegal header", 0); }
        lexeme = nextLexeme();
        token = tokenMap.getOrDefault(lexeme, null);
        if (token != GrammarItem.END_RULE) { throw new ParseException("Illegal header", 0); }
        return name;
    }

    public void mark() { lastMark = lastStart; }

    public String getMarker() { return new String(expression, lastMark, lastEnd - lastMark); }

    public void nextToken() throws ParseException {
        token = null;
        lastEnd = position;
        lexeme = nextLexeme();
        if (token == null) { token = tokenMap.getOrDefault(lexeme, null); }
        if (token == null) { throw new ParseException("Unknown token: " + lexeme , position); }
    }

    private String nextLexeme() {
        while (position != length && Character.isWhitespace(expression[position])) { position++; }
        lastStart = position;
        int oldPosition = position, lexemeLength = 0;
        char start = expression[oldPosition];
        switch (start) {
            case '\\':
                position++; lexemeLength++;
                while (Character.isLetter(expression[position])) { position++; lexemeLength++; }
                return new String(expression, oldPosition, lexemeLength);
            case '\'':
                token = GrammarItem.QUOTE; position++;
                while (position != length && expression[position] != '\'') { position++; lexemeLength++; }
                position++;
                return new String(expression, oldPosition + 1, lexemeLength);
            case '-':
                position += 2;
                return new String(expression, oldPosition, 2);
        }

        while (position != length && Character.isLowerCase(expression[position])) {
            position++; lexemeLength++;
        }
        if (lexemeLength == 0) { position++; return new String(expression, oldPosition, 1); }
        else { token = GrammarItem.NON_TERM; return new String(expression, oldPosition, lexemeLength); }
    }
}
