package ru.ifmo.rain.usov.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import ru.ifmo.rain.usov.antlr.grammar.PythonLexer;
import ru.ifmo.rain.usov.antlr.grammar.PythonParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

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
            System.out.println("Translation is successful");
            int point = args[0].indexOf('.');
            Path output = Path.of(args[0].substring(0, point) + ".cpp");
            Files.deleteIfExists(output);
            Files.createFile(output);
            Files.writeString(output, translator.translate());
            if (compile(output)) {
                System.out.println("Compilation is successful");
            }
        } catch (IllegalArgumentException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    public static boolean compile(Path source) throws IOException, InterruptedException {
        File dir = new File(source.getParent().toAbsolutePath().toString());
        String sourceName = source.getFileName().toString();
        String outName = sourceName.substring(0, sourceName.indexOf("."));
        String compileCommand = "g++ -o " + outName + " " + sourceName;
        System.out.println("Starting compilation");
        Process compilation = Runtime.getRuntime().exec(compileCommand, null, dir);
        compilation.waitFor();
        Scanner errors = new Scanner(compilation.getErrorStream());
        if (!errors.hasNext()) {
            return true;
        }
        while (errors.hasNext()) {
            System.out.println(errors.nextLine());
        }
        compilation.destroy();
        return false;
    }
}
