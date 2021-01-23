package ru.ifmo.rain.usov.generator.grammar_builder;

public class CalcLexer {
    public final CalcGrammar grammar;
    private int position;
    public Token token;

    public CalcLexer(CalcGrammar grammar) {
        this.grammar = grammar;
    }

}
