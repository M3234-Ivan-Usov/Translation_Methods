package ru.ifmo.rain.usov.generator.grammar_base.lexer;

import ru.ifmo.rain.usov.generator.grammar_base.regex.Terminal;

import java.util.*;
import java.util.stream.Collectors;

public class LexerDFA<Token extends Enum<Token>> {
    public static class DFAState<T extends Enum<T>> {
        public boolean terminator;
        public Set<LexerNDFA.NDFAState<T>> equivalent;
        public Map<Terminal, DFAState<T>> transitions;
        public Set<Terminal> terminals;
        public T token;

        public DFAState(Set<LexerNDFA.NDFAState<T>> equivalent) {
            this.equivalent = equivalent;
            this.transitions = new HashMap<>();
            this.collectTerminals();
        }

        public void collectTerminals() {
            this.terminals = equivalent.stream().map(state -> {
                if (state.terminator) { this.terminator = true; this.token = state.token; }
                return state.transitions.keySet();
            }).flatMap(Collection::stream).collect(Collectors.toSet());
            this.terminals.remove(null);
        }

        public DFAState() {
            this.equivalent = new HashSet<>();
            this.transitions = new HashMap<>();
            this.terminals = new HashSet<>();
        }

        public DFAState<T> move(char x) {
            for (Map.Entry<Terminal, DFAState<T>> move: transitions.entrySet()) {
                if (move.getKey().matcher.test(x)) { return move.getValue(); }
            }
            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DFAState<?> other = (DFAState<?>) o;
            return terminator == other.terminator && Objects.equals(equivalent, other.equivalent);
        }

        @Override
        public int hashCode() { return Objects.hash(terminator, equivalent); }
    }

    public final DFAState<Token> initial;
    public final Map<DFAState<Token>, DFAState<Token>> states;

    public LexerDFA(LexerNDFA<Token> automaton) {
        this.initial = new DFAState<>(automaton.epsilonClosure(Set.of(automaton.initial)));
        this.states = new HashMap<>();
        states.put(initial, initial);
        Queue<LexerDFA.DFAState<Token>> statesQueue = new ArrayDeque<>();
        statesQueue.offer(initial);
        while (!statesQueue.isEmpty()) {
            DFAState<Token> current = statesQueue.poll();
            for (Terminal term : current.terminals) {
                DFAState<Token> candidate = new DFAState<>();
                current.equivalent.forEach(inner -> candidate.equivalent
                        .addAll(inner.transitions.getOrDefault(term, Collections.emptySet())));
                candidate.equivalent = automaton.epsilonClosure(candidate.equivalent);
                candidate.collectTerminals();
                DFAState<Token> similarState = states.get(candidate);
                if (similarState != null) { current.transitions.put(term, similarState); }
                else {
                    current.transitions.put(term, candidate);
                    states.put(candidate, candidate);
                    statesQueue.offer(candidate);
                }
            }
        }
    }
}
