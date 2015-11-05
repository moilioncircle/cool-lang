package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.tokenizer.Token;

/**
 * Created by leon on 15-10-31.
 */
public class Branch extends Expression {
    public Token id;
    public Token type;
    public Expression expr;

    public Branch(Token id, Token type, Expression expr) {
        this.id = id;
        this.type = type;
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", type=" + type +
                ", expr=" + expr +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyBranch(this);
    }

    @Override
    public CoolObject eval(Env env) {
        return null;
    }
}
