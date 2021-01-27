package ru.ifmo.rain.usov.generator.lr_parser;

import ru.ifmo.rain.usov.generator.grammar_base.Attributable;
import ru.ifmo.rain.usov.generator.grammar_base.Grammar;
import ru.ifmo.rain.usov.generator.grammar_base.Product;

import java.util.*;

public class LRAutomaton<Token extends Enum<Token>, Attribute extends Attributable<Token>> {

    public static class LRState<T extends Enum<T>, A extends Attributable<T>> {
        public final Set<LRItem<T, A>> items;
        public final Map<T, LRState<T, A>> move;
        public final Map<T, LRAction> action;
        public final Set<T> current;
        public final int id;
        public boolean terminator;

        public LRState(Set<LRItem<T, A>> items, int id) {
            this.items = items;
            this.id = id;
            this.move = new HashMap<>();
            this.action = new HashMap<>();
            this.current = new HashSet<>();
            this.terminator = false;
            for (LRItem<T, A> item : items) {
                if (!item.terminator) { current.add(item.next); }
                else { this.terminator = true; }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LRState<?, ?> lrState = (LRState<?, ?>) o;
            return Objects.equals(items, lrState.items);
        }

        @Override
        public int hashCode() { return Objects.hash(items); }

        @Override
        public String toString() { return String.valueOf(id); }
    }

    public final Grammar<Token, Attribute> grammar;
    public final LRState<Token, Attribute> initial;
    public final Map<LRState<Token, Attribute>, LRState<Token, Attribute>> canonicalCollection;

    public LRAutomaton(Grammar<Token, Attribute> grammar) {
        this.grammar = grammar;
        this.canonicalCollection = new HashMap<>();
        LRItem<Token, Attribute> initialItem =
                new LRItem<>(grammar.products.get(0), 0, null, grammar);
        this.initial = new LRState<>(lrClosure(Set.of(initialItem)), 0);
        canonicalCollection.put(initial, initial);
        int id = 1; boolean update = true;
        while (update) {
            update = false;
            attempt:
            for (LRState<Token, Attribute> state : canonicalCollection.values()) {
                for (Token x : state.current) {
                    LRState<Token, Attribute> next = new LRState<>(lrMove(state.items, x), id);
                    LRState<Token, Attribute> similar = canonicalCollection.get(next);
                    if (similar != null) { state.move.put(x, similar); }
                    else {
                        canonicalCollection.put(next, next);
                        state.move.put(x, next); id++;
                        update = true; break attempt;
                    }
                }
            }
        }
    }

    private Set<LRItem<Token, Attribute>> lrClosure(Set<LRItem<Token, Attribute>> in) {
        Set<LRItem<Token, Attribute>> result = new HashSet<>(in);
        boolean update = true;
        while (update) {
            update = false;
            attempt:
            for (LRItem<Token, Attribute> item : result) {
                if (item.terminator || grammar.terminals.contains(item.next)) { continue; }
                for (Product<Token, Attribute> product : grammar.entries.get(item.next)) {
                    for (Token x : item.afterNext) {
                        if (result.add(new LRItem<>(product, 0, x, grammar))) {
                            update = true; break attempt;
                        }
                    }
                }
            }
        }
        return result;
    }

    private Set<LRItem<Token, Attribute>> lrMove(Set<LRItem<Token, Attribute>> items, Token x) {
        Set<LRItem<Token, Attribute>> result = new HashSet<>();
        for (LRItem<Token, Attribute> item : items) {
            if (!item.terminator && item.next == x) { result.add(item.advance(grammar)); }
        }
        return lrClosure(result);
    }
}
