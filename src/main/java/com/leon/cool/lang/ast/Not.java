package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolBool;
import com.leon.cool.lang.object.CoolObject;

/**
 * Created by leon on 15-10-31.
 */
public class Not extends Expression {
    public Expression expr;

    public Not(Expression expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "Not{" +
                "expr=" + expr +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyNot(this);
    }

    @Override
    public CoolObject eval(Env env) {
        CoolBool bool = (CoolBool) expr.eval(env);
        return o.coolBool(!bool.val);
    }
}
