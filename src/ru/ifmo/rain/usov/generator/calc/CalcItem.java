package ru.ifmo.rain.usov.generator.calc;

public enum CalcItem {
	expr, term, skip, augment, 
	factor, TERM_0, TERM_1, TERM_2, 
	TERM_3, TERM_4, TERM_5, TERM_6, 
	TERM_7, 
	DIGIT, LOWER, UPPER, LETTER,
	CARRIAGE, TAB, NEWLINE;

	@Override
	public String toString() {
		switch (this) {
			case TERM_0: return "'+'";
			case TERM_1: return "'-'";
			case TERM_2: return "'*'";
			case TERM_3: return "'/'";
			case TERM_5: return "'('";
			case TERM_6: return "')'";
			case TERM_4: return "\\d+ ('.' \\d+)?";
			case TERM_7: return "skip";
			default: return this.name();
		}
	}
}