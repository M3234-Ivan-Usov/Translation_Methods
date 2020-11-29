package ru.ifmo.rain.usov.antlr;

import org.antlr.v4.runtime.tree.TerminalNode;
import ru.ifmo.rain.usov.antlr.grammar.PythonBaseListener;
import ru.ifmo.rain.usov.antlr.grammar.PythonParser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Translates Python source into C++.
 * Supports only key-arguments in function signatures.
 * New classes and objects should appear in strict order.
 * Supports int, float, bool and string types.
 * Supports range-based cycles and inlines arithmetic cycles aka in range().
 * Inlines Python's print(obj1, obj2, ...).
 * Supports classes without nested classes and static data.
 * Sometimes may confuse float and int
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
        string.rangeBasedType = "char";
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
            currentFunction.localVariables.put(arg.name, arg.type);
        } else {
            String userType = ctx.func_call().complex_name().getText();
            arg = new Argument(ctx.NAME().getText(), userType);
            currentFunction.localObjects.put(arg.name, classes.get(userType));
        }
        currentType = null;
        currentFunction.addArgument(arg);
        super.exitKey_arg(ctx);
    }

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
            String fullName = ctx.complex_name().getText();
            if (fullName.contains(".")) {
                int point = fullName.indexOf(".");
                String clazz = fullName.substring(0, point);
                String method = fullName.substring(point + 1);
                if (clazz.equals("self")) {
                    clazz = currentClass.name;
                }
                functionToCall.push(classes.containsKey(clazz) ? classes.get(clazz).methods.get(method) :
                        currentFunction.localObjects.get(clazz).methods.get(method));
            } else {
                functionToCall.push(functions.get(fullName));
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
                    currentFunction.body.append("std::cout");
                    for (String param : params) {
                        currentFunction.body.append(" << ").append(param);
                    }
                    currentFunction.body.append(" << std::endl");
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
            throw (new IllegalArgumentException("Unknown object in line:" +
                    currentFunction.body.substring(statementStart.pop())));
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
            boolean float_to_int = currentFunction.returnType.equals("float ") && currentType.equals("int");
            if (!float_to_int) {
                currentFunction.returnType = currentType + " ";
            }
            currentType = null;
            identifyType = false;
        }
        super.exitRet(ctx);
    }

    @Override
    public void enterRange_based(PythonParser.Range_basedContext ctx) {
        String source = ctx.complex_name().getText();
        String type = currentFunction.localObjects.get(source).rangeBasedType;
        currentFunction.body.append(" (").append(type).append(" & ");
        super.enterRange_based(ctx);
    }

    @Override
    public void exitRange_based(PythonParser.Range_basedContext ctx) {
        currentFunction.body.append(")");
        super.exitRange_based(ctx);
    }

    @Override
    public void enterArithmetic_cycle(PythonParser.Arithmetic_cycleContext ctx) {
        print = false;
        List<PythonParser.Int_valueContext> range = ctx.int_value();
        String begin = "0";
        String end = range.get(0).getText();
        String step = "++";
        String counter = ctx.NAME().getText();
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
        currentFunction.body.append(" (int ").append(counter).append(" = ").append(begin).append("; ")
                .append(counter).append(" < ").append(end).append("; ").append(counter).append(step);
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
        boolean newVariable = !name.contains(".") && !currentFunction.localVariables.containsKey(name);
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
                    !currentFunction.localVariables.containsKey(name) &&
                    !currentFunction.localObjects.containsKey(name);
            boolean defineClassField = inConstructor && name.startsWith("self.");
            if (newInstance) {
                currentType = currentType.trim();
                if (PRIMITIVE_TYPES.contains(currentType)) {
                    currentFunction.localVariables.put(name, currentType);
                } else {
                    currentFunction.localObjects.put(name, classes.get(currentType));
                }
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
            currentFunction.body.replace(statementStart.pop(), currentFunction.body.length(),
                    "pow(" + left + ", " + right + ")");
        }
        super.exitPower(ctx);
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
            if (ctx.func_call() != null) {
                try {
                    if (ctx.func_call().built_in() != null) {
                        currentType = functions.get(ctx.func_call().built_in().getText()).returnType;
                        return;
                    }
                    String name = ctx.func_call().complex_name().getText();
                    if (name.contains(".")) {
                        String clazz = name.substring(0, name.indexOf("."));
                        String method = name.substring(name.indexOf(".") + 1);
                        currentType = currentFunction.localObjects.get(clazz).methods.get(method).returnType;
                    } else {
                        currentType = functions.get(name).returnType;
                    }
                } catch (NullPointerException e) {
                    throw new IllegalArgumentException("Unknown function: " + ctx.func_call().getText());
                }
            }
            if (ctx.complex_name() != null) {
                try {
                    String fullName = ctx.complex_name().getText();
                    if (fullName.contains(".")) {
                        String clazz = fullName.substring(0, fullName.indexOf("."));
                        String field = fullName.substring(fullName.indexOf(".") + 1);
                        currentType = (clazz.equals("self")) ?
                                classes.get(currentClass.name).fields.get(field).type :
                                currentFunction.localObjects.get(clazz).fields.get(field).type;
                    } else {
                        if (currentClass != null) {
                            currentType = classes.get(currentClass.name).methods
                                    .get(currentFunction.name).localVariables.get(fullName);
                        } else {
                            currentType = currentFunction.localVariables.get(fullName);
                        }
                    }
                } catch (NullPointerException e) {
                    throw new IllegalArgumentException("Cannot resolve: " + ctx.complex_name());
                }
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
            currentType = "int";
            return;
        }
        if (ctx.string() != null) {
            currentType = "std::string";
            return;
        }
        super.enterSupport_types(ctx);
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
