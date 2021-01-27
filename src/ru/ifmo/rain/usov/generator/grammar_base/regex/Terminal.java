package ru.ifmo.rain.usov.generator.grammar_base.regex;

import java.util.Objects;
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

 /*   @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Terminal terminal = (Terminal) o;
        return value == terminal.value &&
                Objects.equals(matcher, terminal.matcher) &&
                token == terminal.token;
    }

    @Override
    public int hashCode() {
        return Objects.hash(matcher, token, value);
    }*/

    public Terminal(char value) {
        this.value = value;
        /*if (Character.isLowerCase(value)) { this.token = RegexItem.LOWER; }
        else if (Character.isUpperCase(value)) { this.token = RegexItem.UPPER; }
        else if (Character.isLetter(value)) { this.token = RegexItem.LETTER; }
        else if (Character.isDigit(value)) { this.token = RegexItem.DIGIT; }
        else if (value == '\n') { this.token = RegexItem.NEWLINE; }
        else if (value == '\t') { this.token = RegexItem.TAB; }
        else if (value == '\r') { this.token = RegexItem.CARRIAGE; }
        else {*/ this.token = RegexItem.TERMINAL; //}
        this.matcher = (x) -> x == this.value;
    }
}
