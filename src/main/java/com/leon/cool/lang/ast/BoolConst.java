package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolBool;
import com.leon.cool.lang.object.CoolObject;

/**
 * Created by leon on 15-10-31.
 */
public class BoolConst extends Expression {
    public Boolean bool;

    public BoolConst(Boolean bool) {
        this.bool = bool;
    }

    @Override
    public String toString() {
        return "BoolConst{" +
                "bool=" + bool +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyBoolConst(this);
    }

    @Override
    public CoolObject eval(Env env) {
        return o.coolBool(bool);
    }
}
