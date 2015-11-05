package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolInt;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.tokenizer.Token;

/**
 * Created by leon on 15-10-31.
 */
public class IntConst extends Expression {
    public Token tok;

    public IntConst(Token tok) {
        this.tok = tok;
    }

    @Override
    public String toString() {
        return "IntConst{" +
                "tok=" + tok +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyIntConst(this);
    }

    @Override
    public CoolObject eval(Env env) {
        return o.coolInt(Integer.parseInt(tok.name));
    }
}
