package ru.ifmo.rain.usov.generator.bool;

import java.util.*;

import ru.ifmo.rain.usov.generator.grammar_base.*;

public class BoolGrammar extends Grammar<BoolItem, BoolAttribute> {
	public BoolGrammar() {
		super(
			Set.of(BoolItem.TERM_3, BoolItem.TERM_6, BoolItem.TERM_7, 
			BoolItem.TERM_1, BoolItem.TERM_2, BoolItem.TERM_0, BoolItem.TERM_5, 
			BoolItem.TERM_4),
			Set.of(BoolItem.a, BoolItem.v, BoolItem.skip, 
			BoolItem.x, BoolItem.augment, BoolItem.n, BoolItem.o)
		);
		this.initProducts();
		this.findFirst();
		this.findFollow();
	}

	@Override
	protected void initProducts() {
		create(new Product<>(BoolItem.augment, BoolItem.o), p -> {
			p.left = p.right.get(0);
		});
		create(new Product<>(BoolItem.o, BoolItem.o, BoolItem.TERM_1, BoolItem.x), p -> {
		});
		create(new Product<>(BoolItem.o, BoolItem.x), p -> {
		});
		create(new Product<>(BoolItem.x, BoolItem.x, BoolItem.TERM_5, BoolItem.a), p -> {
		});
		create(new Product<>(BoolItem.x, BoolItem.a), p -> {
		});
		create(new Product<>(BoolItem.a, BoolItem.a, BoolItem.TERM_2, BoolItem.n), p -> {
		});
		create(new Product<>(BoolItem.a, BoolItem.n), p -> {
		});
		create(new Product<>(BoolItem.n, BoolItem.TERM_3, BoolItem.n), p -> {
		});
		create(new Product<>(BoolItem.n, BoolItem.v), p -> {
		});
		create(new Product<>(BoolItem.v, BoolItem.TERM_6, BoolItem.o, BoolItem.TERM_7), p -> {
		});
		create(new Product<>(BoolItem.v, BoolItem.TERM_4), p -> {
		});
		create(new Product<>(BoolItem.skip, BoolItem.TERM_0), p -> {
		});
	}
}