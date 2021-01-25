package ru.ifmo.rain.usov.generator.lr_parser;

import ru.ifmo.rain.usov.generator.grammar_base.Attributable;
import ru.ifmo.rain.usov.generator.grammar_base.Grammar;
import ru.ifmo.rain.usov.generator.grammar_base.Product;

import java.util.*;

public class LRAutomaton<Token extends Enum<Token>, Attribute extends Attributable<Token>> {

    public static class LRState<T extends Enum<T>, A extends Attributable<T>> {
        public final Set<LRItem<T, A>> items;
        public final Map<T, LRState<T, A>> move;
        public final Map<A, LRAction> action;
        public final Set<T> current;
        public boolean terminator;

        public LRState(Set<LRItem<T, A>> items) {
            this.items = items;
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
            LRState lrState = (LRState) o;
            return Objects.equals(items, lrState.items);
        }

        @Override
        public int hashCode() { return Objects.hash(items, move, action, current); }

        @Override
        public String toString() {
            return String.valueOf(items.size());
        /*StringBuilder representation = new StringBuilder();
        for (LRItem item: items) { representation.append(item).append(";").append(System.lineSeparator()); }
        return representation.toString();*/
        }
    }

    public final Grammar<Token, Attribute> grammar;
    public final LRState<Token, Attribute> initial;
    public final Set<LRState<Token, Attribute>> canonicalCollection;

    public LRAutomaton(Grammar<Token, Attribute> grammar) {
        this.grammar = grammar;
        this.canonicalCollection = new HashSet<>();
        // null == END_GRAMMAR
        LRItem<Token, Attribute> initialItem =
                new LRItem<>(grammar.products.get(0), 0, null, grammar);
        this.initial = new LRState<>(lrClosure(Set.of(initialItem)));
        canonicalCollection.add(initial);
        boolean update = true;
        while (update) {
            update = false;
            attempt:
            for (LRState<Token, Attribute> state : canonicalCollection) {
                for (Token x : state.current) {
                    LRState<Token, Attribute> next = new LRState<>(lrMove(state.items, x));
                    if (canonicalCollection.add(next)) {
                        state.move.put(x, next);
                        update = true; break attempt;
                    }
                }
            }
        }
        System.out.println("READY");
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
                    for (Token x : item.afterFirst) {
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
