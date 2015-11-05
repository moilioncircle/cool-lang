package com.leon.cool.lang.ast;

import com.leon.cool.lang.object.CoolInt;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.support._;
import com.leon.cool.lang.tree.TreeVisitor;

/**
 * Created by leon on 15-10-31.
 */
public class Divide extends Expression {
    public Expression left;
    public Expression right;

    public Divide(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "Divide{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyDivide(this);
    }

    @Override
    public CoolObject eval(Env env) {
        CoolInt l = (CoolInt) left.eval(env);
        CoolInt r = (CoolInt) right.eval(env);
        if (r.val == 0) {
            _.error("Division by zero" +_.errorPos(right));
        }
        return o.coolInt(l.val / r.val);
    }
}
