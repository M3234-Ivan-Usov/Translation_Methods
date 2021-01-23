package ru.ifmo.rain.usov.generator.grammar_builder.grammar_stuff;

import java.util.Objects;

public class GrammarUnit {
    public final boolean terminal;
    public final String value;
    public final RegexTerminal regexTerminal;

    public GrammarUnit(String value) {
        this.value = value;
        this.regexTerminal = null;
        this.terminal = false;
    }

    public GrammarUnit(RegexTerminal regexTerminal) {
        this.value = null;
        this.regexTerminal = regexTerminal;
        this.terminal = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o != null && o.getClass() == GrammarUnit.class) {
            GrammarUnit that = (GrammarUnit) o;
            if (this.value != null) { return this.value.equals(that.value); }
            else if (regexTerminal != null) { return this.regexTerminal.equals(that.regexTerminal); }
        }
        return false;
    }

    @Override
    public int hashCode() { return Objects.hash(terminal, value, regexTerminal); }
}
