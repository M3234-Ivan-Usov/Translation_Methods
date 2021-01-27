package ru.ifmo.rain.usov.generator.grammar_builder.builder_stuff;

import java.util.ArrayList;
import java.util.List;

public class GrammarProduct {
    public final GrammarUnit left;
    public final List<GrammarUnit> right;
    public final List<String> actions;

    public GrammarProduct(GrammarUnit left) {
        this.left = left;
        this.right = new ArrayList<>();
        this.actions = new ArrayList<>();
    }

    public void newAction(String action) {
        String adapted = action.strip().replace("@it", "p.left");
        for (int index = 0; index < right.size(); ++index) {
            adapted = adapted.replace("@" + index, "p.right.get(" + index + ")");
        }
        actions.add(adapted);
    }
}
