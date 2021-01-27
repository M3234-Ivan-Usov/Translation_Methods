package ru.ifmo.rain.usov.generator.lr_parser;

import ru.ifmo.rain.usov.generator.grammar_base.Attributable;
import ru.ifmo.rain.usov.generator.grammar_base.Grammar;
import ru.ifmo.rain.usov.generator.grammar_base.Product;

import java.util.*;

public class LRAutomaton<Token extends Enum<Token>, Attribute extends Attributable<Token>> {

    public static class LRState<T extends Enum<T>, A extends Attributable<T>> {
        public final Map<LRItem<T, A>, LRItem<T, A>> items;
        public final Map<T, LRState<T, A>> move;
        public final Map<T, LRAction> action;
        public final Set<T> current;
        public final int id;
        public boolean terminator;

        public LRState(Set<LRItem<T, A>> items, int id) {
            this.items = new HashMap<>();
            for (LRItem<T, A> item: items) { this.items.put(item, item); }
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

        public boolean merge(LRState<T, A> other) {
            boolean modified = false;
            for (LRItem<T, A> item : items.keySet()) {
                Set<T> otherLookAhead = other.items.get(item).lookAhead;
                if (item.lookAhead.addAll(otherLookAhead)) { modified = true; }
            }
            return modified;
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
        LRItem<Token, Attribute> initialItem = new LRItem<>(
                grammar.products.get(0), 0, LRItem.set(null), grammar);
        this.initial = new LRState<>(lrClosure(Set.of(initialItem)), 0);
        canonicalCollection.put(initial, initial);
        boolean update = true;
        while (update) {
            update = false;
            attempt:
            for (LRState<Token, Attribute> state : canonicalCollection.values()) {
                int id = canonicalCollection.size();
                for (Token x : state.current) {
                    update = newState(state, x , new LRState<>(lrMove(state.items.keySet(), x), id));
                    if (update) { break attempt; }
                }
            }
        }
    }

    private boolean newState(LRState<Token, Attribute> state, Token x, LRState<Token, Attribute> candidate) {
        LRState<Token, Attribute> similar = canonicalCollection.get(candidate);
        boolean updated;
        if (similar != null) {
            state.move.put(x, similar);
            updated = similar.merge(candidate);
        }
        else {
            canonicalCollection.put(candidate, candidate);
            state.move.put(x, candidate);
            updated = true;
        }
        return updated;
    }

    private boolean newItem(Map<LRItem<Token, Attribute>, LRItem<Token, Attribute>> result,
                            LRItem<Token, Attribute> candidate) {
        LRItem<Token, Attribute> similar = result.get(candidate);
        boolean updated;
        if (similar != null) { updated = similar.lookAhead.addAll(candidate.lookAhead); }
        else { result.put(candidate, candidate); updated = true; }
        return updated;
    }

    private Set<LRItem<Token, Attribute>> lrClosure(Set<LRItem<Token, Attribute>> in) {
        Map<LRItem<Token, Attribute>, LRItem<Token, Attribute>> result = new HashMap<>();
        for (LRItem<Token, Attribute> item : in) { result.put(item, item); }
        boolean update = true;
        while (update) {
            update = false;
            attempt:
            for (LRItem<Token, Attribute> item : result.keySet()) {
                if (item.terminator || grammar.terminals.contains(item.next)) { continue; }
                for (Product<Token, Attribute> product : grammar.entries.get(item.next)) {
                    update = newItem(result, new LRItem<>(product, 0, item.afterNext(), grammar));
                    if (update) { break attempt; }
                }
            }
        }
        return result.keySet();
    }

    private Set<LRItem<Token, Attribute>> lrMove(Set<LRItem<Token, Attribute>> items, Token x) {
        Set<LRItem<Token, Attribute>> result = new HashSet<>();
        for (LRItem<Token, Attribute> item : items) {
            if (!item.terminator && item.next == x) { result.add(item.advance()); }
        }
        return lrClosure(result);
    }
}
