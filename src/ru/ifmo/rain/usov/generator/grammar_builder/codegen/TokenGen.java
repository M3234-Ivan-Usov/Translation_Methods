package ru.ifmo.rain.usov.generator.grammar_builder.codegen;

import ru.ifmo.rain.usov.generator.grammar_builder.grammar_stuff.GrammarItem;
import ru.ifmo.rain.usov.generator.grammar_builder.grammar_stuff.GrammarUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class TokenGen {
    static int IN_ROW = 4;
    static String line = System.lineSeparator();

    public static Map<String, String> run(Set<GrammarUnit> units, String packageName, Path path) throws IOException {
        Map<String, String> terminalMap = new HashMap<>();
        StringBuilder enums = new StringBuilder();
        enums.append("package ").append(packageName).append(";").append(line.repeat(2));
        enums.append("public enum Token { ");
        int counter = 0, terminals = 0;
        for (GrammarUnit unit : units) {
            if (counter++ % IN_ROW == 0) { enums.append(line).append("\t"); }
            String tokenName = unit.value;
            if (unit.terminal) {
                tokenName = "TERM_" + terminals++;
                terminalMap.put(unit.regexTerminal.regex, tokenName);
            }
            enums.append(tokenName).append(", ");
        }
        enums.append(line).append("\t").append("AUGMENT").append(", EPS").append(", END_GRAMMAR;").append(line.repeat(2));

        enums.append("\t").append("@Override").append(line);
        enums.append("\t").append("public String toString() {").append(line);
        enums.append("\t".repeat(2)).append("switch (this) {").append(line);
        for (Map.Entry<String, String> pair : terminalMap.entrySet()) {
            enums.append("\t".repeat(3)).append("case ").append(pair.getValue()).append(": ");
            enums.append("return \"").append(pair.getKey().replace("\\", "\\\\")).append("\";").append(line);
        }
        enums.append("\t".repeat(3)).append("default: return this.name();").append(line);
        enums.append("\t".repeat(2)).append("}").append(line);
        enums.append("\t").append("}").append(line).append("}");
        Files.writeString(path, enums);
        return terminalMap;
    }
}
