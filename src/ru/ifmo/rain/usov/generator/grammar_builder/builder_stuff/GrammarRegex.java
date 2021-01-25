package ru.ifmo.rain.usov.generator.grammar_builder.builder_stuff;

import java.util.ArrayList;
import java.util.List;

public class GrammarRegex {
    public static class Atom {
        public GrammarItem token = null;
        public String value = null;
        public GrammarRegex inner = null;

        public Atom(GrammarItem token) { this.token = token;}
        public Atom(String value) { this.value = value; }
        public Atom(GrammarRegex inner) {this.inner = inner; }
    }
    public String regex;
    public List<Atom> atoms;

    public GrammarRegex() { this.atoms = new ArrayList<>(); }

}
