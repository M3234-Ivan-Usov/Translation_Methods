package ru.ifmo.rain.usov.generator.bool;

public enum BoolItem {
	a, TERM_0, TERM_1, TERM_2, 
	TERM_3, v, skip, TERM_4, 
	x, augment, n, o, 
	TERM_5, TERM_6, TERM_7, 
	DIGIT, LOWER, UPPER, LETTER,
	CARRIAGE, TAB, NEWLINE;

	@Override
	public String toString() {
		switch (this) {
			case TERM_3: return "'not'";
			case TERM_6: return "'('";
			case TERM_7: return "')'";
			case TERM_1: return "'or'";
			case TERM_2: return "'and'";
			case TERM_0: return "skip";
			case TERM_5: return "'xor'";
			case TERM_4: return "\\u+";
			default: return this.name();
		}
	}
}