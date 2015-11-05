package com.leon.cool.lang.ast;

import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.tokenizer.Token;

import java.util.List;

/**
 * Created by leon on 15-10-31.
 */
public class MethodDef extends Feature {
    public final Token id;
    public final List<Formal> formals;
    public final Token type;
    public final Expression expr;

    public MethodDef(Token id, List<Formal> formals, Token type, Expression expr) {
        this.id = id;
        this.formals = formals;
        this.type = type;
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "MethodDef{" +
                "id=" + id +
                ", formals=" + formals +
                ", type=" + type +
                ", expr=" + expr +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyMethodDef(this);
    }
}
