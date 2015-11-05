package com.leon.cool.lang.ast;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolObject;

import java.util.List;

/**
 * Created by leon on 15-10-31.
 */
public class Blocks extends Expression {
    public final List<Expression> exprs;

    public Blocks(List<Expression> exprs) {
        this.exprs = exprs;
    }

    @Override
    public String toString() {
        return "Blocks{" +
                "exprs=" + exprs +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyBlocks(this);
    }

    @Override
    public CoolObject eval(Env env) {
        CoolObject object = ObjectFactory.coolVoid();
        for (Expression expr : exprs) {
            object = expr.eval(env);
        }
        return object;
    }
}