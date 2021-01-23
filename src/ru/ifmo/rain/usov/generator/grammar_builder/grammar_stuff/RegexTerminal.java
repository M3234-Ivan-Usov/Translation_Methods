package ru.ifmo.rain.usov.generator.grammar_builder.grammar_stuff;


import java.util.Stack;

public class RegexTerminal {
    public Stack<Reg> atoms;
    public String regex;

    public RegexTerminal() { this.atoms = new Stack<>(); }

}
