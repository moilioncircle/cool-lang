package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolBool;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.object.CoolVoid;

/**
 * Created by leon on 15-10-31.
 */
public class Loop extends Expression {
    public Expression condExpr;
    public Expression loopExpr;

    public Loop(Expression condExpr, Expression loopExpr) {
        this.condExpr = condExpr;
        this.loopExpr = loopExpr;
    }

    @Override
    public String toString() {
        return "Loop{" +
                "condExpr=" + condExpr +
                ", loopExpr=" + loopExpr +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyLoop(this);
    }

    @Override
    public CoolObject eval(Env env) {
        while (((CoolBool) condExpr.eval(env)).val) {
            loopExpr.eval(env);
        }
        return o.coolVoid();
    }
}
