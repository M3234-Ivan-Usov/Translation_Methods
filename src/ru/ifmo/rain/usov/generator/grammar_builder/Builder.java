package ru.ifmo.rain.usov.generator.grammar_builder;

import ru.ifmo.rain.usov.generator.grammar_builder.codegen.AttributeGen;
import ru.ifmo.rain.usov.generator.grammar_builder.codegen.GrammarGen;
import ru.ifmo.rain.usov.generator.grammar_builder.codegen.LexerGen;
import ru.ifmo.rain.usov.generator.grammar_builder.codegen.TokenGen;
import ru.ifmo.rain.usov.generator.grammar_builder.builder_stuff.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;

/**
 * root -> product ";" root
 * root -> def root
 * root -> attr root
 * root -> start root
 * def -> "@def" "{" _any_ "}"
 * attr -> "@attr" "{" _any_ "}"
 * start -> "@start" _lower_
 * root -> _eps_
 * product -> left "->" right
 * left -> nonterm
 * right -> nonterm right
 * right -> term_regex right
 * right -> _eps_
 * nonterm -> _lower_
 * term_regex -> regex
 * regex -> seq regex'
 * regex' -> "|" seq regex'
 * regex' -> _eps_
 * seq -> term meta seq
 * seq -> "(" regex ")" meta seq
 * seq -> _eps_
 * meta -> "?" | "*" | "+" | _eps_
 * term -> _escapers
 * term -> "'" _any_ "'"
 *
 *           FIRST
 * root ->        word, $
 * product ->     word
 * left ->        word
 * nonterm ->     word
 * right ->       word, quote, special, (, |
 * term_regex ->  quote, special, (, |
 * regex ->       quote, special, (
 * regex' ->      |
 * seq ->         quote, special, (
 * meta ->        ?, +, *
 * term ->        quote, special
 */
public class Builder {
    private GrammarLex lexer;
    private Set<GrammarUnit> units;
    private List<GrammarProduct> products;
    private Map<GrammarUnit, Set<GrammarProduct>> entries;
    private GrammarProduct currentProduct;
    private GrammarRegex currentRegex;
    private String lowerName;
    private String camelName;
    private List<String> attributes;
    private Path workingDir;
    private GrammarUnit start;

    public static Set<GrammarItem> escapers = Set.of(
            GrammarItem.LOWER, GrammarItem.UPPER, GrammarItem.LETTER, GrammarItem.DIGIT,
            GrammarItem.NEWLINE, GrammarItem.CARRIAGE, GrammarItem.TAB
    );


    public Builder(String rawGrammar) throws ParseException {
        this.units = new HashSet<>();
        this.products = new ArrayList<>();
        this.entries = new HashMap<>();
        this.lexer = new GrammarLex(rawGrammar);
        String temp = lexer.readHeader();
        char first = temp.charAt(0);
        this.camelName = Character.toUpperCase(first) + temp.substring(1);
        this.lowerName = Character.toLowerCase(first) + temp.substring(1);
        this.attributes = new ArrayList<>();
        lexer.nextToken(); root();
    }

    public void generate(Path parent, String parentPackage) throws IOException {
        GrammarUnit augment = new GrammarUnit("augment");
        GrammarProduct zero = new GrammarProduct(augment);
        zero.right.add(start); zero.newAction("@it = @0;");
        products.add(0, zero);
        units.add(augment); entries.put(augment, Set.of(zero));
        this.workingDir = Path.of(parent.toString(), lowerName);
        if (!Files.exists(workingDir)) { Files.createDirectory(workingDir); }
        String packageName = parentPackage + "." + lowerName;
        Map<GrammarRegex, String> terminalMap = TokenGen.run(
                units, packageName, camelName, mkFile(camelName + "Item.java"));
        GrammarGen.run(packageName, camelName,
                mkFile(camelName + "Grammar.java"), terminalMap, products, units);
        AttributeGen.run(packageName, camelName, attributes, mkFile(camelName + "Attribute.java"));
        LexerGen.run(packageName, camelName, terminalMap, mkFile(camelName + "Lexer.java"));
    }

    private Path mkFile(String fileName) throws IOException {
        Path newFile = Path.of(workingDir.toString(), fileName);
        Files.deleteIfExists(newFile);
        Files.createFile(newFile);
        return newFile;
    }

    private void root() throws ParseException {
        switch (lexer.token) {
            case NON_TERM: product();
                assert (lexer.token == GrammarItem.END_RULE);
                lexer.nextToken(); root(); break;
            case DEF: def(); root(); break;
            case ATTR: attr(); root(); break;
            case START: start(); root(); break;
            case END_GRAMMAR: break;
            default: throw new ParseException("Failed to parse root: " + lexer.lexeme, lexer.position);
        }
    }

