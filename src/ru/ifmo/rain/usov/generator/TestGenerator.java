package ru.ifmo.rain.usov.generator;

import ru.ifmo.rain.usov.generator.grammar_builder.Builder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;

public class TestGenerator {
    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.out.println("Incorrect args. Expected: grammar-file package-name");
            return;
        }
        try {
            Path inputGrammar = Path.of(args[0]);
            String rawGrammar = Files.readString(inputGrammar);
            Builder builder = new Builder(rawGrammar);
            System.out.println("DEBUG");
            builder.generate(inputGrammar.getParent(), args[1]);
        }
        catch (ParseException e) { System.out.println("Parsing error: " + e.getMessage()); }
        catch (IOException e) { System.out.println("IO error: " + e.getMessage()); }
    }
}
