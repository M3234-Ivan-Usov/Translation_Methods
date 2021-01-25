package ru.ifmo.rain.usov.generator.grammar_builder.codegen;

import ru.ifmo.rain.usov.generator.grammar_builder.builder_stuff.GrammarRegex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class LexerGen {
    static String line = System.lineSeparator();
    public static void run(
            String packageName, String camelName,
            Map<GrammarRegex, String> terminalMap,
            Path path) throws IOException {
        StringBuilder lexer = new StringBuilder();
        lexer.append("package ").append(packageName).append(";").append(line.repeat(2));
        lexer.append("import ru.ifmo.rain.usov.generator.grammar_base.lexer.*;").append(line);
        lexer.append("import ru.ifmo.rain.usov.generator.grammar_base.regex.*;").append(line);
        lexer.append("import java.util.*;").append(line.repeat(2));
        lexer.append("public class ").append(camelName).append("Lexer extends Lexer<")
                .append(camelName).append("Item, ").append(camelName).append("Attribute> {").append(line);
        lexer.append("\t").append("public ").append(camelName).append("Lexer(Class<")
                .append(camelName).append("Attribute> attribute) {").append(line);
        lexer.append("\t".repeat(2)).append("super(attribute);");
        for (Map.Entry<GrammarRegex, String> pair: terminalMap.entrySet()) {
            String tokenName = camelName + "Item." + pair.getValue();
            lexer.append(line).append("\t".repeat(2)).append("regexMap.put(").append(tokenName)
                    .append(", ").append(generateRegex(pair.getKey(), tokenName, 3));
            lexer.append(line).append("\t\t);");
        }
        lexer.append(line).append("\t".repeat(2)).append("this.buildAutomaton();").append(line);
        lexer.append("\t}").append(line).append("}");
        Files.writeString(path, lexer);
    }

    private static String generateRegex(GrammarRegex regex, String tokenName, int tabs) {
        StringBuilder builder = new StringBuilder();
        builder.append("new Regex<>(").append(tokenName);
        for (GrammarRegex.Atom atom : regex.atoms) {
            builder.append(",").append(line).append("\t".repeat(tabs));
            builder.append("new RegexAtom<>(");
            if (atom.token != null) { builder.append("RegexItem.").append(atom.token.name()); }
            else if (atom.value != null) { builder.append('"').append(atom.value).append('"'); }
            else if (atom.inner != null) { builder.append(generateRegex(atom.inner, tokenName, tabs + 1)); }
            else throw new AssertionError("null regex");
            builder.append(")");
        }
        builder.append(")");
        return builder.toString();
    }
}
