package ru.ifmo.rain.usov.generator.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Product {
    public Token left;
    public List<Token> right;

    public Product(Token left, Token... right) {
        this.left = left;
        this.right = new ArrayList<>();
        Collections.addAll(this.right, right);
    }
}