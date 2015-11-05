package com.leon.cool.lang.ast;

import com.leon.cool.lang.factory.TypeFactory;
import com.leon.cool.lang.object.CoolInt;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.object.CoolString;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.support.MethodDeclaration;
import com.leon.cool.lang.support._;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.type.Type;

import java.util.List;
import java.util.stream.Collectors;

/**
* Created by leon on 15-10-31.
*/
public class Dispatch extends Expression {
    public Token id;
    public List<Expression> params;

    public Dispatch(Token id, List<Expression> params) {
        this.id = id;
        this.params = params;
    }

    @Override
    public String toString() {
        return "Dispatch{" +
                "id=" + id +
                ", params=" + params +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyDispatch(this);
    }

    @Override
    public CoolObject eval(Env env) {
        List<CoolObject> paramObjects = params.stream().map(e -> e.eval(env)).collect(Collectors.toList());
        List<Type> paramTypes = paramObjects.stream().map(e -> e.type).collect(Collectors.toList());
        CoolObject obj = env.so;
        MethodDeclaration methodDeclaration = _.lookupMethodDeclaration(obj.type.className(), id.name, paramTypes).get();
        if (methodDeclaration.belongs.equals("Object")) {
            if (methodDeclaration.methodName.equals("type_name")) {
                return o.coolString(env.so.type.className());
            } else if (methodDeclaration.methodName.equals("copy")) {
                return env.so.copy();
            } else if (methodDeclaration.methodName.equals("abort")) {
                return o.coolObject().abort();
            }
        } else if (methodDeclaration.belongs.equals("IO")) {
            if (methodDeclaration.methodName.equals("out_string")) {
                System.out.print(((CoolString) paramObjects.get(0)).str);
                return env.so;
            } else if (methodDeclaration.methodName.equals("out_int")) {
                System.out.print(((CoolInt) paramObjects.get(0)).val);
                return env.so;
            } else if (methodDeclaration.methodName.equals("in_string")) {
                try {
                    String str = _.reader().readLine();
                    return o.coolString(str);
                } catch (Exception e) {
                    e.printStackTrace();
                    _.error("error read input");
                }
                return o.coolStringDefault();
            } else if (methodDeclaration.methodName.equals("in_int")) {
                try {
                    String str = _.reader().readLine();
                    return o.coolInt(Integer.parseInt(str));
                } catch (Exception e) {
                    _.error("error read input");
                }
                return o.coolIntDefault();
            }
        } else if (methodDeclaration.belongs.equals("String")) {
            if (methodDeclaration.methodName.equals("length")) {
                return ((CoolString) env.so).length();
            } else if (methodDeclaration.methodName.equals("concat")) {
                return ((CoolString) env.so).concat((CoolString) paramObjects.get(0));
            } else if (methodDeclaration.methodName.equals("substr")) {
                return ((CoolString) env.so).substr((CoolInt) paramObjects.get(0), (CoolInt) paramObjects.get(1));
            }
        }
        env.env.enterScope();
        assert paramObjects.size() == methodDeclaration.declaration.formals.size();
        for (int i = 0; i < methodDeclaration.declaration.formals.size(); i++) {
            env.env.addId(methodDeclaration.declaration.formals.get(i).id.name, paramObjects.get(i));
        }
        CoolObject object = methodDeclaration.declaration.expr.eval(env);
        env.env.exitScope();
        return object;
    }
}
