package ru.ifmo.rain.usov.generator.grammar_builder;

import java.util.*;

public class Product {
    public final Token left;
    public final List<Token> right;
    public final boolean epsilon;

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
        return left == product.left && Objects.equals(right, product.right);
    }

    @Override
    public int hashCode() { return Objects.hash(left, right); }
}
