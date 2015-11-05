package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolBool;
import com.leon.cool.lang.object.CoolObject;

/**
 * Created by leon on 15-10-31.
 */
public class Cond extends Expression {
    public Expression condExpr;
    public Expression thenExpr;
    public Expression elseExpr;

    public Cond(Expression condExpr, Expression thenExpr, Expression elseExpr) {
        this.condExpr = condExpr;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
    }

    @Override
    public String toString() {
        return "Cond{" +
                "condExpr=" + condExpr +
                ", thenExpr=" + thenExpr +
                ", elseExpr=" + elseExpr +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyCond(this);
    }

    @Override
    public CoolObject eval(Env env) {
        if (((CoolBool) condExpr.eval(env)).val) {
            return thenExpr.eval(env);
        } else {
            return elseExpr.eval(env);
        }
    }
}
