package ru.ifmo.rain.usov.generator.grammar_builder.builder_stuff;

import java.text.ParseException;
import java.util.Map;

public class GrammarLex {
    private char [] grammar;
    private int length;
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
            Map.entry("\\r", GrammarItem.CARRIAGE),
            Map.entry("\\t", GrammarItem.TAB),
            Map.entry("\\n", GrammarItem.NEWLINE),
            Map.entry("?", GrammarItem.QUESTION),
            Map.entry("*", GrammarItem.WILDCARD),
            Map.entry("+", GrammarItem.PLUS),
            Map.entry("|", GrammarItem.ALTER),
            Map.entry("(", GrammarItem.START_REGEX),
            Map.entry(")", GrammarItem.END_REGEX),
            Map.entry("->", GrammarItem.ARROW),
            Map.entry("@def", GrammarItem.DEF),
            Map.entry("@attr", GrammarItem.ATTR),
            Map.entry("@start", GrammarItem.START),
            Map.entry(";", GrammarItem.END_RULE),
            Map.entry("$", GrammarItem.END_GRAMMAR)
    );

    public GrammarLex(String grammar) {
        this.grammar = (grammar + " $").toCharArray();
        this.length = this.grammar.length;
        this.position = 0;
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

    public String getMarker() { return new String(grammar, lastMark, lastEnd - lastMark); }

    public void nextToken() throws ParseException {
        token = null;
        lastEnd = position;
        lexeme = nextLexeme();
        //System.out.println(lexeme);
        if (token == null) { token = tokenMap.getOrDefault(lexeme, null); }
        if (token == null) { throw new ParseException("Unknown token: " + lexeme , position); }
    }

    private String nextLexeme() {
        while (position != length && Character.isWhitespace(grammar[position])) { position++; }
        lastStart = position;
        boolean special = false;
        int oldPosition = position, lex = 0;
        char start = grammar[oldPosition];
        switch (start) {
            case '@':
            case '\\':
                position++; lex++;
                special = true; break;
            case '\'': token = GrammarItem.QUOTE;
                position++;
                while (position != length && grammar[position] != '\'') { position++; lex++; }
                position++;
                return new String(grammar, oldPosition + 1, lex);
            case '{': token = GrammarItem.CODE;
                position++;
                while (position != length && grammar[position] != '}') { position++; lex++; }
                position++;
                return new String(grammar, oldPosition + 1, lex);
            case '-': position += 2;
                return new String(grammar, oldPosition, 2);
        }
        while (position != length && Character.isLowerCase(grammar[position])) { position++; lex++; }
        if (special) { return new String(grammar, oldPosition, lex); }
        if (lex == 0) { position++; return new String(grammar, oldPosition, 1); }
        else { token = GrammarItem.NON_TERM; return new String(grammar, oldPosition, lex); }
    }
}