    private void def() throws ParseException {
        assert (lexer.token == GrammarItem.DEF);
        lexer.nextToken();
        if (lexer.token == GrammarItem.CODE) {
            attributes.add(lexer.lexeme.strip());
            lexer.nextToken();
        }
        else { throw new ParseException("Failed to define attribute:" + lexer.lexeme, lexer.position); }

    }

    private void attr() throws ParseException {
        assert (lexer.token == GrammarItem.ATTR);
        lexer.nextToken();
        if (lexer.token == GrammarItem.CODE) {
            products.get(products.size() - 1).newAction(lexer.lexeme);
            lexer.nextToken();
        }
        else { throw new ParseException("Failed to append action:" + lexer.lexeme, lexer.position); }
    }

    private void start() throws ParseException {
        assert (lexer.token == GrammarItem.START);
        lexer.nextToken();
        if (lexer.token == GrammarItem.NON_TERM) {
            start = new GrammarUnit(lexer.lexeme);
            if (units.add(start)) { entries.put(start, new HashSet<>()); }
            else { for (GrammarUnit unit : units) { if (start.equals(unit)) { start = unit; break; } } }
            lexer.nextToken();
        }
        else throw new ParseException("Failed to parse start: " + lexer.lexeme, lexer.position);
    }

    private void product() throws ParseException {
        if (lexer.token == GrammarItem.NON_TERM) {
            currentProduct = new GrammarProduct(left());
            assert (lexer.token == GrammarItem.ARROW);
            lexer.nextToken(); right();
            products.add(currentProduct);
            entries.get(currentProduct.left).add(currentProduct);
        }
        else { throw new ParseException("Failed to parse product " + lexer.lexeme, lexer.position); }

    }

    private GrammarUnit left() throws ParseException {
        if (lexer.token == GrammarItem.NON_TERM) { return nonterm(); }
        else { throw new ParseException("Failed to parse left: " + lexer.lexeme, lexer.position); }
    }

    private void right() throws ParseException {
        if (lexer.token == GrammarItem.NON_TERM) { currentProduct.right.add(nonterm()); right(); }
        else if (lexer.token == GrammarItem.QUOTE || escapers.contains(lexer.token) ||
                lexer.token == GrammarItem.START_REGEX) { termRegex(); right(); }
        if (lexer.token == GrammarItem.END_RULE) { return; }
        throw new ParseException("Failed to parse right: " + lexer.lexeme, lexer.position);
    }

    private GrammarUnit nonterm() throws ParseException {
        if (lexer.token == GrammarItem.NON_TERM) {
            GrammarUnit unit = new GrammarUnit(lexer.lexeme);
            if (units.add(unit)) { entries.put(unit, new HashSet<>()); }
            lexer.nextToken(); return unit;
        }
        throw new ParseException("Failed to recognise nonterm: " + lexer.lexeme, lexer.position);
    }

    private void termRegex() throws ParseException {
        currentRegex = new GrammarRegex(); lexer.mark(); regex();
        currentRegex.regex = lexer.getMarker();
        GrammarUnit regexUnit = new GrammarUnit(currentRegex);
        units.add(regexUnit); currentProduct.right.add(regexUnit);
    }

    private void regex() throws ParseException { seq(); regexPrime(); }

    private void regexPrime() throws ParseException {
        if (lexer.token != GrammarItem.ALTER) { return; }
        lexer.nextToken(); seq();
        currentRegex.atoms.add(new GrammarRegex.Atom(GrammarItem.ALTER));
        regexPrime();
    }

    private void seq() throws ParseException {
        if (lexer.token == GrammarItem.START_REGEX) {
            GrammarRegex prev = currentRegex;
            currentRegex = new GrammarRegex();
            lexer.nextToken(); regex();
            assert (lexer.token == GrammarItem.END_REGEX);
            prev.atoms.add(new GrammarRegex.Atom(currentRegex));
            currentRegex = prev;
            lexer.nextToken(); meta(); seq();
        }
        else if (escapers.contains(lexer.token) || lexer.token == GrammarItem.QUOTE) {
            term(); meta(); seq();
        }

    }

    private void meta() throws ParseException {
        switch (lexer.token) {
            case PLUS:
            case QUESTION:
            case WILDCARD:
                currentRegex.atoms.add(new GrammarRegex.Atom(lexer.token));
                lexer.nextToken();
        }
    }

    private void term() throws ParseException {
        GrammarRegex.Atom atom;
        if (lexer.token == GrammarItem.QUOTE) { atom = new GrammarRegex.Atom(lexer.lexeme); }
        else if (escapers.contains(lexer.token)) { atom = new GrammarRegex.Atom(lexer.token); }
        else { throw new ParseException("Failed to recognise term: " + lexer.lexeme, lexer.position); }
        currentRegex.atoms.add(atom);
        lexer.nextToken();
    }
}
