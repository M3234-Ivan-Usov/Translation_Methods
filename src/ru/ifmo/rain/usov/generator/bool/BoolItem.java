package ru.ifmo.rain.usov.generator.bool;

public enum BoolItem {
	TERM_0, TERM_1, TERM_2, or, 
	TERM_3, not, TERM_4, skip, 
	xor, augment, TERM_5, val, 
	TERM_6, TERM_7, and, TERM_8, 
	DIGIT, LOWER, UPPER, LETTER,
	CARRIAGE, TAB, NEWLINE;

	@Override
	public String toString() {
		switch (this) {
			case TERM_1: return "skip";
			case TERM_4: return "'not'";
			case TERM_6: return "'('";
			case TERM_7: return "')'";
			case TERM_0: return "'true'";
			case TERM_2: return "'or'";
			case TERM_3: return "'and'";
			case TERM_5: return "'xor'";
			case TERM_8: return "'false'";
			default: return this.name();
		}
	}
}