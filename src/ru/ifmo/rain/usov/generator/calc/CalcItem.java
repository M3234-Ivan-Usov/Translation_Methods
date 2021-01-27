package ru.ifmo.rain.usov.generator.calc;

public enum CalcItem {
	TERM_0, TERM_1, expr, term, 
	skip, TERM_2, augment, factor, 
	TERM_3, TERM_4, TERM_5, TERM_6, 
	TERM_7, TERM_8, TERM_9, TERM_10, 
	TERM_11, TERM_12, TERM_13, 
	DIGIT, LOWER, UPPER, LETTER,
	CARRIAGE, TAB, NEWLINE;

	@Override
	public String toString() {
		switch (this) {
			case TERM_4: return "'['";
			case TERM_5: return "']'";
			case TERM_0: return "'pi'";
			case TERM_6: return "'+'";
			case TERM_7: return "'-'";
			case TERM_8: return "'*'";
			case TERM_9: return "'/'";
			case TERM_10: return "'('";
			case TERM_11: return "')'";
			case TERM_12: return "skip";
			case TERM_1: return "'cos('";
			case TERM_3: return "'|'";
			case TERM_2: return "'sin('";
			case TERM_13: return "\\d+ ('.' \\d+)?";
			default: return this.name();
		}
	}
}