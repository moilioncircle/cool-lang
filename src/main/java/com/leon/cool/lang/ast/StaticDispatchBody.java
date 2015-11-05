package com.leon.cool.lang.ast;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.tokenizer.Token;

import java.util.List;

/**
 * Created by leon on 15-10-31.
 */
public class StaticDispatchBody extends Expression {
    public final Token id;
    public final List<Expression> params;

    public StaticDispatchBody(Token id, List<Expression> params) {
        this.id = id;
        this.params = params;
    }

    @Override
    public String toString() {
        return "StaticDispatchBody{" +
                "id=" + id +
                ", params=" + params +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyStaticDispatchBody(this);
    }

    @Override
    public CoolObject eval(Env env) {
        return ObjectFactory.coolVoid();
    }
}
