package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.object.CoolString;
import com.leon.cool.lang.tokenizer.Token;

/**
 * Created by leon on 15-10-31.
 */
public class StringConst extends Expression {
    public Token tok;

    public StringConst(Token tok) {
        this.tok = tok;
    }

    @Override
    public String toString() {
        return "StringConst{" +
                "tok=" + tok +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyStringConst(this);
    }

    @Override
    public CoolObject eval(Env env) {
        return o.coolString(tok.name);
    }
}
