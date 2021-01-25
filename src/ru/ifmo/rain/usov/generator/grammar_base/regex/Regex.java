package ru.ifmo.rain.usov.generator.grammar_base.regex;


import ru.ifmo.rain.usov.generator.grammar_base.lexer.LexerNDFA;

import java.util.*;

public class Regex<Token extends Enum<Token>> {
    public Deque<RegexAtom<Token>> atoms;
    //public final String regex;
    public Token terminalToken;


    public static boolean modifier(RegexItem token) {
        switch (token) {
            case QUESTION: case PLUS: case WILDCARD: return true;
            default: return false;
        }
    }

    @SafeVarargs
    public Regex(Token terminalToken, RegexAtom<Token> ... atoms) {
        this.terminalToken = terminalToken;
       // this.regex = regex;
        this.atoms = new ArrayDeque<>();
        for (RegexAtom<Token> atom : atoms) {
            if (modifier(atom.token)) { this.atoms.getLast().modifier = atom.token; }
            else { this.atoms.offerLast(atom); }
        }
    }

    public LexerNDFA<Token> buildAutomaton() {
        Stack<LexerNDFA<Token>> builder = new Stack<>();
        builder.push(LexerNDFA.epsilon());
        while (!atoms.isEmpty()) {
            RegexAtom<Token> current = atoms.pollFirst();
            LexerNDFA<Token> last = builder.pop(), next;
            if (current.token == RegexItem.REGEX && current.internal != null) { next = current.internal.buildAutomaton(); }
            else if (current.token == RegexItem.TERMINAL) { next = LexerNDFA.simple(current.atom); }
            else { throw new IllegalArgumentException("Failed to build lexer, unexpected " + current.token); }
            next = modify(current, next);
            if (!atoms.isEmpty() && atoms.getFirst().token == RegexItem.ALTER) {
                next = LexerNDFA.alter(last, next); atoms.pollFirst();
            }
            else { next = LexerNDFA.concat(last, next); }
            builder.push(next);
        }
        builder.peek().terminators.forEach(terminator -> terminator.token = terminalToken);
        return builder.pop();
    }

    private static <T extends Enum<T>> LexerNDFA<T> modify(RegexAtom<T> atom, LexerNDFA<T> automaton) {
        if (atom.modifier == null) { return automaton; }
        switch (atom.modifier) {
            case WILDCARD: return LexerNDFA.wildcard(automaton);
            case QUESTION: return LexerNDFA.question(automaton);
            case PLUS: return LexerNDFA.plus(automaton);
            default: return automaton;
        }
    }
}
