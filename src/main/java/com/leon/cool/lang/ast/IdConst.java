package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.tokenizer.Token;

/**
 * Created by leon on 15-10-31.
 */
public class IdConst extends Expression {
    public final Token tok;

    public IdConst(Token tok) {
        this.tok = tok;
    }

    @Override
    public String toString() {
        return "IdConst{" +
                "tok=" + tok +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyIdConst(this);
    }

    @Override
    public CoolObject eval(Env env) {
        return (CoolObject) env.env.lookup(tok.name).get();
    }
}
