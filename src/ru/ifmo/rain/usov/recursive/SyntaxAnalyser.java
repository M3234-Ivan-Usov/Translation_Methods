package ru.ifmo.rain.usov.recursive;

import java.text.ParseException;

/**
 * Recursive descent for Python logical expressions
 * ----------------------------------------
 * O -> O or X | X, binary operation OR
 * X -> X xor A | A, binary operation XOR
 * A -> A and N | N, binary operation AND
 * N -> not N | V, unary operation NOT
 * V -> (O) | [a-z], variable or expression
 * ----------------------------------------
 * O -> XO'
 * O' -> or XO' | e
 * X -> AX'
 * X' -> xor AX' | e
 * A -> NA'
 * A' -> and NA' | e
 * N -> not N | V
 * V -> [a-z] | (O)
 * ----------------------------------------
 * FIRST                  FOLLOW
 * O:  (, [a-z], not      ), $
 * O': or, $              ), $
 * X:  (, [a-z], not      ), $, or
 * X': xor, $             ), $, or
 * A:  (, [a-z], not      ), $, or, xor
 * A': and, $             ), $, or, xor
 * N:  not, [a-z], (      ), $, or, xor, and, not
 * V:  [a-z], (           ), $, or, xor, and
 */
public class SyntaxAnalyser {
    /**
     * Instance of {@link LexicalAnalyser}
     */
    private LexicalAnalyser lexicalAnalyser;

    /**
     * Launch parser, each time a new instance of {@link LexicalAnalyser} is created
     * @param expression Expression to parse
     * @return Root of parser tree, which consists of {@link Node}
     * @throws ParseException If input expression has invalid syntax
     */
    public Node parse(String expression) throws ParseException {
        lexicalAnalyser = new LexicalAnalyser(expression);
        lexicalAnalyser.nextToken();
        Node root = O();
        if (lexicalAnalyser.token != Token.END) {
            throw new ParseException("Parse error: " + lexicalAnalyser.lexeme, lexicalAnalyser.position);
        }
        return root;
    }

    /**
     * O -> XO'
     * @return O-nonterminal subtree
     * @throws ParseException If input expression has invalid syntax
     */
    private Node O() throws ParseException {
        switch (lexicalAnalyser.token) {
            case LEFT:
            case VARIABLE:
            case NOT:
                Node left = X();
                Node right = OPrime();
                return (right == null) ? left : new Node("or", left, right);
            default:
                throw new ParseException("Unexpected lexeme: " +
                        lexicalAnalyser.lexeme, lexicalAnalyser.position);
        }
    }

    /**
     * O' -> or XO' | e
     * @return O'-nonterminal subtree
     * @throws ParseException If input expression has invalid syntax
     */
    private Node OPrime() throws ParseException {
        if (lexicalAnalyser.token == Token.OR) {
            lexicalAnalyser.nextToken();
            Node left = X();
            Node right = OPrime();
            return (right == null) ? left : new Node("or", left, right);
        }
        return null;
    }

    /**
     * X -> AX'
     * @return X-nonterminal subtree
     * @throws ParseException If input expression has invalid syntax
     */
    private Node X() throws ParseException {
        switch (lexicalAnalyser.token) {
            case LEFT:
            case VARIABLE:
            case NOT:
                Node left = A();
                Node right = XPrime();
                return (right == null) ? left : new Node("xor", left, right);
            default:
                throw new ParseException("Unexpected lexeme: " +
                        lexicalAnalyser.lexeme, lexicalAnalyser.position);
        }
    }

    /**
     * X' -> xor AX' | e
     * @return X'-nonterminal subtree
     * @throws ParseException If input expression has invalid syntax
     */
    private Node XPrime() throws ParseException {
        if (lexicalAnalyser.token == Token.XOR) {
            lexicalAnalyser.nextToken();
            Node left = A();
            Node right = XPrime();
            return (right == null) ? left : new Node("xor", left, right);
        }
        return null;
    }

    /**
     * A -> NA'
     * @return A-nonterminal subtree
     * @throws ParseException If input expression has invalid syntax
     */
    private Node A() throws ParseException {
        switch (lexicalAnalyser.token) {
            case LEFT:
            case VARIABLE:
            case NOT:
                Node left = N();
                Node right = APrime();
                return (right == null) ? left : new Node("and", left, right);
            default:
                throw new ParseException("Unexpected lexeme: " +
                        lexicalAnalyser.lexeme, lexicalAnalyser.position);
        }
    }

    /**
     * A' -> and NA' | e
     * @return A'-nonterminal subtree
     * @throws ParseException If input expression has invalid syntax
     */
    private Node APrime() throws ParseException {
        if (lexicalAnalyser.token == Token.AND) {
            lexicalAnalyser.nextToken();
            Node left = N();
            Node right = APrime();
            return (right == null) ? left : new Node("and", left, right);
        }
        return null;
    }

    /**
     * N -> not N | V'
     * @return O-nonterminal subtree
     * @throws ParseException If input expression has invalid syntax
     */
    private Node N() throws ParseException {
        switch (lexicalAnalyser.token) {
            case LEFT:
            case VARIABLE:
                return V();
            case NOT:
                lexicalAnalyser.nextToken();
                return new Node("not", N());
            default:
                throw new ParseException("Unexpected lexeme: " +
                        lexicalAnalyser.lexeme, lexicalAnalyser.position);
        }
    }

    /**
     * V -> [a-z] | (O)
     * @return V-nonterminal subtree
     * @throws ParseException If input expression has invalid syntax
     */
    private Node V() throws ParseException {
        switch (lexicalAnalyser.token) {
            case VARIABLE:
                String variable = lexicalAnalyser.lexeme;
                lexicalAnalyser.nextToken();
                return new Node(variable);
            case LEFT:
                lexicalAnalyser.nextToken();
                Node subTree = O();
                if (lexicalAnalyser.token != Token.RIGHT) {
                    throw new ParseException("Expected right parentheses", lexicalAnalyser.position);
                }
                lexicalAnalyser.nextToken();
                return subTree;
            default:
                throw new ParseException("Unexpected lexeme: " +
                        lexicalAnalyser.lexeme, lexicalAnalyser.position);
        }
    }
}
