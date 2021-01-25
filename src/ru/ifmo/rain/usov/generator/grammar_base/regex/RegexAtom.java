package ru.ifmo.rain.usov.generator.grammar_base.regex;

import java.util.ArrayList;
import java.util.List;

public class RegexAtom<Token extends Enum<Token>> {
    public final List<Terminal> atom;
    public final RegexItem token;
    public final Regex<Token> internal;
    public RegexItem modifier;

    public RegexAtom(RegexItem token) {
        if (token == RegexItem.ALTER || Regex.modifier(token)) { this.atom = null; this.token = token; }
        else { this.atom = List.of(new Terminal(token)); this.token = RegexItem.TERMINAL; }
        this.internal = null;
    }

    public RegexAtom(String value) {
        this.atom = new ArrayList<>();
        for (char c: value.toCharArray()) { atom.add(new Terminal(c)); }
        this.token = RegexItem.TERMINAL;
        this.internal = null;
    }

    public RegexAtom(Regex<Token> internal) {
        this.atom = null;
        this.token = RegexItem.REGEX;
        this.internal = internal;
    }
}
