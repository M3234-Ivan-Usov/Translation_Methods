package ru.ifmo.rain.usov.generator;


import ru.ifmo.rain.usov.generator.bool.BoolAttribute;
import ru.ifmo.rain.usov.generator.bool.BoolGrammar;
import ru.ifmo.rain.usov.generator.bool.BoolItem;
import ru.ifmo.rain.usov.generator.bool.BoolLexer;
import ru.ifmo.rain.usov.generator.calc.CalcAttribute;
import ru.ifmo.rain.usov.generator.calc.CalcGrammar;
import ru.ifmo.rain.usov.generator.calc.CalcItem;
import ru.ifmo.rain.usov.generator.calc.CalcLexer;
import ru.ifmo.rain.usov.generator.lr_parser.LRAutomaton;
import ru.ifmo.rain.usov.generator.lr_parser.LRParser;

import java.text.ParseException;

public class TestAutomaton {
    public static void main(String[] args) throws ParseException {
        CalcGrammar x = new CalcGrammar();
        LRAutomaton<CalcItem, CalcAttribute> y = new LRAutomaton<>(x);
        CalcLexer z = new CalcLexer(CalcAttribute.class);
        Visual.grammarSets(x);
        Visual.lexerDFA(z.automaton);
        Visual.lrAutomaton(y);
        LRParser<CalcItem, CalcAttribute> parser = new LRParser<>(y, z);
        CalcAttribute root = parser.parse("2 - (3 * 5.0) / 2");
        System.out.println(root.val);
        Visual.tree(root);
        /*BoolGrammar a = new BoolGrammar();
        BoolLexer b = new BoolLexer(BoolAttribute.class);
        LRAutomaton<BoolItem, BoolAttribute> c = new LRAutomaton<>(a);
        LRParser<BoolItem, BoolAttribute> d = new LRParser<>(c, b);
        Visual.grammarSets(a);
        Visual.lexerDFA(b.automaton);
        Visual.lrAutomaton(c);
        BoolAttribute x = d.parse("true or false and not true xor false");
        Visual.tree(x);*/
    }
}
