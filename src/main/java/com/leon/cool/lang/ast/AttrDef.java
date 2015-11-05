package com.leon.cool.lang.ast;

import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.tokenizer.Token;

import java.util.Optional;

/**
 * Created by leon on 15-10-31.
 */
public class AttrDef extends Feature {
    public final Token id;
    public final Token type;
    public final Optional<Expression> expr;

    public AttrDef(Token id, Token type, Optional<Expression> expr) {
        this.id = id;
        this.type = type;
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "AttrDef{" +
                "id=" + id +
                ", type=" + type +
                ", expr=" + expr +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyAttrDef(this);
    }
}
