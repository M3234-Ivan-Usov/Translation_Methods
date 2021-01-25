package ru.ifmo.rain.usov.generator.grammar_base.lexer;

import ru.ifmo.rain.usov.generator.grammar_base.regex.Terminal;

import java.util.*;

public class LexerNDFA<Token extends Enum<Token>> {
    public static class NDFAState<T extends Enum<T>> {
        public Map<Terminal, Set<NDFAState<T>>> transitions;
        public T token;
        public boolean terminator;

        public NDFAState() { this.transitions = new HashMap<>(); }
    }

    public NDFAState<Token> initial;
    public Set<NDFAState<Token>> terminators;
    public Set<NDFAState<Token>> states;

    public static <T extends Enum<T>> LexerNDFA<T> epsilon() {
        LexerNDFA<T> automaton = new LexerNDFA<>();
        NDFAState<T> next = automaton.finish();
        automaton.initial.transitions.put(null, set(next));
        return automaton;
    }

    public static <T extends Enum<T>> LexerNDFA<T> simple(List<Terminal> atom) {
        LexerNDFA<T> automaton = new LexerNDFA<>();
        NDFAState<T> current = automaton.initial;
        for (Terminal term: atom) {
            NDFAState<T> next = new NDFAState<>();
            current.transitions.put(term, set(next));
            current = next;
        }
        automaton.terminators.add(current);
        automaton.states.add(current);
        return automaton;
    }

    public static <T extends Enum<T>> LexerNDFA<T> concat(LexerNDFA<T> first, LexerNDFA<T> second) {
        LexerNDFA<T> automaton = new LexerNDFA<>(first, second);
        NDFAState<T> next = automaton.finish();
        automaton.initial.transitions.put(null, set(first.initial));
        first.terminators.forEach(terminator -> put(terminator, set(second.initial)));
        second.terminators.forEach(terminator -> put(terminator, set(next)));
        return automaton;
    }

    public static <T extends Enum<T>> LexerNDFA<T> alter(LexerNDFA<T> first, LexerNDFA<T> second) {
        LexerNDFA<T> automaton = new LexerNDFA<>(first, second);
        NDFAState<T> next = automaton.finish();
        automaton.initial.transitions.put(null, set(first.initial, second.initial));
        second.terminators.forEach((terminator) -> put(terminator, set(next)));
        first.terminators.forEach((terminator) -> put(terminator, set(next)));
        return automaton;
    }

    public static <T extends Enum<T>> LexerNDFA<T> join(LexerNDFA<T> first, LexerNDFA<T> second) {
        LexerNDFA<T> automaton = new LexerNDFA<>(first, second);
        automaton.initial.transitions.put(null, set(first.initial, second.initial));
        automaton.terminators.addAll(first.terminators);
        automaton.terminators.addAll(second.terminators);
        automaton.terminators.forEach(state -> state.terminator = true);
        return automaton;
    }

    public static <T extends Enum<T>> LexerNDFA<T> wildcard(LexerNDFA<T> automaton) {
        LexerNDFA<T> kliniAutomaton = new LexerNDFA<>(automaton);
        NDFAState<T> next = kliniAutomaton.finish();
        next.transitions.put(null, set(kliniAutomaton.initial));
        kliniAutomaton.initial.transitions.put(null, set(next, automaton.initial));
        automaton.terminators.forEach((terminator) -> put(terminator, set(next)));
        return kliniAutomaton;
    }

    public static <T extends Enum<T>> LexerNDFA<T> question(LexerNDFA<T> automaton) {
        LexerNDFA<T> optionalAutomaton = new LexerNDFA<>(automaton);
        NDFAState<T> next = optionalAutomaton.finish();
        optionalAutomaton.initial.transitions.put(null, set(next, automaton.initial));
        automaton.terminators.forEach(terminator -> put(terminator, set(next)));
        return optionalAutomaton;
    }

    public static <T extends Enum<T>> LexerNDFA<T> plus(LexerNDFA<T> automaton) {
        LexerNDFA<T> manyAutomaton = new LexerNDFA<>(automaton);
        NDFAState<T> next = manyAutomaton.finish();
        manyAutomaton.initial.transitions.put(null, set(automaton.initial));
        automaton.terminators.forEach(terminator -> put(terminator, set(next)));
        next.transitions.put(null, set(manyAutomaton.initial));
        return manyAutomaton;
    }

    @SafeVarargs
    private LexerNDFA(LexerNDFA<Token>... others) {
        this.terminators = new HashSet<>();
        this.initial = new NDFAState<>();
        this.states = set(initial);
        for (LexerNDFA<Token> automaton: others) { states.addAll(automaton.states); }
    }

    public Set<NDFAState<Token>> epsilonClosure(Set<NDFAState<Token>> states) {
        Set<NDFAState<Token>> closure = new HashSet<>(states);
        Queue<NDFAState<Token>> q = new ArrayDeque<>(closure);
        while (!q.isEmpty()) {
            NDFAState<Token> next = q.poll();
            for (NDFAState<Token> eps : next.transitions.getOrDefault(null, Collections.emptySet())) {
                if (closure.add(eps)) { q.offer(eps); }
            }
        }
        return closure;
    }

    private NDFAState<Token> finish() {
        NDFAState<Token> last = new NDFAState<>();
        terminators.add(last);
        states.add(last);
        return last;
    }

    @SafeVarargs
    private static <T extends Enum<T>>  Set<NDFAState<T>> set(NDFAState<T>... state) {
        Set<NDFAState<T>> set = new HashSet<>();
        Collections.addAll(set, state); return set;
    }

    private static <T extends Enum<T>> void put(NDFAState<T> terminator, Set<NDFAState<T>> append) {
        Set<NDFAState<T>> x = terminator.transitions.getOrDefault(null, null);
        if (x == null) { terminator.transitions.put(null, append); }
        else x.addAll(append);
    }
}
