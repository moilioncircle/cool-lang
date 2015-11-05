package com.leon.cool.lang.ast;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.object.CoolBool;
import com.leon.cool.lang.object.CoolInt;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.object.CoolString;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.support.Utils;
import com.leon.cool.lang.tree.TreeVisitor;

/**
 * Created by leon on 15-10-31.
 */
public class Comp extends Expression {
    public final Expression left;
    public final Expression right;

    public Comp(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "Comp{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyComp(this);
    }

    @Override
    public CoolObject eval(Env env) {
        CoolObject l = left.eval(env);
        CoolObject r = right.eval(env);
        if (Utils.isBasicType(l.type) && Utils.isBasicType(r.type)) {
            if (l instanceof CoolString && r instanceof CoolString) {
                return ObjectFactory.coolBool(((CoolString) l).str.equals(((CoolString) r).str));
            } else if (l instanceof CoolInt && r instanceof CoolInt) {
                return ObjectFactory.coolBool(((CoolInt) l).val == ((CoolInt) r).val);
            } else if (l instanceof CoolBool && r instanceof CoolBool) {
                return ObjectFactory.coolBool(((CoolBool) l).val == ((CoolBool) r).val);
            } else {
                throw new AssertionError("unexpected error.");
            }
        } else {
            return ObjectFactory.coolBool(l.equals(r));
        }
    }
}
