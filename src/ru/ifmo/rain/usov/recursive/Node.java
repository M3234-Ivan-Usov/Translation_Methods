package ru.ifmo.rain.usov.recursive;

import edu.uci.ics.jung.graph.DelegateTree;

import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Components of {@link SyntaxAnalyser} tree
 */
public class Node {
    public String lexeme;
    public LinkedList<Node> children;

    public Node(String lexeme, Node... children) {
        this.lexeme = lexeme;
        this.children = new LinkedList<>(Arrays.asList(children));
    }

    public int plotSubtree(DelegateTree<String, String> graph, String parent, int uniqueCounter) {
        int vertexNum = uniqueCounter;
        String vertexId = vertexNum + ": " + lexeme;
        String edgeId =  String.valueOf(uniqueCounter);
        graph.addChild(edgeId, parent, vertexId);
        for (Node next : children) { uniqueCounter = next.plotSubtree(graph, vertexId, uniqueCounter + 1); }
        return uniqueCounter;
    }

    public static void main(String[] args) {
        String expression = "not (not a)";
        SyntaxAnalyser syntaxAnalyser = new SyntaxAnalyser();
        try {
            Node root = syntaxAnalyser.parse(expression);
            UnitTest.plotTree(root, expression).setVisible(true);
        } catch (ParseException e) {
            System.out.println("Syntax error in expression: " + expression);
        }
    }
}
