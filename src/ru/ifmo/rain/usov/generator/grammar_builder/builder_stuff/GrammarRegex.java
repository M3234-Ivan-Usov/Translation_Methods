package ru.ifmo.rain.usov.generator.grammar_builder.builder_stuff;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GrammarRegex {
    public static class Atom {
        public GrammarItem token = null;
        public String value = null;
        public GrammarRegex inner = null;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Atom atom = (Atom) o;
            return token == atom.token && Objects.equals(value, atom.value) && Objects.equals(inner, atom.inner);
        }

        @Override
        public int hashCode() { return Objects.hash(token, value, inner); }

        public Atom(GrammarItem token) { this.token = token;}
        public Atom(String value) { this.value = value; }
        public Atom(GrammarRegex inner) {this.inner = inner; }
    }
    public String regex;
    public List<Atom> atoms;

    public GrammarRegex() { this.atoms = new ArrayList<>(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrammarRegex that = (GrammarRegex) o;
        return Objects.equals(regex, that.regex) && Objects.equals(atoms, that.atoms);
    }

    @Override
    public int hashCode() { return Objects.hash(regex, atoms); }
}
