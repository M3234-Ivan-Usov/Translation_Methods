package ru.ifmo.rain.usov.generator.lr_parser;

import ru.ifmo.rain.usov.generator.grammar_base.Attributable;
import ru.ifmo.rain.usov.generator.grammar_base.Product;
import ru.ifmo.rain.usov.generator.grammar_base.RuntimeProduct;

import java.util.Map;
import java.util.Objects;

public class ActionReduce<T extends Enum<T>, A extends Attributable<T>> implements LRAction {
    public final RuntimeProduct<T, A> runtimeProduct;
    public final Product<T, A> product;
    public boolean accept = false;
    private final LRParser<T, A> parser;


    public ActionReduce(Product<T, A> product, LRParser<T, A> parser) {
        this.runtimeProduct = new RuntimeProduct<>(product, parser.lexer.makeInstance(product.left));
        this.runtimeProduct.left.tree = runtimeProduct;
        this.parser = parser; this.product = product;
    }

    @Override
    public void perform() {
        for (int i = runtimeProduct.last; i >= 0; --i) {
            runtimeProduct.right.set(i, parser.viablePrefix.pop());
            parser.states.pop();
        }
        runtimeProduct.action.calculate(runtimeProduct);
        LRAutomaton.LRState<T, A> current = parser.states.peek();
        parser.viablePrefix.push(runtimeProduct.left);
        parser.states.push(current.move.get(product.left));
        parser.accepted = this.accept;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionReduce<?, ?> that = (ActionReduce<?, ?>) o;
        return Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(runtimeProduct, product, parser);
    }
}
