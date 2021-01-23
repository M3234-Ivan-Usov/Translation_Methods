package ru.ifmo.rain.usov.generator.lr_parser;

import ru.ifmo.rain.usov.generator.grammar_builder.Product;

public class ActionReduce implements LRAction {
    private final LRParser parser;
    private final Product product;

    public ActionReduce(LRParser parser, Product product) {
        this.parser = parser;
        this.product = product;
    }

    @Override
    public void perform() {
        for (int i = 0; i < product.right.size(); ++i) { parser.prefix.pop(); }
    }
}
