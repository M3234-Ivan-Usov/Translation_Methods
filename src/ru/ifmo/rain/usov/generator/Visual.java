package ru.ifmo.rain.usov.generator;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;
import ru.ifmo.rain.usov.generator.grammar_base.Attributable;
import ru.ifmo.rain.usov.generator.grammar_base.lexer.LexerDFA;
import ru.ifmo.rain.usov.generator.grammar_base.lexer.LexerNDFA;
import ru.ifmo.rain.usov.generator.grammar_base.regex.Terminal;
import ru.ifmo.rain.usov.generator.lr_parser.LRAutomaton;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;

public class Visual {
    private static class TokenInstance<T extends Enum<T>> {
        private final String string;
        public TokenInstance(T token) { this.string = token.toString(); }

        @Override
        public boolean equals(Object o) { return false; }

        @Override
        public String toString() { return string; }
    }

    private static class TerminalInstance {
        private final String string;

        public TerminalInstance(Terminal terminal) {
            if (terminal == null) { string = "eps"; }
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

    public static <T extends Enum<T>, E extends Attributable<T>> void lrAutomaton(LRAutomaton<T, E> automaton) {
        DirectedSparseMultigraph<LRAutomaton.LRState<T, E>, TokenInstance<T>> graph = new DirectedSparseMultigraph<>();
        for (LRAutomaton.LRState<T, E> state : automaton.canonicalCollection) { graph.addVertex(state); }
        for (LRAutomaton.LRState<T, E> state: automaton.canonicalCollection) {
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
        label.name = "Lexer DFA";
        plot(graph, label);
    }

    private static class Label<V, E> {
        Transformer<E, String> edgeLabel = new ToStringLabeller<>();
        Transformer<V, Paint> vertexColour;
        String name;
    }

    private static <V, E> void plot(DirectedSparseMultigraph<V, E> graph, Label<V, E> param) {
        Layout<V, E> layout = new KKLayout<>(graph);
        BasicVisualizationServer<V, E> view = new BasicVisualizationServer<>(layout);
        view.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.AUTO);
        view.getRenderContext().setEdgeLabelTransformer(param.edgeLabel);
        view.getRenderContext().setVertexFillPaintTransformer(param.vertexColour);
        JFrame frame = new JFrame(param.name);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(view);
        frame.pack();
        frame.setVisible(true);
    }
}
