package ru.ifmo.rain.usov.generator.lr_parser;

import ru.ifmo.rain.usov.generator.grammar_base.Attributable;
import ru.ifmo.rain.usov.generator.grammar_base.lexer.Lexer;

import java.util.*;

public class LRParser<Token extends Enum<Token>, Attribute extends Attributable<Token>> {
    public Stack<Map.Entry<LRAutomaton.LRState<Token, Attribute>, Attributable<Token>>> prefix;
    public final LRAutomaton<Token, Attribute> automaton;
    public final Lexer<Token, Attribute> lexer;
    public Deque<Attribute> input;
    public boolean accepted;


    public LRParser(LRAutomaton<Token, Attribute> automaton, Lexer<Token, Attribute> lexer) {
        this.automaton = automaton;
        this.lexer = lexer;
        LRItem<Token, Attribute> terminator = new LRItem<>(automaton.grammar.products.get(0),
                1, null, automaton.grammar);
        for (LRAutomaton.LRState<Token, Attribute> state : automaton.canonicalCollection) {
            for (LRItem<Token, Attribute> item : state.items) {
/*
                if (!item.terminator) {
                    LRAutomaton.LRState<Token, Attribute> next = state.move.get(item.next);
                    state.action.put(item.next, () -> prefix.push(Map.entry(next, input.removeFirst()))
                    );
                }
                else if (item.equals(terminator)) { state.action.put(item.lookAhead, () -> accepted = true); }
                else  { state.action.put(item.lookAhead, () -> {
                    for (int i = 0; i < item.product.right.size(); ++i) { prefix.pop(); }
                    LRAutomaton.LRState<Token, Attribute> next = prefix.peek().getKey().move.get(item.product.left);
                    //prefix.push(Map.entry(next, item.product.left));
                }); }*/
            }
        }
    }

   /* private Attribute makeAttributable(Token token) {
    }*/
    public void parse(String input) {
        this.input = new ArrayDeque<>();
        lexer.load(input);
        do { this.input.offerLast(lexer.nextToken()); }
        while (lexer.position != lexer.length);
        prefix.push(Map.entry(automaton.initial, null));
        accepted = false;
        while(!accepted) {
            Attribute lookAhead = this.input.peek();
            LRAutomaton.LRState<Token, Attribute> current = prefix.peek().getKey();
            current.action.get(lookAhead).perform();
        }
    }

}
