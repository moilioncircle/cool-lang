package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolBool;
import com.leon.cool.lang.object.CoolInt;
import com.leon.cool.lang.object.CoolObject;

/**
 * Created by leon on 15-10-31.
 */
public class Lt extends Expression {
    public Expression left;
    public Expression right;

    public Lt(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "Lt{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyLt(this);
    }

    @Override
    public CoolObject eval(Env env) {
        CoolInt l = (CoolInt) left.eval(env);
        CoolInt r = (CoolInt) right.eval(env);
        if (l.val < r.val) {
            return o.coolBool(true);
        } else {
            return o.coolBool(false);
        }
    }
}
