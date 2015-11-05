package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.object.CoolVoid;

/**
 * Created by leon on 15-10-31.
 */
public class NoExpression extends Expression {
    @Override
    public String toString() {
        return "NoExpression{}";
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyNoExpression(this);
    }

    @Override
    public CoolObject eval(Env env) {
        return o.coolVoid();
    }
}
