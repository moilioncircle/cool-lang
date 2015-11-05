package com.leon.cool.lang.ast;

import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.support._;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.tree.TreeVisitor;

import java.util.Optional;

/**
 * Created by leon on 15-10-31.
 */
public class LetAttrDef extends Expression {
    public Token id;
    public Token type;
    public Optional<Expression> expr;

    public LetAttrDef(Token id, Token type, Optional<Expression> expr) {
        this.id = id;
        this.type = type;
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "LetAttrDef{" +
                "id=" + id +
                ", type=" + type +
                ", expr=" + expr +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyLetAttrDef(this);
    }

    @Override
    public CoolObject eval(Env env) {
        if (expr.isPresent()) {
            env.env.addId(id.name, expr.get().eval(env));
        } else {
            if (_.isStringType(type)) {
                env.env.addId(id.name, o.coolStringDefault());
            } else if (_.isIntType(type)) {
                env.env.addId(id.name, o.coolIntDefault());
            } else if (_.isBoolType(type)) {
                env.env.addId(id.name, o.coolBoolDefault());
            } else {
                env.env.addId(id.name, o.coolVoid());
            }
        }
        return env.so;
    }
}
