package ru.ifmo.rain.usov.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import ru.ifmo.rain.usov.antlr.grammar.PythonLexer;
import ru.ifmo.rain.usov.antlr.grammar.PythonParser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args == null || args.length != 1) {
            System.out.println("Invalid args. Expected <source.py>");
            return;
        }
        try {
            PythonLexer lexer = new PythonLexer(CharStreams.fromFileName(args[0]));
            PythonParser parser = new PythonParser(new CommonTokenStream(lexer));
            PythonTranslator translator = new PythonTranslator();
            new ParseTreeWalker().walk(translator, parser.root());
            System.out.println(translator.translate());
            /*int point = args[0].lastIndexOf('.');
            Path output = Path.of(args[0].substring(0, point), ".cpp");
            Files.deleteIfExists(output);
            Files.createFile(output);
            Files.writeString(output, translator.translate());*/
        } catch (IllegalArgumentException | IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
