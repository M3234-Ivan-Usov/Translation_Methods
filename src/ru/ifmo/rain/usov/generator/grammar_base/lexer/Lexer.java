package ru.ifmo.rain.usov.generator.grammar_base.lexer;

import ru.ifmo.rain.usov.generator.grammar_base.Attributable;
import ru.ifmo.rain.usov.generator.grammar_base.regex.Regex;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Lexer<Token extends Enum<Token>, Attribute extends Attributable<Token>> {
    protected Map<Token, Regex<Token>> regexMap;
    private Constructor<Attribute> install;
    public LexerDFA<Token> automaton;
    private char[] input;
    public int length;
    public int position;

    public Lexer(Class<Attribute> clazz) {
        try {
            this.install = clazz.getDeclaredConstructor();
            this.regexMap = new HashMap<>();
        }
        catch (NoSuchMethodException ignored) {}
    }

    public Attribute nextToken() {
        LexerDFA.DFAState<Token> current = automaton.initial;
        Map.Entry<LexerDFA.DFAState<Token>, Integer> lastTerminal = null;
        int oldPosition = position;
        for (; position != length; ++position) {
            assert current != null;
            current = current.move(input[position]);
            if (current == null) { return recall(lastTerminal, oldPosition); }
            else if (current.terminator) { lastTerminal = Map.entry(current, position + 1); }
        }
        return recall(lastTerminal, oldPosition);
    }

    private Attribute recall(Map.Entry<LexerDFA.DFAState<Token>, Integer> last, int oldPosition) {
        if (last == null) { return null; }
        try {
            position = last.getValue();
            Attribute attribute = install.newInstance();
            attribute.token = last.getKey().token;
            attribute.string = new String(input, oldPosition, position - oldPosition);
            return attribute;
        }
        catch (InstantiationException | InvocationTargetException | IllegalAccessException ignored) {}
        return null;
    }

    public void load(String input) {
        this.input = input.toCharArray();
        this.length = this.input.length;
        this.position = 0;
    }

    protected void buildAutomaton() {
        this.automaton = new LexerDFA<>(regexMap.values().stream().map(Regex::buildAutomaton)
                .collect(Collectors.toSet()).stream().reduce(LexerNDFA::join).orElseThrow());
    }
}
