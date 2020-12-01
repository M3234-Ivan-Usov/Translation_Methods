package ru.ifmo.rain.usov.antlr;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import ru.ifmo.rain.usov.antlr.grammar.PythonBaseListener;
import ru.ifmo.rain.usov.antlr.grammar.PythonParser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Translates Python source into C++.
 * Supports only key-arguments and explicitly typed arguments in function signatures.
 * New classes and objects should appear in strict order.
 * Supports int, float, bool and string types.
 * Supports range-based cycles and inlines arithmetic cycles aka in range().
 * Inlines Python's print(obj1, obj2, ...).
 * Supports classes without nested classes and static data.
 */
public class PythonTranslator extends PythonBaseListener {
    protected static final String GLOBAL = "def";
    protected static final String INDENT = "    ";

    private PythonFunction globalFunction = new PythonFunction(GLOBAL, null, "int ");
    protected LinkedHashMap<String, PythonFunction> functions = new LinkedHashMap<>();
    protected LinkedHashMap<String, PythonClass> classes = new LinkedHashMap<>();

    private PythonClass currentClass = null;
    private PythonFunction currentFunction = null;

    private boolean print = false;
    private boolean inConstructor = false;
    private boolean identifyType = false;

    private String currentType = null;
    private String margin = "";

    private Stack<Boolean> inlineBuiltIn = new Stack<>();
    private Stack<Integer> statementStart = new Stack<>();
    private Stack<Map<String, String>> scopeInstances = new Stack<>();
    private Stack<PythonFunction> functionToCall = new Stack<>();

    /**
     * C++ Equivalents of some Python key words
     */
    private static final Map<String, String> KEY_DICT = Map.ofEntries(
            Map.entry("True", "true"),
            Map.entry("False", "false"),
            Map.entry("None", "nullptr"),
            Map.entry("or", " || "),
            Map.entry("and", " && "),
            Map.entry("xor", " ^ "),
            Map.entry("not", "!"),
            Map.entry(",", ", "),
            Map.entry("in", " : ")
    );

    /**
     * There is no char in Python, but char appears when range-based for on string
     */
    private static final Set<String> PRIMITIVE_TYPES = Set.of("int", "float", "bool");

    /**
     * List of supported built-ins. Initialised before entering tree
     */
    private static final Set<String> BUILT_IN = Set.of("std::string", "print", "len", "sqrt", "def");

    /**
     * These operations are better to be distanced from operands
     */
    private static final Set<String> WITH_WHITESPACES = Set.of(
            "<", "<=", ">", ">=", "==", "!=",
            "=", "+", "-", "/", "//", "%", "*",
            "+=", "-=", "/=", "//=", "*=", "%="
    );

    /**
     * Helps to define if expression has bool type
     */
    private static final Set<String> PREDICATE = Set.of("<", "<=", ">", ">=", "==", "!=", "and", "xor", "or", "not");

    /**
     * C++ does not need grammars terminals and Python's "def" and ":".
     * "return" is here just to avoid one extra whitespace while parsing
     */
    private static Set<String> IGNORE = Set.of("indent", "dedent", "def", ":",  "newline", "return");

    /**
     * Adds some built-in instances to be seen since the start of a program
     */
    public PythonTranslator() {
        globalFunction.args.add(new Argument("argc", "int"));
        globalFunction.args.add(new Argument("argv", "char**"));
        PythonClass string = new PythonClass("std::string");
        string.containerElementsType = "char";
        classes.put("std::string", string);
        PythonFunction len = new PythonFunction("len", null, "int ");
        len.args.add(new Argument("object", "int"));
        PythonFunction print = new PythonFunction("print", true);
        PythonFunction sqrt = new PythonFunction("sqrt", null, "float");
        sqrt.args.add(new Argument("value", "float"));
        functions.put("len", len);
        functions.put("print", print);
        functions.put("sqrt", sqrt);
    }

