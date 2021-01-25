package ru.ifmo.rain.usov.generator.grammar_base;

public abstract class Attributable<Token extends Enum<Token>> {
    public Token token;
    public String string;

    public Attributable() {}
}
