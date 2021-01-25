package ru.ifmo.rain.usov.generator.calc;

public enum CalcItem {
	TERM_0, augment, expr, TERM_1, 
	TERM_2, TERM_3, factor, TERM_4, 
	term, 
	DIGIT, LOWER, UPPER, LETTER,
	CARRIAGE, TAB, NEWLINE;

	@Override
	public String toString() {
		switch (this) {
			case TERM_0: return "'+'";
			case TERM_4: return "')'";
			case TERM_2: return "\\d+ ('.' \\d+)?";
			case TERM_1: return "'*'";
			case TERM_3: return "'('";
			default: return this.name();
		}
	}
}