package com.leon.cool.lang.tree;

/**
 * Created by leon on 15-10-14.
 */
public interface TreeElement {
    public abstract void accept(TreeVisitor visitor);
}
