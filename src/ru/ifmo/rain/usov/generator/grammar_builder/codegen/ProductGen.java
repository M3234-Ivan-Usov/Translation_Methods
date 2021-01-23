package ru.ifmo.rain.usov.generator.grammar_builder.codegen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProductGen {
    static String template =
            "package @@@;\n" +
            "\n" +
            "import java.util.ArrayList;\n" +
            "import java.util.Collections;\n" +
            "import java.util.List;\n" +
            "\n" +
            "public class Product {\n" +
            "    public Token left;\n" +
            "    public List<Token> right;\n" +
            "\n" +
            "    public Product(Token left, Token... right) {\n" +
            "        this.left = left;\n" +
            "        this.right = new ArrayList<>();\n" +
            "        Collections.addAll(this.right, right);\n" +
            "    }\n" +
            "}";

    public static void run(String packageName, Path path) throws IOException {
        Files.writeString(path, template.replace("@@@", packageName));
    }


}
