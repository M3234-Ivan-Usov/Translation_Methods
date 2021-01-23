package ru.ifmo.rain.usov.generator.lr_parser;

import ru.ifmo.rain.usov.generator.grammar_builder.CalcGrammar;
import ru.ifmo.rain.usov.generator.grammar_builder.Product;
import ru.ifmo.rain.usov.generator.grammar_builder.Token;

import java.util.HashSet;
import java.util.Set;

public class LRAutomaton {
    public final CalcGrammar grammar;
    public final LRState initial;
    public final Set<LRState> canonicalCollection;

    public LRAutomaton(CalcGrammar grammar) {
        this.grammar = grammar;
        this.canonicalCollection = new HashSet<>();
        LRItem initialItem = new LRItem(grammar.products.get(0), 0, Token.END_GRAMMAR, grammar.first);
        this.initial = new LRState(closure(Set.of(initialItem)));
        canonicalCollection.add(initial);
        boolean update = true;
        while (update) {
            update = false;
            attempt:
            for (LRState state : canonicalCollection) {
                for (Token x : state.current) {
                    LRState next = new LRState(move(state.items, x));
                    if (canonicalCollection.add(next)) {
                        state.move.put(x, next);
                        update = true; break attempt;
                    }
                }
            }
        }
        System.out.println("READY");
    }

    private Set<LRItem> closure(Set<LRItem> in) {
        Set<LRItem> result = new HashSet<>(in);
        boolean update = true;
        while (update) {
            update = false;
            attempt:
            for (LRItem item : result) {
                if (item.terminator || grammar.terminals.contains(item.next)) { continue; }
                for (Product product : grammar.entries.get(item.next)) {
                    for (Token x : item.afterFirst) {
                        if (result.add(new LRItem(product, 0, x, grammar.first))) {
                            update = true; break attempt;
                        }
                    }
                }
            }
        }
        return result;
    }

    private Set<LRItem> move(Set<LRItem> items, Token x) {
        Set<LRItem> result = new HashSet<>();
        for (LRItem item : items) {
            if (!item.terminator && item.next == x) { result.add(item.advance()); }
        }
        return closure(result);
    }
}
