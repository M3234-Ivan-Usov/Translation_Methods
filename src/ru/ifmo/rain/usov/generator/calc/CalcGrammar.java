package ru.ifmo.rain.usov.generator.calc;

import java.util.*;

import ru.ifmo.rain.usov.generator.grammar_base.*;

public class CalcGrammar extends Grammar<CalcItem, CalcAttribute> {
	public CalcGrammar() {
		super(
			Set.of(CalcItem.TERM_0, CalcItem.TERM_4, CalcItem.TERM_2, 
			CalcItem.TERM_1, CalcItem.TERM_3),
			Set.of(CalcItem.augment, CalcItem.expr, CalcItem.factor, 
			CalcItem.term)
		);
		this.initProducts();
		this.findFirst();
	}

	@Override
	protected void initProducts() {
		create(new Product<>(CalcItem.augment, CalcItem.expr), p -> {
			p.left = p.right[0];
		});
		create(new Product<>(CalcItem.expr, CalcItem.expr, CalcItem.TERM_0, CalcItem.term), p -> {
			p.left.val = p.right[0].val + p.right[2].val;
		});
		create(new Product<>(CalcItem.expr, CalcItem.term), p -> {
			p.left.val = p.right[0].val;
		});
		create(new Product<>(CalcItem.term, CalcItem.term, CalcItem.TERM_1, CalcItem.factor), p -> {
			p.left.val = p.right[0].val * p.right[2].val;
		});
		create(new Product<>(CalcItem.term, CalcItem.factor), p -> {
			p.left.val = p.right[0].val;
		});
		create(new Product<>(CalcItem.factor, CalcItem.TERM_2), p -> {
			p.left.val = Double.parseDouble(p.right[0].string);
		});
		create(new Product<>(CalcItem.factor, CalcItem.TERM_3, CalcItem.expr, CalcItem.TERM_4), p -> {
			p.left.val = p.right[1].val;
		});
	}
}