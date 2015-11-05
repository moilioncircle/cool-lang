package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolInt;
import com.leon.cool.lang.object.CoolObject;

/**
 * Created by leon on 15-10-31.
 */
public class Mul extends Expression {
    public Expression left;
    public Expression right;

    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "Mul{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }

    @Override
    public CoolObject eval(Env env) {
        CoolInt l = (CoolInt) left.eval(env);
        CoolInt r = (CoolInt) right.eval(env);
        return o.coolInt(l.val * r.val);
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyMul(this);
    }
}
