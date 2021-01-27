package ru.ifmo.rain.usov.generator.lr_parser;

import ru.ifmo.rain.usov.generator.grammar_base.Attributable;
import ru.ifmo.rain.usov.generator.grammar_base.lexer.Lexer;

import java.text.ParseException;
import java.util.*;

public class LRParser<Token extends Enum<Token>, Attribute extends Attributable<Token>> {
    protected Stack<LRAutomaton.LRState<Token, Attribute>> states;
    protected Stack<Attribute> viablePrefix;
    public final LRAutomaton<Token, Attribute> automaton;
    public final Lexer<Token, Attribute> lexer;
    protected Deque<Attribute> tokenStream;
    protected boolean accepted;


    public LRParser(LRAutomaton<Token, Attribute> automaton, Lexer<Token, Attribute> lexer) {
        this.automaton = automaton;
        this.lexer = lexer;
        LRItem<Token, Attribute> terminator = new LRItem<>(automaton.grammar.products.get(0),
                1, LRItem.set(null), automaton.grammar);
        for (LRAutomaton.LRState<Token, Attribute> state : automaton.canonicalCollection.values()) {
            for (LRItem<Token, Attribute> item : state.items.keySet()) {
                if (!item.terminator && item.nextTerminal) {
                    LRAutomaton.LRState<Token, Attribute> next = state.move.get(item.next);
                    LRAction prev = state.action.put(item.next, () -> {
                        states.push(next); viablePrefix.push(tokenStream.pollFirst());
                    });
                    if (prev instanceof ActionReduce) { throw new RuntimeException(
                            "ERROR: Shift-Reduce conflict appears on state " + state.id + " via " + item.next); }
                }
                else if (item.terminator) {
                    ActionReduce<Token, Attribute> reduce = new ActionReduce<>(item.product, this);
                    if (item.equals(terminator)) { reduce.accept = true; }
                    for (Token ahead : item.lookAhead) {
                        LRAction prev = state.action.put(ahead, reduce);
                        if (prev != null) { checkConflict(prev, reduce, state, item); }
                    }
                }
            }
        }
    }

    private void checkConflict(LRAction prev, ActionReduce<Token, Attribute> reduce,
                               LRAutomaton.LRState<Token, Attribute> state, LRItem<Token, Attribute> item) {
        if (!(prev instanceof ActionReduce)) { throw new RuntimeException(
                "ERROR: Shift-Reduce conflict appears on state " + state.id + " via " + item.next); }
        else if (!reduce.equals(prev)) { throw new RuntimeException(
                "ERROR: Reduce-Reduce conflict appears on state " + state.id + " by " + item.next +
                        System.lineSeparator() + ((ActionReduce<?, ?>) prev).product +
                        System.lineSeparator() + reduce.product + System.lineSeparator()); }
    }

    public Attribute parse(String input) throws ParseException {
        this.tokenStream = new ArrayDeque<>();
        this.states = new Stack<>();
        this.viablePrefix = new Stack<>();
        lexer.load(input);
        do { Attribute next = lexer.nextToken();
            if (next == null) { throw new ParseException("ERROR: Unexpected char: '"
                    + lexer.input[lexer.position] + "' (" + lexer.position + ")", lexer.position); }
            this.tokenStream.offerLast(next);
        } while (lexer.position != lexer.length);
        this.tokenStream.offerLast(lexer.makeInstance(null));
        this.states.push(automaton.initial);
        accepted = false;
        while (!accepted) {
            Attribute lookAhead = this.tokenStream.peekFirst();
            LRAutomaton.LRState<Token, Attribute> current = states.peek();
            LRAction action = current.action.get(lookAhead.token);
            if (action != null) { action.perform(); }
            else { throw new ParseException("ERROR: No action on state " +
                        states.peek().id + " by " + this.tokenStream.peekFirst().token, 0); }
        }
        return viablePrefix.pop();
    }
}
