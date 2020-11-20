package ru.ifmo.rain.usov.recursive;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.List;

public class UnitTest {
    private static SyntaxAnalyser syntaxAnalyser = new SyntaxAnalyser();

    /**
     *    Expected:
     *      or
     *    /    \
     *   a      xor
     *         /   \
     *       and    d
     *      /   \
     *     b     c
     * @throws ParseException expected no ParseException
     */
    @Test
    public void test01_simple() throws ParseException {
        String expression = "a or b and c xor d";
        Node root = syntaxAnalyser.parse(expression);

        assertNotNull(root);
        assertEquals("or", root.lexeme);
        List<Node> depth_one = root.children;
        assertEquals("a", depth_one.get(0).lexeme);

        Node right = depth_one.get(1);
        assertEquals("xor", right.lexeme);
        assertEquals("d", right.children.get(1).lexeme);

        Node rightLeft = right.children.get(0);
        assertEquals("and", rightLeft.lexeme);
        assertEquals("b", rightLeft.children.get(0).lexeme);
        assertEquals("c", rightLeft.children.get(1).lexeme);
    }

    /**
     *      Expected:
     *         or
     *       /   \
     *     not   and
     *    /     /   \
     *   a     b    not
     *             /
     *           not
     *          /
     *         c
     * @throws ParseException expected no ParseException
     */
    @Test
    public void test02_simple() throws ParseException {
        String expression = "not a or b and not not c";
        Node root = syntaxAnalyser.parse(expression);

        assertEquals("or", root.lexeme);
        assertEquals("not", root.children.get(0).lexeme);
        assertEquals("a", root.children.get(0).children.get(0).lexeme);

        Node right = root.children.get(1);
        assertEquals("and", right.lexeme);
        assertEquals("b", right.children.get(0).lexeme);

        Node doubleNot = right.children.get(1);
        assertEquals("not", doubleNot.lexeme);
        assertEquals("not", doubleNot.children.get(0).lexeme);
        assertEquals("c", doubleNot.children.get(0).children.get(0).lexeme);
    }

    /**
     *      Expected
     *         and
     *       /     \
     *     xor      d
     *    /   \
     *   a    or
     *      /   \
     *     b     c
     * @throws ParseException expected no ParseException
     */
    @Test
    public void test03_brackets() throws ParseException {
        String expression = "(a xor (b or c)) and d";
        Node root = syntaxAnalyser.parse(expression);

        assertEquals("and", root.lexeme);
        assertEquals("d", root.children.get(1).lexeme);

        Node left = root.children.get(0);
        assertEquals("xor", left.lexeme);
        assertEquals("a", left.children.get(0).lexeme);

        Node leftRight = left.children.get(1);
        assertEquals("or", leftRight.lexeme);
        assertEquals("b", leftRight.children.get(0).lexeme);
        assertEquals("c", leftRight.children.get(1).lexeme);
    }

    @Test(expected = ParseException.class)
    public void test04_syntax() throws ParseException {
        String expression = "a or b and or c";
        syntaxAnalyser.parse(expression);
    }

    @Test(expected = ParseException.class)
    public void test05_syntax() throws ParseException {
        String expression = "a or b or and c";
        syntaxAnalyser.parse(expression);
    }

    @Test(expected = ParseException.class)
    public void test06_syntax() throws ParseException {
        String expression = "a or b not and c";
        syntaxAnalyser.parse(expression);
    }

    @Test(expected = ParseException.class)
    public void test07_brackets() throws ParseException {
        String expression = "a or (b and c)) xor d";
        syntaxAnalyser.parse(expression);
    }

    @Test
    public void test08_brackets() throws ParseException {
        String expression = "a or (b and (c) xor d)";
        syntaxAnalyser.parse(expression);
    }

    @Test(expected = ParseException.class)
    public void test09_syntax() throws ParseException {
        String expression = "a not b or c";
        syntaxAnalyser.parse(expression);
    }

    @Test(expected = ParseException.class)
    public void test10_empty() throws ParseException {
        syntaxAnalyser.parse("");
    }

    @Test
    public void test11_single_variable() throws ParseException {
        syntaxAnalyser.parse("a");
    }

    @Test(expected = ParseException.class)
    public void test12_single_key_word() throws ParseException {
        syntaxAnalyser.parse("xor");
    }

    @Test(expected = ParseException.class)
    public void test13_empty() throws ParseException {
        syntaxAnalyser.parse("a or ()");
    }
}
