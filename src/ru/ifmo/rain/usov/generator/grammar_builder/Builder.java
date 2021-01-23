package ru.ifmo.rain.usov.generator.grammar_builder;

import ru.ifmo.rain.usov.generator.grammar_builder.codegen.GrammarGen;
import ru.ifmo.rain.usov.generator.grammar_builder.codegen.ProductGen;
import ru.ifmo.rain.usov.generator.grammar_builder.codegen.TokenGen;
import ru.ifmo.rain.usov.generator.grammar_builder.grammar_stuff.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;

/**
 * root -> product ";" root
 * root -> _eps_
 * product -> left "->" right
 * left -> nonterm
 * right -> nonterm right
 * right -> term_regex right
 * right -> _eps_
 * nonterm -> _word_
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
    private RegexTerminal currentTerminal;
    private String lowerName;
    private String camelName;
    private Path workingDir;

    public static Set<GrammarItem> escapers = Set.of(
            GrammarItem.LOWER, GrammarItem.UPPER, GrammarItem.LETTER, GrammarItem.DIGIT,
            GrammarItem.NEWLINE, GrammarItem.SPACE, GrammarItem.TAB
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
        lexer.nextToken(); root();
    }

    public void generate(Path parent, String parentPackage) throws IOException {
        this.workingDir = Path.of(parent.toString(), lowerName);
        if (!Files.exists(workingDir)) { Files.createDirectory(workingDir); }
        String packageName = parentPackage + "." + lowerName;
        Map<String, String> terminalMap = TokenGen.run(units, packageName, mkFile("Token.java"));
        ProductGen.run(packageName, mkFile("Product.java"));
        GrammarGen.run(packageName, camelName, mkFile(camelName + "Grammar.java"), terminalMap, products);
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
            case END_GRAMMAR: break;
            default: throw new ParseException("Illegal token: " + lexer.lexeme, lexer.position);
        }
    }

    private void product() throws ParseException {
        if (lexer.token == GrammarItem.NON_TERM) {
            currentProduct = new GrammarProduct(left());
            assert (lexer.token == GrammarItem.ARROW);
            lexer.nextToken(); right();
            products.add(currentProduct);
            entries.get(currentProduct.left).add(currentProduct);
        }
        else { throw new ParseException("Illegal token: " + lexer.lexeme, lexer.position); }

    }

    private GrammarUnit left() throws ParseException {
        if (lexer.token == GrammarItem.NON_TERM) { return nonterm(); }
        else { throw new ParseException("Illegal token: " + lexer.lexeme, lexer.position); }
    }

    private void right() throws ParseException {
        if (lexer.token == GrammarItem.NON_TERM) { currentProduct.right.add(nonterm()); right(); }
        else if (lexer.token == GrammarItem.QUOTE || escapers.contains(lexer.token) ||
                lexer.token == GrammarItem.LEFT_BRACKET) { termRegex(); right(); }
        if (lexer.token == GrammarItem.END_RULE) { return; }
        throw new ParseException("Illegal token: " + lexer.lexeme, lexer.position);
    }

    private GrammarUnit nonterm() throws ParseException {
        if (lexer.token == GrammarItem.NON_TERM) {
            GrammarUnit unit = new GrammarUnit(lexer.lexeme);
            if (units.add(unit)) { entries.put(unit, new HashSet<>()); }
            lexer.nextToken(); return unit;
        }
        throw new ParseException("Illegal token: " + lexer.lexeme, lexer.position);
    }

    private void termRegex() throws ParseException {
        currentTerminal = new RegexTerminal(); lexer.mark(); regex();
        currentTerminal.regex = lexer.getMarker();
        GrammarUnit regexUnit = new GrammarUnit(currentTerminal);
        units.add(regexUnit); currentProduct.right.add(regexUnit);
    }

    private void regex() throws ParseException { seq(); regexPrime(); }

    private void regexPrime() throws ParseException {
        if (lexer.token != GrammarItem.ALTER) { return; }
        lexer.nextToken(); seq(); regexPrime();
    }

    private void seq() throws ParseException {
        if (lexer.token == GrammarItem.LEFT_BRACKET) {
            RegexTerminal prev = currentTerminal;
            currentTerminal = new RegexTerminal();
            lexer.nextToken(); regex();
            assert (lexer.token == GrammarItem.RIGHT_BRACKET);
            prev.atoms.push(new Reg(currentTerminal));
            currentTerminal = prev;
            lexer.nextToken(); meta(); seq();
        }
        else if (escapers.contains(lexer.token) ||
                lexer.token == GrammarItem.QUOTE) {
            term(); meta(); seq();
        }

    }

    private void meta() throws ParseException {
        switch (lexer.token) {
            case PLUS:
            case QUESTION:
            case WILDCARD:
                currentTerminal.atoms.push(new Reg(lexer.token));
                lexer.nextToken();
        }
    }


    private void term() throws ParseException {
        Reg reg;
        if (lexer.token == GrammarItem.QUOTE) { reg = new Reg(lexer.lexeme); }
        else if (escapers.contains(lexer.token)) { reg = new Reg(lexer.token); }
        else { throw new ParseException("Illegal token: " + lexer.lexeme, lexer.position); }
        currentTerminal.atoms.push(reg);
        lexer.nextToken();
    }

}
