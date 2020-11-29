package ru.ifmo.rain.usov.antlr;

/**
 * Data structure to hold object and its type
 */
public class Argument {
    public String name;
    public String type;
    public String defaultValue;
    private String margin = "";

    public Argument(String argumentName) {
        this.name = argumentName;
    }

    public Argument(String argumentName, String type) {
        this.name = argumentName;
        this.type = type;
    }

    public Argument(String argumentName, String type, String defaultValue) {
        this.name = argumentName;
        this.type = type;
        this.defaultValue = defaultValue;
    }


    public boolean isPrimitive() {
        return type.equals("int") || type.equals("float") || type.equals("bool") || type.equals("char**");
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    @Override
    public String toString() {
        StringBuilder argumentBuilder = new StringBuilder();
        argumentBuilder.append(margin).append(type);
        if (!isPrimitive()) {
            argumentBuilder.append(" &");
        }
        argumentBuilder.append(" ").append(name);
        if (defaultValue != null) {
            argumentBuilder.append(" = ").append(defaultValue);
        }
        return argumentBuilder.toString();
    }
}
