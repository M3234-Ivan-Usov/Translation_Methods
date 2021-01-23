package ru.ifmo.rain.usov.generator.calc;

import java.util.*;

public class CalcGrammar {
    public List<Product> products;
    public EnumMap<Token, Set<Product>> entries;

    public EnumSet<Token> terminals;
    public EnumSet<Token> nonterminals;

    public EnumMap<Token, EnumSet<Token>> first;
    public EnumMap<Token, EnumSet<Token>> follow;

    private void initProducts() {
		create(new Product(Token.AUGMENT, Token.expr));
		create(new Product(Token.expr, Token.expr, Token.TERM_4, Token.term));
		create(new Product(Token.expr, Token.term));
		create(new Product(Token.term, Token.term, Token.TERM_3, Token.factor));
		create(new Product(Token.term, Token.factor));
		create(new Product(Token.factor, Token.TERM_0));
		create(new Product(Token.factor, Token.TERM_2, Token.expr, Token.TERM_1));
	}

    public CalcGrammar() {
        this.terminals = EnumSet.of(
			Token.TERM_4, Token.TERM_3, Token.TERM_1, 
			Token.TERM_2, Token.TERM_0, 
			Token.EPS, Token.END_GRAMMAR
		);
        this.nonterminals = EnumSet.complementOf(terminals);

        this.entries = new EnumMap<>(Token.class);
        this.first = new EnumMap<>(Token.class);
        this.follow = new EnumMap<>(Token.class);

        for(Token nonterm : nonterminals) {
            entries.put(nonterm, new HashSet<>());
            first.put(nonterm, EnumSet.noneOf(Token.class));
            follow.put(nonterm, EnumSet.noneOf(Token.class));
        }

        this.products = new ArrayList<>();
        this.initProducts();

        for (Token term : terminals) { first.put(term, EnumSet.of(term)); }
        this.findFirst();
    }

    private void create(Product product) {
        products.add(product);
        entries.get(product.left).add(product);
    }

    protected void findFirst() {
        boolean update = true;
        while (update) {
            update = false;
            for (Product product : products) {
                Token next = product.right.get(0);
                if (first.get(product.left).addAll(first.get(next))) { update = true; }
            }
        }
    }

    protected void findFollow() {
        boolean update = true;
        while (update) {
            update = false;
            for (Product product : products) {
                List<Token> right = product.right;

                for (int index = 0; index < right.size() - 1; ++index) {
                    if (terminals.contains(right.get(index))) { continue; }

                }
            }
        }
    }
}
