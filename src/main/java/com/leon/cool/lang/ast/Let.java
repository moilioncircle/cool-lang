package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolObject;

import java.util.List;

/**
 * Created by leon on 15-10-31.
 */
public class Let extends Expression {
    public List<LetAttrDef> attrDefs;
    public Expression expr;

    public Let(List<LetAttrDef> attrDefs, Expression expr) {
        this.attrDefs = attrDefs;
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "Let{" +
                "attrDefs=" + attrDefs +
                ", expr=" + expr +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyLet(this);
    }

    @Override
    public CoolObject eval(Env env) {
        env.env.enterScope();
        attrDefs.forEach(e -> e.eval(env));
        CoolObject object = expr.eval(env);
        env.env.exitScope();
        return object;
    }
}
