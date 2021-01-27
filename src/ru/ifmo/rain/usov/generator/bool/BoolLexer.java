package ru.ifmo.rain.usov.generator.bool;

import ru.ifmo.rain.usov.generator.grammar_base.lexer.*;
import ru.ifmo.rain.usov.generator.grammar_base.regex.*;
import java.util.*;

public class BoolLexer extends Lexer<BoolItem, BoolAttribute> {
	public BoolLexer(Class<BoolAttribute> attribute) {
		super(attribute);
		regexMap.put(BoolItem.TERM_1, new Regex<>(BoolItem.TERM_1,
			new RegexAtom<>(" "),
			new RegexAtom<>(RegexItem.PLUS),
			new RegexAtom<>(RegexItem.NEWLINE),
			new RegexAtom<>(RegexItem.PLUS),
			new RegexAtom<>(RegexItem.ALTER))
		);
		regexMap.put(BoolItem.TERM_4, new Regex<>(BoolItem.TERM_4,
			new RegexAtom<>("not"))
		);
		regexMap.put(BoolItem.TERM_6, new Regex<>(BoolItem.TERM_6,
			new RegexAtom<>("("))
		);
		regexMap.put(BoolItem.TERM_7, new Regex<>(BoolItem.TERM_7,
			new RegexAtom<>(")"))
		);
		regexMap.put(BoolItem.TERM_0, new Regex<>(BoolItem.TERM_0,
			new RegexAtom<>("true"))
		);
		regexMap.put(BoolItem.TERM_2, new Regex<>(BoolItem.TERM_2,
			new RegexAtom<>("or"))
		);
		regexMap.put(BoolItem.TERM_3, new Regex<>(BoolItem.TERM_3,
			new RegexAtom<>("and"))
		);
		regexMap.put(BoolItem.TERM_5, new Regex<>(BoolItem.TERM_5,
			new RegexAtom<>("xor"))
		);
		regexMap.put(BoolItem.TERM_8, new Regex<>(BoolItem.TERM_8,
			new RegexAtom<>("false"))
		);
		this.buildAutomaton();
	}
}