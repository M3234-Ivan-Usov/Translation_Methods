package ru.ifmo.rain.usov.generator.calc;

public enum Token { 
	expr, TERM_0, TERM_1, TERM_2, 
	TERM_3, factor, TERM_4, term, 
	AUGMENT, EPS, END_GRAMMAR;

	@Override
	public String toString() {
		switch (this) {
			case TERM_4: return "'+'";
			case TERM_3: return "'*'";
			case TERM_1: return "')'";
			case TERM_2: return "'('";
			case TERM_0: return "\\d+ ('.' \\d+)?";
			default: return this.name();
		}
	}
}