    /**
     * Gathers tree walk result into one string
     * @return C++ analog of Python source
     */
    public String translate() {
        StringBuilder cppSource = new StringBuilder();
        cppSource.append("#include<iostream>").append(System.lineSeparator());
        cppSource.append("#include<string>").append(System.lineSeparator());
        cppSource.append("#include<cmath>").append(System.lineSeparator());
        for (PythonClass pythonClass : classes.values()) {
            if (!BUILT_IN.contains(pythonClass.name)) {
                pythonClass.methods.get(pythonClass.name).returnType = "";
                cppSource.append(pythonClass.toString()).append(System.lineSeparator());
            }
        }
        for (PythonFunction pythonFunction : functions.values()) {
            if (!BUILT_IN.contains(pythonFunction.name) && !classes.containsKey(pythonFunction.name)) {
                cppSource.append(pythonFunction.toString()).append(System.lineSeparator());
            }
        }
        globalFunction.name = "main";
        Pattern blanks = Pattern.compile("(\r?\n)+");
        Matcher matcher = blanks.matcher(globalFunction.toString());
        cppSource.append(matcher.replaceAll(System.lineSeparator() + INDENT));
        cppSource.append(System.lineSeparator()).append("}");
        return cppSource.toString();
    }

    /**
     * Prevents converting floating rvalue to integral
     * @param type Does some case if only equals "int"
     * @return Float in case input int and expression type is floating
     */
    private String checkFloatToInt(String type) {
        type = type.trim();
        if (currentType != null && currentType.equals("float") && type.equals("int")) {
            return "float";
        }
        return type;
    }

