package ru.ifmo.rain.usov.antlr;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Data structure to hold referenced type
 */
public class PythonClass {
    public String name;
    public LinkedList<String> ancestors = new LinkedList<>();
    public LinkedHashMap<String, Argument> fields = new LinkedHashMap<>();
    public LinkedHashMap<String, PythonFunction> methods = new LinkedHashMap<>();
    public String rangeBasedType;

    public PythonClass(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder classBuilder = new StringBuilder();
        classBuilder.append("class ").append(name);
        if (!ancestors.isEmpty()) {
            classBuilder.append(" : public ").append(ancestors.get(0));
            for (int ancestor = 1; ancestor < ancestors.size(); ++ancestor) {
                classBuilder.append(", public ").append(ancestors.get(ancestor));
            }
        }
        classBuilder.append(" {").append(System.lineSeparator())
                .append("public:").append(System.lineSeparator());
        for (Argument field : fields.values()) {
            field.setMargin(PythonTranslator.INDENT);
            classBuilder.append(field.toString()).append(";").append(System.lineSeparator());
        }

        for (PythonFunction method : methods.values()) {
            method.setMargin(PythonTranslator.INDENT);
            classBuilder.append(System.lineSeparator()).append(method.toString());
        }
        classBuilder.append(System.lineSeparator());
        return classBuilder.toString();
    }
}
