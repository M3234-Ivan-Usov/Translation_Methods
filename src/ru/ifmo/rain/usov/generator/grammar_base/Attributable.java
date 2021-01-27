package ru.ifmo.rain.usov.generator.grammar_base;

import java.util.Objects;

public abstract class Attributable<Token extends Enum<Token>> {
    public RuntimeProduct<Token, ? extends Attributable<Token>> tree;
    public Token token;
    public String string;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attributable<?> that = (Attributable<?>) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() { return Objects.hash(token); }

    public Attributable() {}
}
