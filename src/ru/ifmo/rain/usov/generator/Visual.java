package ru.ifmo.rain.usov.generator;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;
import ru.ifmo.rain.usov.generator.grammar_base.*;
import ru.ifmo.rain.usov.generator.grammar_base.lexer.LexerDFA;
import ru.ifmo.rain.usov.generator.grammar_base.lexer.LexerNDFA;
import ru.ifmo.rain.usov.generator.grammar_base.regex.Terminal;
import ru.ifmo.rain.usov.generator.lr_parser.*;
import ru.ifmo.rain.usov.recursive.Token;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Visual {
    public static Dimension size = new Dimension(1000, 800);

    private static class TokenInstance<T extends Enum<T>> {
        private final String string;
        private boolean leaf;
        public TokenInstance(T token) { this.string = token.toString(); }

        public TokenInstance(Attributable<T> attribute) {
            if (attribute.string == null) {
                this.string = attribute.token.toString();
                this.leaf = false;
            }
            else {
                this.string = attribute.string;
                this.leaf = true;
            }
        }

        @Override
        public boolean equals(Object o) { return false; }

        @Override
        public String toString() { return string; }
    }

    private static class TerminalInstance {
        private final String string;

        public TerminalInstance(Terminal terminal) {
            if (terminal == null) { string = "\u03b5"; }
            else switch (terminal.value) {
                case '.': string = "POINT"; break;
                case ',': string = "COMMA"; break;
                case ' ': string = "SPACE"; break;
                case '\0': string = terminal.token.toString(); break;
                default: string = String.valueOf(terminal.value);
            }
        }

        @Override
        public boolean equals(Object o) { return false; }

        @Override
        public String toString() { return string; }
    }

    public static <T extends Enum<T>, E extends Attributable<T>> void grammarSets(Grammar<T, E> grammar) {
        System.out.println("\tFIRST");
        for (Map.Entry<T, Set<T>> unit: grammar.first.entrySet()) {
            if (!grammar.nonterminals.containsKey(unit.getKey()) ||
                    unit.getKey().name().equals("skip")) { continue; }
            System.out.print(unit.getKey()+ ": ");
            for (T first : unit.getValue()) { System.out.print(first + ", "); }
            if (grammar.nonterminals.getOrDefault(unit.getKey(), false)) { System.out.print("eps"); }
            System.out.println();
        }

        System.out.println("\tFOLLOW");
        for (Map.Entry<T, Set<T>> unit: grammar.follow.entrySet()) {
            if (!grammar.nonterminals.containsKey(unit.getKey()) ||
                    unit.getKey().name().equals("skip")) { continue; }
            System.out.print(unit.getKey()+ ": ");
            for (T follower : unit.getValue()) { System.out.print(follower + ", "); }
            if (grammar.nonterminals.getOrDefault(unit.getKey(), false)) { System.out.print("eps"); }
            System.out.println();
        }
    }

    public static <T extends Enum<T>, E extends Attributable<T>> void lrAutomaton(LRAutomaton<T, E> automaton) {
        DirectedSparseMultigraph<LRAutomaton.LRState<T, E>, TokenInstance<T>> graph = new DirectedSparseMultigraph<>();
        for (LRAutomaton.LRState<T, E> state : automaton.canonicalCollection.values()) { graph.addVertex(state); }
        for (LRAutomaton.LRState<T, E> state: automaton.canonicalCollection.values()) {
            for (Map.Entry<T, LRAutomaton.LRState<T, E>> move : state.move.entrySet()) {
                graph.addEdge(new TokenInstance<>(move.getKey()), state, move.getValue());
            }
        }
        Label<LRAutomaton.LRState<T, E>, TokenInstance<T>> label = new Label<>();
        label.vertexColour = state -> {
            if (state.terminator) { return Color.GREEN; }
            if (automaton.initial.equals(state)) { return Color.BLUE; }
            return Color.RED;
        };
        label.vertexLabel = x -> String.valueOf(x.id);
        label.name = automaton.grammar.getClass().getSimpleName() + " LR-automaton";
        plot(graph, label);
    }

    public static <T extends Enum<T>> void lexerNDFA(LexerNDFA<T> automaton) {
        DirectedSparseMultigraph<LexerNDFA.NDFAState<T>, TerminalInstance> graph = new DirectedSparseMultigraph<>();
        for (LexerNDFA.NDFAState<T> state : automaton.states) { graph.addVertex(state); }
        for (LexerNDFA.NDFAState<T> from: automaton.states) {
            for (Map.Entry<Terminal, Set<LexerNDFA.NDFAState<T>>> move : from.transitions.entrySet()) {
                for (LexerNDFA.NDFAState<T> to : move.getValue()) {
                    graph.addEdge(new TerminalInstance(move.getKey()), from, to);
                }
            }
        }
        Label<LexerNDFA.NDFAState<T>, TerminalInstance> label = new Label<>();
        label.vertexColour = state -> {
            if (state.terminator) { return Color.GREEN; }
            if (automaton.initial.equals(state)) { return Color.BLUE; }
            return Color.RED;
        };
        label.vertexLabel = x -> "";
        label.name = "Lexer NDFA";
        plot(graph, label);
    }

    public static <T extends Enum<T>> void lexerDFA(LexerDFA<T> automaton) {
        DirectedSparseMultigraph<LexerDFA.DFAState<T>, TerminalInstance> graph = new DirectedSparseMultigraph<>();
        for (LexerDFA.DFAState<T> state : automaton.states.values()) { graph.addVertex(state); }
        for (LexerDFA.DFAState<T> from: automaton.states.values()) {
            for (Map.Entry<Terminal, LexerDFA.DFAState<T>> move : from.transitions.entrySet()) {
                graph.addEdge(new TerminalInstance(move.getKey()), from, move.getValue());
            }
        }
        Label<LexerDFA.DFAState<T>, TerminalInstance> label = new Label<>();
        label.vertexColour = state -> {
            if (state.terminator) { return Color.GREEN; }
            if (automaton.initial.equals(state)) { return Color.BLUE; }
            return Color.RED;
        };
        label.vertexLabel = x -> "";
        label.name = "Lexer DFA";
        plot(graph, label);
    }

    private static class Label<V, E> {
        Transformer<E, String> edgeLabel = new ToStringLabeller<>();
        Transformer<V, String> vertexLabel = new ToStringLabeller<>();
        Transformer<V, Paint> vertexColour;
        String name;
    }

    private static <V, E> void plot(DirectedSparseMultigraph<V, E> graph, Label<V, E> param) {
        Layout<V, E> layout = new KKLayout<>(graph); layout.setSize(size);
        BasicVisualizationServer<V, E> view = new BasicVisualizationServer<>(layout);
        view.setSize(size); view.setPreferredSize(size);
        view.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.AUTO);
        view.getRenderContext().setVertexLabelTransformer(param.vertexLabel);
        view.getRenderContext().setEdgeLabelTransformer(param.edgeLabel);
        view.getRenderContext().setVertexFillPaintTransformer(param.vertexColour);
        JFrame frame = new JFrame(param.name);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(view);
        frame.pack();
        frame.setVisible(true);
    }

    public static <T extends Enum<T>, A extends Attributable<T>> void actionTable(LRParser<T, A> parser) {
        System.out.println("\tTABLE");
        for (LRAutomaton.LRState<T, A> state : parser.automaton.canonicalCollection.keySet()) {
            System.out.println("--- " + state.id + " ---");
            for (LRItem<T, A> item: state.items.keySet()) { System.out.println(item); }
            System.out.println();
            for (Map.Entry<T, LRAction> action: state.action.entrySet()) {
                if (action.getValue() instanceof ActionReduce<?, ?>) {
                    System.out.println("Reduce " + ((ActionReduce<?, ?>)
                            action.getValue()).product + " by " + action.getKey());
                }
                else { System.out.println("Shift to " + state.move.
                        get(action.getKey()).id + " by " + action.getKey());
                }
            }
            System.out.println();
        }
    }

    public static <T extends Enum<T>> void tree(Attributable<T> root) {
        DelegateTree<TokenInstance<T>, TokenInstance<T>> tree =
                new DelegateTree<>(new DirectedOrderedSparseMultigraph<>());
        TokenInstance<T> adaptedRoot = new TokenInstance<>(root);
        tree.setRoot(adaptedRoot);
        Stack<Map.Entry<Attributable<T>, TokenInstance<T>>> treeDfs = new Stack<>();
        treeDfs.push(Map.entry(root, adaptedRoot));
        while (!treeDfs.empty()) {
            Map.Entry<Attributable<T>, TokenInstance<T>> subRoot = treeDfs.pop();
            if (subRoot.getKey().string == null) {
                for (Attributable<T> child : subRoot.getKey().tree.right) {
                    TokenInstance<T> adaptedChild = new TokenInstance<>(child);
                    tree.addChild(new TokenInstance<>(child.token), subRoot.getValue(), adaptedChild);
                    treeDfs.push(Map.entry(child, adaptedChild));
                }
            }
        }
        Layout<TokenInstance<T>, TokenInstance<T>> layout = new TreeLayout<>(tree);
        BasicVisualizationServer<TokenInstance<T>, TokenInstance<T>> view = new BasicVisualizationServer<>(layout);
        view.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.AUTO);
        view.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        view.getRenderContext().setVertexFillPaintTransformer(x -> (x.leaf)? Color.GREEN : Color.RED);
        JFrame frame = new JFrame("Parsing Tree");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(view);
        frame.pack();
        frame.setVisible(true);
    }
}
