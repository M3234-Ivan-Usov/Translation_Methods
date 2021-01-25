package ru.ifmo.rain.usov.generator.grammar_base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class RuntimeProduct<Token extends Enum<Token>, Attribute extends Attributable<Token>> {
    private Constructor<Attribute> install;
    public Attribute left;
    public Attribute[] right;
    public boolean epsilon;
    public AttributeAction<Token, Attribute> action;

    public RuntimeProduct(Product<Token, Attribute> origin, Class<Attribute> clazz) {
        try {
            this.install = clazz.getDeclaredConstructor();
            this.left = this.install.newInstance();
            this.right = (Attribute[]) new Object[origin.right.size()];
            this.epsilon = origin.epsilon;
            this.action = origin.action;
            for (int i = 0; i < origin.right.size(); ++i) {
                this.right[i] = install.newInstance();
                this.right[i].token = origin.right.get(i);
            }
        } catch (NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException ignored) {}
    }
}
