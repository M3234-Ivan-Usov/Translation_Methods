package ru.ifmo.rain.usov.generator;

import ru.ifmo.rain.usov.generator.grammar_builder.CalcGrammar;
import ru.ifmo.rain.usov.generator.grammar_builder.CalcLexer;
import ru.ifmo.rain.usov.generator.lr_parser.LRAutomaton;
import ru.ifmo.rain.usov.generator.lr_parser.LRParser;
import ru.ifmo.rain.usov.generator.lr_parser.Visual;

public class TestAutomaton {
    public static void main(String[] args) {
        CalcGrammar x = new CalcGrammar();
        LRAutomaton y = new LRAutomaton(x);
        CalcLexer z = new CalcLexer(x);
        LRParser w = new LRParser(y, z);
        Visual.automaton(y);
    }
}
