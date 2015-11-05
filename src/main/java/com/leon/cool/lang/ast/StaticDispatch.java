package com.leon.cool.lang.ast;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.object.CoolInt;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.object.CoolString;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.support.MethodDeclaration;
import com.leon.cool.lang.support.Utils;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.type.Type;
import com.leon.cool.lang.type.TypeEnum;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by leon on 15-10-31.
 */
public class StaticDispatch extends Expression {
    public final Expression expr;
    public final Optional<Token> type;
    public final StaticDispatchBody dispatch;

    public StaticDispatch(Expression expr, Optional<Token> type, StaticDispatchBody dispatch) {
        this.expr = expr;
        this.type = type;
        this.dispatch = dispatch;
    }

    @Override
    public String toString() {
        return "StaticDispatch{" +
                "expr=" + expr +
                ", type=" + type +
                ", dispatch=" + dispatch +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyStaticDispatch(this);
    }

    @Override
    public CoolObject eval(Env env) {
        List<CoolObject> paramObjects = dispatch.params.stream().map(e -> e.eval(env)).collect(Collectors.toList());
        List<Type> paramTypes = paramObjects.stream().map(e -> e.type).collect(Collectors.toList());
        MethodDeclaration methodDeclaration;
        CoolObject obj = expr.eval(env);
        if (obj.type.type() == TypeEnum.VOID) {
            Utils.error("A dispatch (static or dynamic) on void" + Utils.errorPos(expr));
        }
        if (type.isPresent()) {
            methodDeclaration = Utils.lookupMethodDeclaration(type.get().name, dispatch.id.name, paramTypes).get();
        } else {
            methodDeclaration = Utils.lookupMethodDeclaration(obj.type.className(), dispatch.id.name, paramTypes).get();
        }

        switch (methodDeclaration.belongs) {
            case "Object":
                if (methodDeclaration.methodName.equals("type_name")) {
                    return ObjectFactory.coolString(obj.type.className());
                } else if (methodDeclaration.methodName.equals("copy")) {
                    return obj.copy();
                } else if (methodDeclaration.methodName.equals("abort")) {
                    return ObjectFactory.coolObject().abort();
                }
                break;
            case "IO":
                if (methodDeclaration.methodName.equals("out_string")) {
                    System.out.print(((CoolString) paramObjects.get(0)).str);
                    return obj;
                } else if (methodDeclaration.methodName.equals("out_int")) {
                    System.out.print(((CoolInt) paramObjects.get(0)).val);
                    return obj;
                } else if (methodDeclaration.methodName.equals("in_string")) {
                    try {
                        String str = Utils.reader().readLine();
                        return ObjectFactory.coolString(str);
                    } catch (Exception e) {
                        Utils.error("error read input");
                    }
                    return ObjectFactory.coolStringDefault();
                } else if (methodDeclaration.methodName.equals("in_int")) {
                    try {
                        String str = Utils.reader().readLine();
                        return ObjectFactory.coolInt(Integer.parseInt(str));
                    } catch (Exception e) {
                        Utils.error("error read input");
                    }
                    return ObjectFactory.coolIntDefault();
                }
                break;
            case "String":
                if (methodDeclaration.methodName.equals("length")) {
                    return ((CoolString) obj).length();
                } else if (methodDeclaration.methodName.equals("concat")) {
                    return ((CoolString) obj).concat((CoolString) paramObjects.get(0));
                } else if (methodDeclaration.methodName.equals("substr")) {
                    return ((CoolString) obj).substr((CoolInt) paramObjects.get(0), (CoolInt) paramObjects.get(1));
                }
                break;
        }

        obj.env.env.enterScope();
        assert paramObjects.size() == methodDeclaration.declaration.formals.size();
        for (int i = 0; i < methodDeclaration.declaration.formals.size(); i++) {
            obj.env.env.addId(methodDeclaration.declaration.formals.get(i).id.name, paramObjects.get(i));
        }
        CoolObject object = methodDeclaration.declaration.expr.eval(obj.env);
        obj.env.env.exitScope();
        return object;
    }
}
