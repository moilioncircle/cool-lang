package com.leon.cool.lang.ast;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.type.TypeEnum;

/**
 * Created by leon on 15-10-31.
 */
public class IsVoid extends Expression {
    public final Expression expr;

    public IsVoid(Expression expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "IsVoid{" +
                "expr=" + expr +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyIsVoid(this);
    }

    @Override
    public CoolObject eval(Env env) {
        CoolObject object = expr.eval(env);
        return ObjectFactory.coolBool(object.type.type() == TypeEnum.VOID);
    }
}
