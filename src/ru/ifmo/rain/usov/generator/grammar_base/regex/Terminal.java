package ru.ifmo.rain.usov.generator.grammar_base.regex;

import java.util.function.Predicate;

public class Terminal {
    public final Predicate<Character> matcher;
    public final RegexItem token;
    public final char value;

    public Terminal(RegexItem token) {
        this.value = '\0';
        this.token = token;
        switch (token) {
            case DIGIT: matcher = Character::isDigit; break;
            case LOWER: matcher = Character::isLowerCase; break;
            case UPPER: matcher = Character::isUpperCase; break;
            case LETTER: matcher = Character::isLetter; break;
            case NEWLINE: matcher = (x) -> x == '\n'; break;
            case TAB: matcher = (x) -> x == '\t'; break;
            case CARRIAGE: matcher = (x) -> x == '\r'; break;
            default:
                throw new AssertionError("Unknown multi terminal token:" + token.toString());
        }
    }

    public Terminal(char value) {
        this.value = value;
        this.token = RegexItem.TERMINAL;
        this.matcher = (x) -> x == this.value;
    }
}
