package ru.ifmo.rain.usov.generator.lr_parser;


public class ActionShift implements LRAction {
    private final LRParser parser;
    private final LRState state;

    public ActionShift(LRParser parser, LRState state) {
        this.parser = parser;
        this.state = state;
    }

    @Override
    public void perform() { parser.currentState = state; }
}
