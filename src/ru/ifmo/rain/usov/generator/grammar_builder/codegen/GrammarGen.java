package ru.ifmo.rain.usov.generator.grammar_builder.codegen;

import ru.ifmo.rain.usov.generator.grammar_builder.grammar_stuff.GrammarItem;
import ru.ifmo.rain.usov.generator.grammar_builder.grammar_stuff.GrammarProduct;
import ru.ifmo.rain.usov.generator.grammar_builder.grammar_stuff.GrammarUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class GrammarGen {
    static int IN_ROW = 3;
    static String line = System.lineSeparator();

    static String template = "package @@@;\n" +
            "\n" +
            "import java.util.*;\n" +
            "\n" +
            "public class ###Grammar {\n" +
            "    public List<Product> products;\n" +
            "    public EnumMap<Token, Set<Product>> entries;\n" +
            "\n" +
            "    public EnumSet<Token> terminals;\n" +
            "    public EnumSet<Token> nonterminals;\n" +
            "\n" +
            "    public EnumMap<Token, EnumSet<Token>> first;\n" +
            "    public EnumMap<Token, EnumSet<Token>> follow;\n" +
            "\n" +
            "    private void initProducts() {%%%}\n" +
            "\n" +
            "    public ###Grammar() {\n" +
            "        this.terminals = EnumSet.of(&&&);\n" +
            "        this.nonterminals = EnumSet.complementOf(terminals);\n" +
            "\n" +
            "        this.entries = new EnumMap<>(Token.class);\n" +
            "        this.first = new EnumMap<>(Token.class);\n" +
            "        this.follow = new EnumMap<>(Token.class);\n" +
            "\n" +
            "        for(Token nonterm : nonterminals) {\n" +
            "            entries.put(nonterm, new HashSet<>());\n" +
            "            first.put(nonterm, EnumSet.noneOf(Token.class));\n" +
            "            follow.put(nonterm, EnumSet.noneOf(Token.class));\n" +
            "        }\n" +
            "\n" +
            "        this.products = new ArrayList<>();\n" +
            "        this.initProducts();\n" +
            "\n" +
            "        for (Token term : terminals) { first.put(term, EnumSet.of(term)); }\n" +
            "        this.findFirst();\n" +
            "    }\n" +
            "\n" +
            "    private void create(Product product) {\n" +
            "        products.add(product);\n" +
            "        entries.get(product.left).add(product);\n" +
            "    }\n" +
            "\n" +
            "    protected void findFirst() {\n" +
            "        boolean update = true;\n" +
            "        while (update) {\n" +
            "            update = false;\n" +
            "            for (Product product : products) {\n" +
            "                Token next = product.right.get(0);\n" +
            "                if (first.get(product.left).addAll(first.get(next))) { update = true; }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    protected void findFollow() {\n" +
            "        boolean update = true;\n" +
            "        while (update) {\n" +
            "            update = false;\n" +
            "            for (Product product : products) {\n" +
            "                List<Token> right = product.right;\n" +
            "\n" +
            "                for (int index = 0; index < right.size() - 1; ++index) {\n" +
            "                    if (terminals.contains(right.get(index))) { continue; }\n" +
            "\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n";

    public static void run(String packageName, String camelName, Path path,
                           Map<String, String> terminalMap, List<GrammarProduct> products) throws IOException {
        StringBuilder terminals = new StringBuilder();
        int counter = 0;
        for (Map.Entry<String, String> pair : terminalMap.entrySet()) {
            if (counter++ % IN_ROW == 0) { terminals.append(line).append("\t".repeat(3)); }
            terminals.append("Token.").append(pair.getValue()).append(", ");
        }
        terminals.append(line).append("\t".repeat(3))
                .append("Token.EPS, Token.END_GRAMMAR").append(line).append("\t".repeat(2));

        StringBuilder rules = new StringBuilder(line + "\t".repeat(2));
        rules.append("create(new Product(Token.AUGMENT, Token.").append(products.get(0).left.value).append("));");
        for (GrammarProduct rule : products) {
            rules.append(line).append("\t".repeat(2)).append("create(new Product(Token.").append(rule.left.value);
            for (GrammarUnit unit : rule.right) {
                String tokenName = unit.value;
                if (unit.terminal) { tokenName = terminalMap.get(unit.regexTerminal.regex); }
                rules.append(", Token.").append(tokenName);
            }
            rules.append("));");
        }
        rules.append(line).append("\t");
        String source = template;
        source = source.replace("@@@", packageName);
        source = source.replace("###", camelName);
        source = source.replace("&&&", terminals);
        source = source.replace("%%%", rules);
        Files.writeString(path, source);
    }
}