    /**
     * Adds new instance into current scope
     * @param name Instance name
     * @param type Instance type
     */
    private void pushNewInstance(String name, String type) {
        try {
            if (PRIMITIVE_TYPES.contains(type)) {
                currentFunction.primitives.put(name, type);
            } else {
                currentFunction.references.put(name, classes.get(type));
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
        if (scopeInstances.size() > 0) {
            scopeInstances.peek().put(name, type);
        }
    }

    /**
     * Clear stack instances when exit current scope
     */
    private void destroyVariables() {
        Map<String, String> cycleVariables = scopeInstances.pop();
        for(Map.Entry<String, String> variable : cycleVariables.entrySet())
            if (PRIMITIVE_TYPES.contains(variable.getValue())) {
                currentFunction.primitives.remove(variable.getKey(), variable.getValue());
            } else {
                currentFunction.references.remove(variable.getKey(), classes.get(variable.getValue()));
            }
    }

    /**
     * Splits complex name into class and instance name
     * @param fullName Complex name with or without point
     * @return Key = "" or class-object name if any, Value = instance name
     */
    private Map.Entry<String, String> resolveName(String fullName) {
        if (fullName.contains(".")) {
            int point = fullName.indexOf(".");
            String clazz = fullName.substring(0, point);
            String method = fullName.substring(point + 1);
            return Map.entry(clazz, method);
        }
        return Map.entry("", fullName);
    }

    @Override
    public void enterRoot(PythonParser.RootContext ctx) {
        currentFunction = globalFunction;
        inlineBuiltIn.push(false);
        functions.put(GLOBAL, globalFunction);
        globalFunction.body.append(" {").append(System.lineSeparator());
        print = true;
        super.enterRoot(ctx);
    }

    @Override
    public void enterStatement(PythonParser.StatementContext ctx) {
        currentFunction.body.append(margin);
        super.enterStatement(ctx);
    }

    @Override
    public void exitStatement(PythonParser.StatementContext ctx) {
        currentFunction.body.append(System.lineSeparator());
        super.exitStatement(ctx);
    }

    @Override
    public void exitSingle_line(PythonParser.Single_lineContext ctx) {
        currentFunction.body.append(";");
        super.exitSingle_line(ctx);
    }

    @Override
    public void enterFunc_def(PythonParser.Func_defContext ctx) {
        print = false;
        PythonFunction newFunction = new PythonFunction(ctx.NAME().getText());
        if (currentClass != null) {
            if (ctx.NAME().getText().equals("__init__")) {
                newFunction.name = currentClass.name;
                newFunction.returnType = currentClass.name;
                inConstructor = true;
                functions.put(currentClass.name, newFunction);
            }
            newFunction.enclosingClass = currentClass.name;
            currentClass.methods.put(newFunction.name, newFunction);
        } else {
            functions.put(newFunction.name, newFunction);
        }
        currentFunction = newFunction;
        super.enterFunc_def(ctx);
    }

    @Override
    public void enterTyped_arg(PythonParser.Typed_argContext ctx) {
        Argument arg = new Argument(ctx.NAME(0).getText(), ctx.NAME(1).getText());
        pushNewInstance(arg.name, arg.type);
        currentFunction.addArgument(arg);
        super.enterTyped_arg(ctx);
    }

    @Override
    public void enterKey_arg(PythonParser.Key_argContext ctx) {
        identifyType = true;
        super.enterKey_arg(ctx);
    }

    @Override
    public void exitKey_arg(PythonParser.Key_argContext ctx) {
        identifyType = false;
        Argument arg;
        if (ctx.support_types() != null) {
            String value = ctx.support_types().getText();
            value = KEY_DICT.getOrDefault(value, value);
            arg = new Argument(ctx.NAME().getText(), currentType, value);
            currentFunction.primitives.put(arg.name, arg.type);
        } else {
            String complexType = ctx.func_call().complex_name().getText();
            try {
                arg = new Argument(ctx.NAME().getText(), complexType);
                currentFunction.references.put(arg.name, classes.get(complexType));
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("Unknown type: " + complexType + " at line: " + ctx.getText());
            }
        }
        currentType = null;
        currentFunction.addArgument(arg);
        super.exitKey_arg(ctx);
    }

    /*
    Since it will be parsed, do not use it! Dynamically type definition is not implemented
     */
    @Override
    public void enterSimple_arg(PythonParser.Simple_argContext ctx) {
        currentFunction.addArgument(new Argument(ctx.NAME().getText()));
        super.enterSimple_arg(ctx);
    }

    @Override
    public void enterFunc_body(PythonParser.Func_bodyContext ctx) {
        print = true;
        super.enterFunc_body(ctx);
    }

    @Override
    public void exitFunc_body(PythonParser.Func_bodyContext ctx) {
        inConstructor = false;
        super.exitFunc_body(ctx);
    }

    @Override
    public void exitFunction(PythonParser.FunctionContext ctx) {
        if (currentClass == null) {
            currentFunction = globalFunction;
            print = true;
        } else {
            print = false;
        }
        super.exitFunction(ctx);
    }

    @Override
    public void enterClazz(PythonParser.ClazzContext ctx) {
        print = false;
        currentClass = new PythonClass(ctx.NAME().getText());
        classes.put(currentClass.name, currentClass);
        super.enterClazz(ctx);
    }

    @Override
    public void exitClazz(PythonParser.ClazzContext ctx) {
        currentClass = null;
        currentFunction = globalFunction;
        print = true;
        super.exitClazz(ctx);
    }

    /*
    Do not really work. Only prints, but do not really merge fields and methods
     */
    @Override
    public void enterAncestors(PythonParser.AncestorsContext ctx) {
        for (TerminalNode ancestor : ctx.NAME()) {
            currentClass.ancestors.add(ancestor.getText());
        }
    }

    @Override
    public void enterFunc_call(PythonParser.Func_callContext ctx) {
        if (ctx.built_in() != null) {
            inlineBuiltIn.push(true);
            functionToCall.push(functions.get(ctx.built_in().getText()));
        } else {
            inlineBuiltIn.push(false);
            Map.Entry<String, String> fullName = resolveName(ctx.complex_name().getText());
            if (!fullName.getKey().equals("")) {
                String clazz = fullName.getKey().equals("self")? currentClass.name : fullName.getKey();
                functionToCall.push(currentFunction.references.get(clazz).methods.get(fullName.getValue()));
            }
            else {
                functionToCall.push(functions.get(fullName.getValue()));
            }
        }
        statementStart.push(currentFunction.body.length());
        super.enterFunc_call(ctx);
    }

    @Override
    public void exitFunc_call(PythonParser.Func_callContext ctx) {
        if (inlineBuiltIn.pop()) {
            int paramsStart = currentFunction.body.indexOf("(", statementStart.peek()) + 1;
            String rawParams = currentFunction.body.substring(paramsStart, currentFunction.body.length() - 1);
            String[] params = rawParams.split(", ");
            currentFunction.body.delete(statementStart.peek(), currentFunction.body.length());
            switch (ctx.built_in().getText()) {
                case "print":
                    currentFunction.body.append("std::cout << ");
                    int innerFunctions = 0;
                    for (String param : params) {
                        currentFunction.body.append(param);
                        if (param.contains("(")) { innerFunctions++; }
                        if (param.contains(")")) { innerFunctions--; }
                        if (innerFunctions == 0) { currentFunction.body.append(" << "); }
                        else { currentFunction.body.append(", "); }
                    }
                    currentFunction.body.append("std::endl");
                    break;
                case "len":
                    currentFunction.body.append(params[0]).append(".size()");
                    currentType = "int";
                    break;
            }
        }
        try {
            currentType = functionToCall.pop().returnType;
            statementStart.pop();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Unknown object at line:" + ctx.getText());
        }
        super.exitFunc_call(ctx);
    }

    @Override
    public void enterRet(PythonParser.RetContext ctx) {
        identifyType = ctx.rvalue() != null;
        currentType = null;
        currentFunction.body.append("return").append(identifyType ? " " : "");
        super.enterRet(ctx);
    }

    @Override
    public void exitRet(PythonParser.RetContext ctx) {
        if (identifyType && currentType != null) {
            currentFunction.returnType = currentType + " ";
            currentType = null;
            identifyType = false;
        }
        super.exitRet(ctx);
    }

    @Override
    public void enterRange_based(PythonParser.Range_basedContext ctx) {
        try {
            String source = ctx.complex_name().getText();
            String type = currentFunction.references.get(source).containerElementsType;
            scopeInstances.push(new HashMap<>());
            pushNewInstance(ctx.NAME().getText(), type);
            currentFunction.body.append(" (").append(type).append(" & ");
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Unknown object: " + ctx.complex_name().getText());
        }
        super.enterRange_based(ctx);
    }

    @Override
    public void exitRange_based(PythonParser.Range_basedContext ctx) {
        currentFunction.body.append(")");
        super.exitRange_based(ctx);
    }

    @Override
    public void exitFor_cycle(PythonParser.For_cycleContext ctx) {
        destroyVariables();
        super.exitFor_cycle(ctx);
    }

    @Override
    public void enterArithmetic_cycle(PythonParser.Arithmetic_cycleContext ctx) {
        print = false;
        List<PythonParser.Int_valueContext> range = ctx.int_value();
        String begin = "0";
        String end = range.get(0).getText();
        String step = "++";
        String name = ctx.NAME().getText();
        if (range.size() > 2) {
            begin = range.get(0).getText();
            end = range.get(1).getText();
        }
        if (range.size() == 3) {
            step = range.get(2).getText();
            step = (step.startsWith("-")) ? " -= " + step.substring(1) : " += " + step;
            if (step.equals(" -= 1")) {
                step = "--";
            }
        }
        scopeInstances.push(new HashMap<>());
        Argument counter = new Argument(name, "int", begin);
        pushNewInstance(counter.name, counter.type);
        currentFunction.body.append(" (").append(counter).append("; ").append(counter.name)
                .append(" < ").append(end).append("; ").append(counter.name).append(step);
        super.enterArithmetic_cycle(ctx);
    }

    @Override
    public void exitArithmetic_cycle(PythonParser.Arithmetic_cycleContext ctx) {
        print = true;
        currentFunction.body.append(")");
        super.exitArithmetic_cycle(ctx);
    }

    @Override
    public void enterAssignment(PythonParser.AssignmentContext ctx) {
        String name = ctx.lvalue().complex_name().getText();
        boolean newVariable = !name.contains(".") && !currentFunction.primitives.containsKey(name);
        boolean defineClassMember = inConstructor && name.startsWith("self.");
        if (newVariable) {
            statementStart.push(currentFunction.body.length());
            identifyType = true;
        } else if (defineClassMember) {
            identifyType = true;
            currentClass.fields.put(name.substring(5), new Argument(name.substring(5)));
        }
        currentType = null;
        super.enterAssignment(ctx);
    }

    @Override
    public void exitAssignment(PythonParser.AssignmentContext ctx) {
        if (currentType != null && identifyType) {
            String name = ctx.lvalue().complex_name().getText();
            boolean newInstance = !name.contains(".") &&
                    !currentFunction.primitives.containsKey(name) &&
                    !currentFunction.references.containsKey(name);
            boolean defineClassField = inConstructor && name.startsWith("self.");
            if (newInstance) {
                currentType = currentType.trim();
                pushNewInstance(name, currentType);
                currentFunction.body.insert(statementStart.pop(), currentType + " ");
            } else if (defineClassField) {
                currentClass.fields.get(name.substring(5)).type = currentType;
            }
            currentType = null;
            identifyType = false;
        }
        super.exitAssignment(ctx);
    }

    @Override
    public void enterPredicate(PythonParser.PredicateContext ctx) {
        currentFunction.body.append(" (");
        super.enterPredicate(ctx);
    }

    @Override
    public void exitPredicate(PythonParser.PredicateContext ctx) {
        currentFunction.body.append(")");
        super.exitPredicate(ctx);
    }

    @Override
    public void enterPower(PythonParser.PowerContext ctx) {
        if (ctx.factor() != null) {
            statementStart.push(currentFunction.body.length());
        }
        super.enterPower(ctx);
    }

    @Override
    public void exitPower(PythonParser.PowerContext ctx) {
        if (ctx.factor() != null) {
            int power = currentFunction.body.indexOf("**", statementStart.peek());
            String left = currentFunction.body.substring(statementStart.peek(), power);
            String right = currentFunction.body.substring(power + 2);
            currentFunction.body.replace(statementStart.pop(),
                    currentFunction.body.length(), "pow(" + left + ", " + right + ")");
            currentType = "float";
        }
        super.exitPower(ctx);
    }

    @Override
    public void enterWhile_cycle(PythonParser.While_cycleContext ctx) {
        scopeInstances.push(new HashMap<>());
        super.enterWhile_cycle(ctx);
    }

    @Override
    public void exitWhile_cycle(PythonParser.While_cycleContext ctx) {
        destroyVariables();
        super.exitWhile_cycle(ctx);
    }

    @Override
    public void enterBranch(PythonParser.BranchContext ctx) {
        scopeInstances.push(new HashMap<>());
        super.enterBranch(ctx);
    }

    @Override
    public void exitBranch(PythonParser.BranchContext ctx) {
        destroyVariables();
        super.exitBranch(ctx);
    }

    @Override
    public void enterBlock(PythonParser.BlockContext ctx) {
        if (print) {
            currentFunction.body.append(" {").append(System.lineSeparator());
        }
        margin += INDENT;
        super.enterBlock(ctx);
    }

    @Override
    public void exitBlock(PythonParser.BlockContext ctx) {
        margin = margin.substring(4);
        currentFunction.body.append(margin).append("} ");
        super.exitBlock(ctx);
    }

    @Override
    public void enterAtom(PythonParser.AtomContext ctx) {
        if (identifyType) {
            if (currentType != null && currentType.equals("bool")) {
                return;
            }
            try {
                if (ctx.func_call() != null) {
                    if (ctx.func_call().built_in() != null) {
                        currentType = functions.get(ctx.func_call().built_in().getText()).returnType;
                        return;
                    }
                    Map.Entry<String, String> fullName = resolveName(ctx.func_call().complex_name().getText());
                    currentType = fullName.getKey().equals("") ? functions.get(fullName.getValue()).returnType :
                            currentFunction.references.get(fullName.getKey()).methods.get(fullName.getValue()).returnType;
                }
                if (ctx.complex_name() != null) {
                    Map.Entry<String, String> fullName = resolveName(ctx.complex_name().getText());
                    if (!fullName.getKey().equals("")) {
                        currentType = checkFloatToInt(fullName.getKey().equals("self") ?
                                currentClass.fields.get(fullName.getValue()).type :
                                currentFunction.references.get(fullName.getKey())
                                        .fields.get(fullName.getValue()).type);
                    } else {
                        currentType = checkFloatToInt((currentClass != null) ?
                                checkFloatToInt(currentClass.methods.get(currentFunction.name)
                                        .primitives.get(fullName.getValue())) :
                                currentFunction.primitives.get(fullName.getValue()));
                    }
                }
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("Unknown object at line: " + ctx.getText());
            }
        }
        super.enterAtom(ctx);
    }

    @Override
    public void enterSupport_types(PythonParser.Support_typesContext ctx) {
        if (currentType != null && currentType.equals("bool")) {
            return;
        }
        if (ctx.bool() != null) {
            currentType = "bool";
            return;
        }
        if (ctx.floating() != null) {
            currentType = "float";
            return;
        }
        if (ctx.integral() != null) {
            currentType = checkFloatToInt("int");
            return;
        }
        if (ctx.string() != null) {
            currentType = "std::string";
            return;
        }
        super.enterSupport_types(ctx);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        throw new IllegalArgumentException("Unexpected symbol: " + node.getText());
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        if (!print) {
            return;
        }
        if (PREDICATE.contains(node.getText())) {
            currentType = "bool";
        }
        if (KEY_DICT.containsKey(node.getText())) {
            currentFunction.body.append(KEY_DICT.get(node.getText()));
            return;
        }
        if (WITH_WHITESPACES.contains(node.getText())) {
            currentFunction.body.append(" ").append(node.getText()).append(" ");
            return;
        }
        if (IGNORE.contains(node.getText()) || node.getText().matches("\r?\n *")) {
            return;
        }
        if (node.getText().equals("__init__") && currentClass != null) {
            currentFunction.body.append(currentClass.name);
            return;
        }
        if (node.getText().equals("self.") && currentClass != null) {
            currentFunction.body.append("this->");
            return;
        }
        currentFunction.body.append(node.getText());
    }
}
