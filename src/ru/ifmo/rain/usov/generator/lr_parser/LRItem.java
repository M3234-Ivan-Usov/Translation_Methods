package ru.ifmo.rain.usov.generator.lr_parser;

import ru.ifmo.rain.usov.generator.grammar_base.Attributable;
import ru.ifmo.rain.usov.generator.grammar_base.Grammar;
import ru.ifmo.rain.usov.generator.grammar_base.Product;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class LRItem<Token extends Enum<Token>, Attribute extends Attributable<Token>> {
    public final Product<Token, Attribute> product;
    public final int pointer;
    public final boolean terminator;
    public final Token next;
    public final Token lookAhead;
    public final Set<Token> afterFirst;

    public LRItem(Product<Token, Attribute> product, int pointer,
                  Token lookAhead, Grammar<Token, Attribute> grammar) {
        this.product = product;
        this.pointer = pointer;
        this.terminator = pointer == product.right.size();
        this.next = (terminator)? null : product.right.get(pointer);
        this.lookAhead = lookAhead;
        this.afterFirst = new HashSet<>();
        if (pointer == product.right.size() - 1) { afterFirst.add(lookAhead); }
        else if (pointer < product.right.size() - 1) {
            Token after = product.right.get(pointer + 1);
            afterFirst.addAll(grammar.first.get(after));
            /*Token after = product.right.get(pointer + 1);
            Set<Token> afterFirst = grammar.first.get(after);
            if (afterFirst.contains(Token.EPS)) { this.afterFirst.add(lookAhead); }
            else this.afterFirst.addAll(afterFirst);*/
        }
    }

    public LRItem<Token, Attribute> advance(Grammar<Token, Attribute> grammar) {
        return new LRItem<>(product, pointer + 1, lookAhead, grammar);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LRItem other = (LRItem) o;
        return pointer == other.pointer && Objects.equals(product, other.product) && lookAhead == other.lookAhead;
    }

    @Override
    public int hashCode() { return Objects.hash(product, pointer, terminator, lookAhead); }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(product.left.toString() + " ->");
        int index = 0;
        for (; index < pointer; ++index) { str.append(" ").append(product.right.get(index).toString()); }
        str.append(" .");
        for (; index < product.right.size(); ++index) { str.append(" ").append(product.right.get(index).toString()); }
        str.append(", ").append(lookAhead.toString());
        return str.toString();
    }
}
