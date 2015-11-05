package com.leon.cool.lang.ast;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolInt;
import com.leon.cool.lang.object.CoolObject;

/**
 * Created by leon on 15-10-31.
 */
public class Neg extends Expression {
    public final Expression expr;

    public Neg(Expression expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "Neg{" +
                "expr=" + expr +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyNeg(this);
    }

    @Override
    public CoolObject eval(Env env) {
        CoolInt val = (CoolInt) expr.eval(env);
        return ObjectFactory.coolInt(-val.val);
    }
}
