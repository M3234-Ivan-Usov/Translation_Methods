package ru.ifmo.rain.usov.generator.grammar_builder.grammar_stuff;

import ru.ifmo.rain.usov.generator.grammar_builder.Builder;

public class Reg {
    public final String value;
    public final GrammarItem token;
    public final boolean meta;
    public final RegexTerminal internal;

    public Reg(GrammarItem token) {
        this.value = null;
        this.token = token;
        this.meta = Builder.escapers.contains(token);
        this.internal = null;
    }

    public Reg(String value) {
        this.value = value;
        this.token = GrammarItem.QUOTE;
        this.meta = false;
        this.internal = null;
    }

    public Reg(RegexTerminal internal) {
        this.value = null;
        this.token = null;
        this.meta = true;
        this.internal = internal;
    }
}
