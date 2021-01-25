package ru.ifmo.rain.usov.generator;


import ru.ifmo.rain.usov.generator.calc.CalcAttribute;
import ru.ifmo.rain.usov.generator.calc.CalcGrammar;
import ru.ifmo.rain.usov.generator.calc.CalcItem;
import ru.ifmo.rain.usov.generator.calc.CalcLexer;
import ru.ifmo.rain.usov.generator.lr_parser.LRAutomaton;

public class TestAutomaton {
    public static void main(String[] args) {
        CalcGrammar x = new CalcGrammar();
        LRAutomaton<CalcItem, CalcAttribute> y = new LRAutomaton<>(x);
        CalcLexer z = new CalcLexer(CalcAttribute.class);
        Visual.lrAutomaton(y);
        Visual.lexerDFA(z.automaton);
    }
}
