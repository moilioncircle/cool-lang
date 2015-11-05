package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolObject;

/**
 * Created by leon on 15-10-31.
 */
public class Paren extends Expression {
    public Expression expr;

    public Paren(Expression expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "Paren{" +
                "expr=" + expr +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyParen(this);
    }

    @Override
    public CoolObject eval(Env env) {
        return expr.eval(env);
    }
}
