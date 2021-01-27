package ru.ifmo.rain.usov.generator.calc;

import ru.ifmo.rain.usov.generator.grammar_base.lexer.*;
import ru.ifmo.rain.usov.generator.grammar_base.regex.*;
import java.util.*;

public class CalcLexer extends Lexer<CalcItem, CalcAttribute> {
	public CalcLexer(Class<CalcAttribute> attribute) {
		super(attribute);
		regexMap.put(CalcItem.TERM_4, new Regex<>(CalcItem.TERM_4,
			new RegexAtom<>("["))
		);
		regexMap.put(CalcItem.TERM_5, new Regex<>(CalcItem.TERM_5,
			new RegexAtom<>("]"))
		);
		regexMap.put(CalcItem.TERM_0, new Regex<>(CalcItem.TERM_0,
			new RegexAtom<>("pi"))
		);
		regexMap.put(CalcItem.TERM_6, new Regex<>(CalcItem.TERM_6,
			new RegexAtom<>("+"))
		);
		regexMap.put(CalcItem.TERM_7, new Regex<>(CalcItem.TERM_7,
			new RegexAtom<>("-"))
		);
		regexMap.put(CalcItem.TERM_8, new Regex<>(CalcItem.TERM_8,
			new RegexAtom<>("*"))
		);
		regexMap.put(CalcItem.TERM_9, new Regex<>(CalcItem.TERM_9,
			new RegexAtom<>("/"))
		);
		regexMap.put(CalcItem.TERM_10, new Regex<>(CalcItem.TERM_10,
			new RegexAtom<>("("))
		);
		regexMap.put(CalcItem.TERM_11, new Regex<>(CalcItem.TERM_11,
			new RegexAtom<>(")"))
		);
		regexMap.put(CalcItem.TERM_12, new Regex<>(CalcItem.TERM_12,
			new RegexAtom<>(" "))
		);
		regexMap.put(CalcItem.TERM_1, new Regex<>(CalcItem.TERM_1,
			new RegexAtom<>("cos("))
		);
		regexMap.put(CalcItem.TERM_3, new Regex<>(CalcItem.TERM_3,
			new RegexAtom<>("|"))
		);
		regexMap.put(CalcItem.TERM_2, new Regex<>(CalcItem.TERM_2,
			new RegexAtom<>("sin("))
		);
		regexMap.put(CalcItem.TERM_13, new Regex<>(CalcItem.TERM_13,
			new RegexAtom<>(RegexItem.DIGIT),
			new RegexAtom<>(RegexItem.PLUS),
			new RegexAtom<>(new Regex<>(CalcItem.TERM_13,
				new RegexAtom<>("."),
				new RegexAtom<>(RegexItem.DIGIT),
				new RegexAtom<>(RegexItem.PLUS))),
			new RegexAtom<>(RegexItem.QUESTION))
		);
		this.buildAutomaton();
	}
}