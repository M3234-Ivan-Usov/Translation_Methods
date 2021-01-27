package ru.ifmo.rain.usov.generator.grammar_base;

import java.util.ArrayList;
import java.util.List;

public class RuntimeProduct<Token extends Enum<Token>, Attribute extends Attributable<Token>> {
    public Attribute left;
    public List<Attribute> right;
    public int last;
    public AttributeAction<Token, Attribute> action;

    public RuntimeProduct(Product<Token, Attribute> origin, Attribute left) {
        this.left = left;
        this.right = new ArrayList<>();
        for (int i = 0; i < origin.right.size(); ++i) { this.right.add(null); }
        this.last = origin.right.size() - 1;
        this.action = origin.action;
    }
}
