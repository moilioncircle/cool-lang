package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolObject;

/**
 * Created by leon on 15-10-31.
 */
public class Assign extends Expression {
    public final IdConst id;
    public final Expression expr;

    public Assign(IdConst id, Expression expr) {
        this.id = id;
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "Assign{" +
                "id=" + id +
                ", expr=" + expr +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyAssign(this);
    }

    @Override
    public CoolObject eval(Env env) {
        CoolObject object = expr.eval(env);
        env.env.update(id.tok.name, object);
        return object;
    }
}
