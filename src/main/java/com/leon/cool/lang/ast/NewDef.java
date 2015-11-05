package com.leon.cool.lang.ast;

import com.leon.cool.lang.factory.TypeFactory;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.support._;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.type.Type;

/**
 * Created by leon on 15-10-31.
 */
public class NewDef extends Expression {
    public Token type;

    public NewDef(Token type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NewDef{" +
                "type=" + type +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyNewDef(this);
    }

    public CoolObject eval(Env env) {
        Type type;
        if (_.isSelfType(this.type)) {
            type = env.so.type;
        } else {
            type = TypeFactory.objectType(this.type.name);
        }
        return _.newDef(type);
    }
}
