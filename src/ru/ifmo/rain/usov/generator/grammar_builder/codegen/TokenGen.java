package ru.ifmo.rain.usov.generator.grammar_builder.codegen;

import ru.ifmo.rain.usov.generator.grammar_builder.builder_stuff.GrammarRegex;
import ru.ifmo.rain.usov.generator.grammar_builder.builder_stuff.GrammarUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class TokenGen {
    static int IN_ROW = 4;
    static String line = System.lineSeparator();

    public static Map<GrammarRegex, String> run(
            Set<GrammarUnit> units, String packageName,
            String camelName, Set<GrammarRegex> skippers, Path path) throws IOException {
        Map<GrammarRegex, String> terminalMap = new HashMap<>();
        StringBuilder enums = new StringBuilder();
        enums.append("package ").append(packageName).append(";").append(line.repeat(2));
        enums.append("public enum ").append(camelName).append("Item {");
        int counter = 0, terminals = 0;
        for (GrammarUnit unit : units) {
            if (counter++ % IN_ROW == 0) { enums.append(line).append("\t"); }
            String tokenName = unit.value;
            if (unit.terminal) {
                tokenName = "TERM_" + terminals++;
                terminalMap.put(unit.regexTerminal, tokenName);
            }
            enums.append(tokenName).append(", ");
        }
        enums.append(line).append("\t").append("DIGIT, LOWER, UPPER, LETTER,");
        enums.append(line).append("\t").append("CARRIAGE, TAB, NEWLINE;").append(line.repeat(2));

        enums.append("\t").append("@Override").append(line);
        enums.append("\t").append("public String toString() {").append(line);
        enums.append("\t".repeat(2)).append("switch (this) {").append(line);
        for (Map.Entry<GrammarRegex, String> pair : terminalMap.entrySet()) {
            enums.append("\t".repeat(3)).append("case ").append(pair.getValue()).append(": return \"");
            if (skippers.contains(pair.getKey())) { enums.append("skip\";").append(line); }
            else { enums.append(pair.getKey().regex
                    .replace("\\", "\\\\")).append("\";").append(line); }
        }
        enums.append("\t".repeat(3)).append("default: return this.name();").append(line);
        enums.append("\t".repeat(2)).append("}").append(line);
        enums.append("\t").append("}").append(line).append("}");
        Files.writeString(path, enums);
        return terminalMap;
    }
}
