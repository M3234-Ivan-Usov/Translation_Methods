package ru.ifmo.rain.usov.generator.bool;

import java.util.*;

import ru.ifmo.rain.usov.generator.grammar_base.*;

public class BoolGrammar extends Grammar<BoolItem, BoolAttribute> {
	public BoolGrammar() {
		super(
			Set.of(BoolItem.TERM_1, BoolItem.TERM_4, BoolItem.TERM_6, 
			BoolItem.TERM_7, BoolItem.TERM_0, BoolItem.TERM_2, BoolItem.TERM_3, 
			BoolItem.TERM_5, BoolItem.TERM_8),
			Set.of(BoolItem.or, BoolItem.not, BoolItem.skip, 
			BoolItem.xor, BoolItem.augment, BoolItem.val, BoolItem.and)
		);
		this.initProducts();
		this.findFirst();
		this.findFollow();
	}

	@Override
	protected void initProducts() {
		create(new Product<>(BoolItem.augment, BoolItem.or), p -> {
			p.left = p.right.get(0);
		});
		create(new Product<>(BoolItem.or, BoolItem.or, BoolItem.TERM_2, BoolItem.xor), p -> {
			p.left.val = p.right.get(0).val || p.right.get(2).val;
		});
		create(new Product<>(BoolItem.or, BoolItem.xor), p -> {
			p.left.val = p.right.get(0).val;
		});
		create(new Product<>(BoolItem.xor, BoolItem.xor, BoolItem.TERM_5, BoolItem.and), p -> {
			p.left.val = p.right.get(0).val ^ p.right.get(2).val;
		});
		create(new Product<>(BoolItem.xor, BoolItem.and), p -> {
			p.left.val = p.right.get(0).val;
		});
		create(new Product<>(BoolItem.and, BoolItem.and, BoolItem.TERM_3, BoolItem.not), p -> {
			p.left.val = p.right.get(0).val && p.right.get(2).val;
		});
		create(new Product<>(BoolItem.and, BoolItem.not), p -> {
			p.left.val = p.right.get(0).val;
		});
		create(new Product<>(BoolItem.not, BoolItem.TERM_4, BoolItem.not), p -> {
			p.left.val = !p.right.get(1).val;
		});
		create(new Product<>(BoolItem.not, BoolItem.val), p -> {
			p.left.val = p.right.get(0).val;
		});
		create(new Product<>(BoolItem.val, BoolItem.TERM_6, BoolItem.or, BoolItem.TERM_7), p -> {
			p.left.val = p.right.get(1).val;
		});
		create(new Product<>(BoolItem.val, BoolItem.TERM_0), p -> {
			p.left.val = true;
		});
		create(new Product<>(BoolItem.val, BoolItem.TERM_8), p -> {
			p.left.val = false;
		});
		create(new Product<>(BoolItem.skip, BoolItem.TERM_1), p -> {
		});
	}
}