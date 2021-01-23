package ru.ifmo.rain.usov.generator.lr_parser;

import ru.ifmo.rain.usov.generator.grammar_builder.CalcLexer;
import ru.ifmo.rain.usov.generator.grammar_builder.Token;

import java.util.*;

public class LRParser {
    public final LRAutomaton automaton;
    public final CalcLexer lexer;
    public LRState currentState;
    public Deque<Token> input;
    public Stack<Map.Entry<LRState, Token>> prefix;
    public boolean accepted;


    public LRParser(LRAutomaton automaton, CalcLexer lexer) {
        this.automaton = automaton;
        this.lexer = lexer;
        LRItem terminator = new LRItem(automaton.grammar.products.get(0), 1, Token.END_GRAMMAR, automaton.grammar.first);
        for (LRState state : automaton.canonicalCollection) {
            for (LRItem item : state.items) {
                if (!item.terminator) {
                    LRState next = state.move.get(item.next);
                    //state.action.put(item.next, new ActionShift(this, next));
                    state.action.put(item.next, () -> prefix.push(Map.entry(next, input.removeFirst()))
                    );
                }
                else if (item.equals(terminator)) { state.action.put(item.lookAhead, () -> accepted = true); }
                //else  { state.action.put(item.lookAhead, new ActionReduce(this, item.product)); }
                else  { state.action.put(item.lookAhead, () -> {
                    for (int i = 0; i < item.product.right.size(); ++i) { prefix.pop(); }
                    LRState next = prefix.peek().getKey().move.get(item.product.left);
                    prefix.push(Map.entry(next, item.product.left));
                }); }
            }
        }
    }

    public void parse(String in) {
        input = new ArrayDeque<>();
        //do { input.offerLast(lexer.nextToken()); }
        while (lexer.token != Token.END_GRAMMAR);
        currentState = automaton.initial;
        prefix.push(Map.entry(automaton.initial, Token.AUGMENT));
        accepted = false;
        while(!accepted) {
            prefix.peek().getKey().action.get(input.peekFirst()).perform();
        }
    }

}
