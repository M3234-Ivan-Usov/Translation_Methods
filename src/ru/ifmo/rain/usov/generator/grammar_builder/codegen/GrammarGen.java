package ru.ifmo.rain.usov.generator.grammar_builder.codegen;

import ru.ifmo.rain.usov.generator.grammar_builder.builder_stuff.GrammarProduct;
import ru.ifmo.rain.usov.generator.grammar_builder.builder_stuff.GrammarRegex;
import ru.ifmo.rain.usov.generator.grammar_builder.builder_stuff.GrammarUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GrammarGen {
    static int IN_ROW = 4;
    static String line = System.lineSeparator();

    public static void run(
            String packageName, String camelName,
            Path path, Map<GrammarRegex, String> terminalMap,
            List<GrammarProduct> products, Set<GrammarUnit> units
    ) throws IOException {
        StringBuilder grammar = new StringBuilder();
        grammar.append("package ").append(packageName).append(";").append(line.repeat(2));
        grammar.append("import java.util.*;").append(line.repeat(2));
        grammar.append("import ru.ifmo.rain.usov.generator.grammar_base.*;").append(line.repeat(2));
        grammar.append("public class ").append(camelName).append("Grammar extends Grammar<").append(camelName)
                .append("Item, ").append(camelName).append("Attribute> {").append(line);
        grammar.append("\tpublic ").append(camelName).append("Grammar() {").append(line);
        grammar.append("\t".repeat(2)).append(callSuper(camelName, terminalMap, units));
        grammar.append("\t".repeat(2)).append("this.initProducts();").append(line);
        grammar.append("\t".repeat(2)).append("this.findFirst();").append(line);
        grammar.append("\t".repeat(2)).append("this.findFollow();").append(line);
        grammar.append("\t}").append(line.repeat(2));
        grammar.append("\t@Override").append(line);
        grammar.append("\tprotected void initProducts() {");
        grammar.append(createRules(camelName, products, terminalMap));
        grammar.append(line).append("\t}").append(line).append("}");
        Files.writeString(path, grammar);

    }

    private static String callSuper(
            String camelName, Map<GrammarRegex, String> terminalMap,
            Set<GrammarUnit> units) {
        StringBuilder call = new StringBuilder();
        call.append("super(").append(line).append("\t".repeat(3)).append("Set.of(");
        int counter = 1;
        for (Map.Entry<GrammarRegex, String> pair : terminalMap.entrySet()) {
            if (counter++ % IN_ROW == 0) { call.append(line).append("\t".repeat(3)); }
            call.append(camelName).append("Item.").append(pair.getValue()).append(", ");
        }
        int lastIndex = call.length() - 2;
        call.replace(lastIndex, call.length(), "),").append(line).append("\t".repeat(3)).append("Set.of(");
        counter = 1;
        for (GrammarUnit unit: units) {
            if (unit.terminal) { continue; }
            if (counter++ % IN_ROW == 0) { call.append(line).append("\t".repeat(3)); }
            call.append(camelName).append("Item.").append(unit.value).append(", ");
        }
        lastIndex = call.length() - 2;
        call.replace(lastIndex, call.length(), ")");
        call.append(line).append("\t".repeat(2)).append(");").append(line);
        return call.toString();
    }

    private static String createRules(
            String camelName, List<GrammarProduct> products,
            Map<GrammarRegex, String> terminalMap) {
        StringBuilder rules = new StringBuilder();
        for (GrammarProduct rule : products) {
            rules.append(line).append("\t".repeat(2)).append("create(new Product<>(")
                    .append(camelName).append("Item.").append(rule.left.value);
            for (GrammarUnit unit : rule.right) {
                String tokenName = unit.value;
                if (unit.terminal) { tokenName = terminalMap.get(unit.regexTerminal); }
                rules.append(", ").append(camelName).append("Item.").append(tokenName);
            }
            rules.append("), p -> {");
            for (String action : rule.actions) { rules.append(line).append("\t".repeat(3)).append(action); }
            rules.append(line).append("\t".repeat(2)).append("});");
        }
        return rules.toString();
    }
}
