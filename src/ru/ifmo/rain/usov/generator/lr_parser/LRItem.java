package ru.ifmo.rain.usov.generator.lr_parser;

import ru.ifmo.rain.usov.generator.grammar_base.Attributable;
import ru.ifmo.rain.usov.generator.grammar_base.Grammar;
import ru.ifmo.rain.usov.generator.grammar_base.Product;

import java.util.*;
import java.util.stream.Collectors;

public class LRItem<Token extends Enum<Token>, Attribute extends Attributable<Token>> {
    public final Product<Token, Attribute> product;
    public final int pointer;
    public final boolean terminator;
    public final Token next;
    public final boolean nextTerminal;
    public final Set<Token> lookAhead;
    private final List<Token> followers;
    private final Grammar<Token, Attribute> grammar;

    public LRItem(Product<Token, Attribute> product, int pointer,
                  Set<Token> lookAhead, Grammar<Token, Attribute> grammar) {
        this.product = product; this.pointer = pointer; this.grammar = grammar;
        this.terminator = pointer == product.right.size();
        this.next = (terminator)? null : product.right.get(pointer);
        this.nextTerminal = (!terminator) && grammar.terminals.contains(next);
        this.lookAhead = new HashSet<>(lookAhead);
        this.followers = terminator? null : new ArrayList<>(
                product.right.subList(pointer + 1, product.right.size()));
    }

    public Set<Token> afterNext() {
        if (terminator) { return Collections.emptySet(); }
        Set<Token> result = new HashSet<>();
        int lastIndex = followers.size();
        for (Token ahead: lookAhead) {
            followers.add(ahead);
            result.addAll(grammar.first(followers).getKey());
            followers.remove(lastIndex);
        }
        return result;
    }

    public static <Token extends Enum<Token>>  Set<Token> set(Token x) {
        Set<Token> result = new HashSet<>();
        result.add(x); return result;
    }

    public LRItem<Token, Attribute> advance() {
        return new LRItem<>(product, pointer + 1, lookAhead, grammar);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LRItem<?, ?> other = (LRItem<?, ?>) o;
        return pointer == other.pointer && Objects.equals(product, other.product);
    }

    @Override
    public int hashCode() { return Objects.hash(product, pointer, terminator); }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(product.left.toString() + " ->");
        int index = 0;
        for (; index < pointer; ++index) { str.append(" ").append(product.right.get(index).toString()); }
        str.append(" .");
        for (; index < product.right.size(); ++index) { str.append(" ").append(product.right.get(index).toString()); }
        str.append(" ,  ").append(lookAhead.stream().map(x -> (x == null)? "$" : x.toString())
                .collect(Collectors.joining(" / ")));
        return str.toString();
    }
}
