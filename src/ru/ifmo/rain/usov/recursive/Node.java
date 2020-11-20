package ru.ifmo.rain.usov.recursive;

import java.util.Arrays;
import java.util.List;

/**
 * Components of {@link SyntaxAnalyser} tree
 */
public class Node {
    public String lexeme;
    public List<Node> children;

    public Node(String lexeme, Node... children) {
        this.lexeme = lexeme;
        this.children = Arrays.asList(children);
    }
}
