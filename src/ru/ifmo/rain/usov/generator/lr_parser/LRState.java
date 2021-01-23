package ru.ifmo.rain.usov.generator.lr_parser;

import ru.ifmo.rain.usov.generator.grammar_builder.Token;

import java.util.*;

public class LRState {
    public final Set<LRItem> items;
    public final EnumMap<Token, LRState> move;
    public final EnumMap<Token, LRAction> action;
    public final EnumSet<Token> current;
    public boolean terminator;

    public LRState(Set<LRItem> items) {
        this.items = items;
        this.move = new EnumMap<>(Token.class);
        this.action = new EnumMap<>(Token.class);
        this.current = EnumSet.noneOf(Token.class);
        this.terminator = false;
        for (LRItem item : items) {
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
    public int hashCode() {
        return Objects.hash(items, move, action, current);
    }

    @Override
    public String toString() {
        return String.valueOf(items.size());
        /*StringBuilder representation = new StringBuilder();
        for (LRItem item: items) { representation.append(item).append(";").append(System.lineSeparator()); }
        return representation.toString();*/
    }
}
