package ru.ifmo.rain.usov.generator.grammar_base;

import java.util.*;
import java.util.function.ToLongBiFunction;
import java.util.stream.Collectors;

public abstract class Grammar<Token extends Enum<Token>, Attribute extends Attributable<Token>> {
    public List<Product<Token, Attribute>> products;
    public Map<Token, Set<Product<Token, Attribute>>> entries;

    public Set<Token> terminals;
    public Map<Token, Boolean> nonterminals;

    public Map<Token, Set<Token>> first;
    public Map<Token, Set<Token>> follow;

    protected abstract void initProducts();

    public Grammar(Set<Token> terminals, Set<Token> nonterminals) {
        this.terminals = new HashSet<>(terminals);
        this.terminals.add(null);
        this.nonterminals = new HashMap<>();
        for (Token nonterminal : nonterminals) { this.nonterminals.put(nonterminal, false); }
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
                Map.Entry<Set<Token>, Boolean> toAdd = first(product.right);
                if (first.get(product.left).addAll(toAdd.getKey())) { update = true; }
                if (toAdd.getValue()) { if (!nonterminals.put(product.left, true)) update = true; }
                if (update) { break; }
            }
        }
    }

    public Map.Entry<Set<Token>, Boolean> first(List<Token> seq) {
        int index = 0;
        Set<Token> seqFirst = new HashSet<>();
        for (; index < seq.size(); ++index) {
            Token right = seq.get(index);
            seqFirst.addAll(first.get(right));
            if (!nonterminals.getOrDefault(right, false)) { break; }
        }
        return Map.entry(seqFirst, index == seq.size());
    }

    protected void findFollow() {
        follow.put(products.get(0).left, set(null));
        follow.put(products.get(0).right.get(0), set(null));
        boolean update = true;
        while (update) {
            update = false;
            for (Product<Token, Attribute> product : products) {
                for (int index = 0; index < product.right.size(); ++index) {
                    Token right = product.right.get(index);
                    if (nonterminals.containsKey(right)) {
                        Map.Entry<Set<Token>, Boolean> next =
                                first(product.right.subList(index + 1, product.right.size()));
                        if (follow.get(right).addAll(next.getKey())) { update = true; }
                        if (next.getValue()) {
                            Set<Token> followLeft = follow.get(product.left);
                            if (follow.get(right).addAll(followLeft)) { update = true; }
                        }
                    }
                }
                if (update) { break; }
            }
        }
    }

}
