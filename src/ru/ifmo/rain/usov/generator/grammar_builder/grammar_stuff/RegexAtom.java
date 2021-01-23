package ru.ifmo.rain.usov.generator.grammar_builder.grammar_stuff;

import java.util.function.Predicate;

public class RegexAtom {
    public final Predicate<String> matcher;
    public final String value;
    public final GrammarItem token;

    public RegexAtom(GrammarItem token) {
        this.value = null;
        this.token = token;
        switch (token) {
            case DIGIT:
                matcher = (x) -> Character.isDigit(x.charAt(0));
                break;
            case LOWER:
                matcher = (x) -> Character.isLowerCase(x.charAt(0));
                break;
            case UPPER:
                matcher = (x) -> Character.isUpperCase(x.charAt(0));
                break;
            case LETTER:
                matcher = (x) -> Character.isLetter(x.charAt(0));
                break;
            default:
                throw new AssertionError("Unknown multi terminal token");
        }
    }

    public RegexAtom(String value, GrammarItem token) {
        this.value = value;
        this.matcher = value::equals;
        this.token = token;
    }
}
