package ru.ifmo.rain.usov.generator.lr_parser;


import ru.ifmo.rain.usov.generator.grammar_builder.*;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Objects;

public class LRItem {
    public final Product product;
    public final int pointer;
    public final boolean terminator;
    public final Token next;
    public final Token lookAhead;
    public final EnumSet<Token> afterFirst;
    public final EnumMap<Token, EnumSet<Token>> firsts;

    public LRItem(Product product, int pointer, Token lookAhead, EnumMap<Token, EnumSet<Token>> firsts) {
        this.product = product;
        this.pointer = pointer;
        this.firsts = firsts;
        this.terminator = pointer == product.right.size();
        this.next = (terminator)? null : product.right.get(pointer);
        this.lookAhead = lookAhead;
        this.afterFirst = EnumSet.noneOf(Token.class);
        if (pointer == product.right.size() - 1) { afterFirst.add(lookAhead); }
        else if (pointer < product.right.size() - 1) {
            Token after = product.right.get(pointer + 1);
            EnumSet<Token> afterFirst = firsts.get(after);
            if (afterFirst.contains(Token.EPS)) { this.afterFirst.add(lookAhead); }
            else this.afterFirst.addAll(afterFirst);
        }
    }

    public LRItem advance() { return new LRItem(product, pointer + 1, lookAhead, firsts); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LRItem other = (LRItem) o;
        return pointer == other.pointer && Objects.equals(product, other.product) && lookAhead == other.lookAhead;
    }

    @Override
    public int hashCode() { return Objects.hash(product, pointer, terminator, next, lookAhead, afterFirst, firsts); }

    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder(product.left.toString() + " ->");
        int index = 0;
        for (; index < pointer; ++index) {
            representation.append(" ").append(product.right.get(index).toString());
        }
        representation.append(" .");
        for (; index < product.right.size(); ++index) {
            representation.append(" ").append(product.right.get(index).toString());
        }
        representation.append(", ").append(lookAhead.toString());
        return representation.toString();
    }
}
