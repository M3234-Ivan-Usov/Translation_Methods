package ru.ifmo.rain.usov.generator.lr_parser;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import ru.ifmo.rain.usov.generator.grammar_builder.Token;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Visual {
    private static class TokenInstance {
        private final Token token;
        public TokenInstance(Token token) { this.token = token; }

        @Override
        public boolean equals(Object o) { return false; }

        @Override
        public String toString() { return token.toString(); }
    }

    public static void automaton(LRAutomaton automaton) {
        DirectedSparseMultigraph<LRState, TokenInstance> graph = new DirectedSparseMultigraph<>();
        for (LRState state : automaton.canonicalCollection) { graph.addVertex(state); }
        for (LRState state: automaton.canonicalCollection) {
            for (Map.Entry<Token, LRState> move : state.move.entrySet()) {
                graph.addEdge(new TokenInstance(move.getKey()), state, move.getValue());
            }
        }
        Layout<LRState, TokenInstance> layout = new KKLayout<>(graph);
        BasicVisualizationServer<LRState, TokenInstance> view = new BasicVisualizationServer<>(layout);
        view.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.AUTO);
        view.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        view.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<>());
        view.getRenderContext().setVertexFillPaintTransformer((state) -> {
            if (state.terminator) { return Color.GREEN; }
            if (automaton.initial.equals(state)) { return Color.BLUE; }
            return Color.RED;
        });
        JFrame frame = new JFrame(automaton.grammar.getClass().getSimpleName() + " LR-automaton");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(view);
        frame.pack();
        frame.setVisible(true);
    }
}
