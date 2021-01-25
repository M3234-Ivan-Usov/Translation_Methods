package ru.ifmo.rain.usov.generator.grammar_base;

import java.util.*;

public abstract class Grammar<Token extends Enum<Token>, Attribute extends Attributable<Token>> {
    public List<Product<Token, Attribute>> products;
    public Map<Token, Set<Product<Token, Attribute>>> entries;

    public Set<Token> terminals;
    public Set<Token> nonterminals;

    public Map<Token, Set<Token>> first;
    public Map<Token, Set<Token>> follow;

    protected abstract void initProducts();

    public Grammar(Set<Token> terminals, Set<Token> nonterminals) {
        this.terminals = new HashSet<>(terminals);
        this.terminals.add(null);
        this.nonterminals = nonterminals;
        this.entries = new HashMap<>();
        this.first = new HashMap<>();
        this.follow = new HashMap<>();

        for(Token nonterm : nonterminals) {
            entries.put(nonterm, new HashSet<>());
            first.put(nonterm, new HashSet<>());
            follow.put(nonterm, new HashSet<>());
        }
        this.products = new ArrayList<>();
    }

    protected void create(Product<Token, Attribute> product, AttributeAction<Token, Attribute> action) {
        product.action = action;
        products.add(product);
        entries.get(product.left).add(product);
    }

    private Set<Token> set(Token x) {
        Set<Token> result = new HashSet<>();
        result.add(x); return result;
    }

    protected void findFirst() {
        for (Token term : terminals) { first.put(term, set(term)); }
        boolean update = true;
        while (update) {
            update = false;
            for (Product<Token, Attribute> product : products) {
                Token next = product.right.get(0);
                if (first.get(product.left).addAll(first.get(next))) { update = true; }
            }
        }
    }

    protected void findFollow() {
        boolean update = true;
        while (update) {
            update = false;
            for (Product<Token, Attribute> product : products) {
                List<Token> right = product.right;
                for (int index = 0; index < right.size() - 1; ++index) {
                    if (terminals.contains(right.get(index))) { continue; }
                }
            }
        }
    }

}
