package ru.ifmo.rain.usov.generator.grammar_base;

@FunctionalInterface
public interface AttributeAction<Token extends Enum<Token>, Attribute extends Attributable<Token>> {
    void calculate(RuntimeProduct<Token, Attribute> product);
}
