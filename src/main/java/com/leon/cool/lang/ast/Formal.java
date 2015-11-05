package com.leon.cool.lang.ast;

import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.tokenizer.Token;

/**
 * Created by leon on 15-10-31.
 */
public class Formal extends TreeNode {
    public Token id;
    public Token type;

    public Formal(Token id, Token type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Formal{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyFormal(this);
    }
}
