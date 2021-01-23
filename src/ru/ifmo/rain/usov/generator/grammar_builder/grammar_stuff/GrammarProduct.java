package ru.ifmo.rain.usov.generator.grammar_builder.grammar_stuff;

import java.util.ArrayList;
import java.util.List;

public class GrammarProduct {
    public final GrammarUnit left;
    public final List<GrammarUnit> right;

    public GrammarProduct(GrammarUnit left) {
        this.left = left;
        this.right = new ArrayList<>();
    }
}
