package ru.ifmo.rain.usov.generator.grammar_builder.codegen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class AttributeGen {
    static String line = System.lineSeparator();

    public static void run(String packageName, String camelName, List<String> attrs, Path path) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(packageName).append(";").append(line.repeat(2));
        builder.append("import ru.ifmo.rain.usov.generator.grammar_base.Attributable;").append(line.repeat(2));
        builder.append("public class ").append(camelName)
                .append("Attribute extends Attributable<").append(camelName).append("Item> {");
        for (String attr : attrs) { builder.append(line).append("\tpublic ").append(attr); }
        builder.append(line).append("}");
        Files.writeString(path, builder);

    }
}
