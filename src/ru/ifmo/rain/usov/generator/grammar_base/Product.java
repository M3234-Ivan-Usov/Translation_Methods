package ru.ifmo.rain.usov.generator.grammar_base;

import java.util.*;

public class Product<Token extends Enum<Token>, Attribute extends Attributable<Token>> {
    public final Token left;
    public final List<Token> right;
    public final boolean epsilon;
    public AttributeAction<Token, Attribute> action;

    @SafeVarargs
    public Product(Token left, Token... right) {
        this.left = left;
        this.right = new ArrayList<>();
        if (right.length == 0) { this.epsilon = true; }
        else { this.epsilon = false; Collections.addAll(this.right, right); }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return left == product.left && Objects.equals(right, product.right) && epsilon == product.epsilon;
    }

    @Override
    public int hashCode() { return Objects.hash(left, right, epsilon); }
}
