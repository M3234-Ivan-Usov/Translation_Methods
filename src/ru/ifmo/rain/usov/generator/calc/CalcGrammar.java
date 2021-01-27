package ru.ifmo.rain.usov.generator.calc;

import java.util.*;

import ru.ifmo.rain.usov.generator.grammar_base.*;

public class CalcGrammar extends Grammar<CalcItem, CalcAttribute> {
	public CalcGrammar() {
		super(
			Set.of(CalcItem.TERM_4, CalcItem.TERM_5, CalcItem.TERM_0, 
			CalcItem.TERM_6, CalcItem.TERM_7, CalcItem.TERM_8, CalcItem.TERM_9, 
			CalcItem.TERM_10, CalcItem.TERM_11, CalcItem.TERM_12, CalcItem.TERM_1, 
			CalcItem.TERM_3, CalcItem.TERM_2, CalcItem.TERM_13),
			Set.of(CalcItem.expr, CalcItem.term, CalcItem.skip, 
			CalcItem.augment, CalcItem.factor)
		);
		this.initProducts();
		this.findFirst();
		this.findFollow();
	}

	@Override
	protected void initProducts() {
		create(new Product<>(CalcItem.augment, CalcItem.expr), p -> {
			p.left = p.right.get(0);
		});
		create(new Product<>(CalcItem.expr, CalcItem.expr, CalcItem.TERM_6, CalcItem.term), p -> {
			p.left.val = p.right.get(0).val + p.right.get(2).val;
		});
		create(new Product<>(CalcItem.expr, CalcItem.expr, CalcItem.TERM_7, CalcItem.term), p -> {
			p.left.val = p.right.get(0).val - p.right.get(2).val;
		});
		create(new Product<>(CalcItem.expr, CalcItem.term), p -> {
			p.left.val = p.right.get(0).val;
		});
		create(new Product<>(CalcItem.term, CalcItem.term, CalcItem.TERM_8, CalcItem.factor), p -> {
			p.left.val = p.right.get(0).val * p.right.get(2).val;
		});
		create(new Product<>(CalcItem.term, CalcItem.term, CalcItem.TERM_9, CalcItem.factor), p -> {
			p.left.val = p.right.get(0).val / p.right.get(2).val;
		});
		create(new Product<>(CalcItem.term, CalcItem.factor), p -> {
			p.left.val = p.right.get(0).val;
		});
		create(new Product<>(CalcItem.factor, CalcItem.TERM_13), p -> {
			p.left.val = Double.parseDouble(p.right.get(0).string);
		});
		create(new Product<>(CalcItem.factor, CalcItem.TERM_0), p -> {
			p.left.val = Math.PI;
		});
		create(new Product<>(CalcItem.factor, CalcItem.TERM_10, CalcItem.expr, CalcItem.TERM_11), p -> {
			p.left.val = p.right.get(1).val;
		});
		create(new Product<>(CalcItem.factor, CalcItem.TERM_2, CalcItem.expr, CalcItem.TERM_11), p -> {
			p.left.val = Math.sin(p.right.get(1).val);
		});
		create(new Product<>(CalcItem.factor, CalcItem.TERM_1, CalcItem.expr, CalcItem.TERM_11), p -> {
			p.left.val = Math.cos(p.right.get(1).val);
		});
		create(new Product<>(CalcItem.factor, CalcItem.TERM_3, CalcItem.expr, CalcItem.TERM_3), p -> {
			p.left.val = Math.abs(p.right.get(1).val);
		});
		create(new Product<>(CalcItem.factor, CalcItem.TERM_4, CalcItem.expr, CalcItem.TERM_5), p -> {
			p.left.val = Math.ceil(p.right.get(1).val);
		});
		create(new Product<>(CalcItem.skip, CalcItem.TERM_12), p -> {
		});
	}
}