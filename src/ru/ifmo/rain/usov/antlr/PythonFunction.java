package ru.ifmo.rain.usov.antlr;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Data structure to hold function signature and body with using objects
 */
public class PythonFunction {
    public String name;
    public String enclosingClass;
    public String returnType = "void ";
    public boolean va = false;

    public LinkedList<Argument> args = new LinkedList<>();
    public Map<String, String> localVariables = new HashMap<>();
    public Map<String, PythonClass> localObjects = new HashMap<>();
    public StringBuilder body = new StringBuilder();
    private String margin = "";

    public void addArgument(Argument arg) {
        if (enclosingClass != null && args.isEmpty() && arg.name.equals("self")) {
            return;
        }
        args.add(arg);
    }

    public PythonFunction(String functionName) {
        this.name = functionName;
        this.enclosingClass = null;
    }

    public PythonFunction(String functionName, String enclosingClass, String returnType) {
        this.name = functionName;
        this.enclosingClass = enclosingClass;
        this.returnType = returnType;
    }

    public PythonFunction(String functionName, boolean va) {
        this.name = functionName;
        this.va = va;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    @Override
    public String toString() {
        StringBuilder functionBuilder = new StringBuilder();
        functionBuilder.append(margin).append(returnType).append(name).append("(");
        if (!args.isEmpty()) {
            functionBuilder.append(args.get(0).toString());
            for (int arg = 1; arg < args.size(); ++arg) {
                functionBuilder.append(", ").append(args.get(arg).toString());
            }
        }
        functionBuilder.append(")").append(body).append(System.lineSeparator());
        return functionBuilder.toString();
    }
}
