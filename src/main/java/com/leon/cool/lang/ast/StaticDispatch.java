package com.leon.cool.lang.ast;

import com.leon.cool.lang.object.CoolInt;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.object.CoolString;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.support.MethodDeclaration;
import com.leon.cool.lang.support._;
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
    public Expression expr;
    public Optional<Token> type;
    public StaticDispatchBody dispatch;

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
            _.error("A dispatch (static or dynamic) on void" + _.errorPos(expr));
        }
        if (type.isPresent()) {
            methodDeclaration = _.lookupMethodDeclaration(type.get().name, dispatch.id.name, paramTypes).get();
        } else {
            methodDeclaration = _.lookupMethodDeclaration(obj.type.className(), dispatch.id.name, paramTypes).get();
        }

        if (methodDeclaration.belongs.equals("Object")) {
            if (methodDeclaration.methodName.equals("type_name")) {
                return o.coolString(obj.type.className());
            } else if (methodDeclaration.methodName.equals("copy")) {
                return obj.copy();
            } else if (methodDeclaration.methodName.equals("abort")) {
                return o.coolObject().abort();
            }
        } else if (methodDeclaration.belongs.equals("IO")) {
            if (methodDeclaration.methodName.equals("out_string")) {
                System.out.print(((CoolString) paramObjects.get(0)).str);
                return obj;
            } else if (methodDeclaration.methodName.equals("out_int")) {
                System.out.print(((CoolInt) paramObjects.get(0)).val);
                return obj;
            } else if (methodDeclaration.methodName.equals("in_string")) {
                try {
                    String str = _.reader().readLine();
                    return o.coolString(str);
                } catch (Exception e) {
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
                return ((CoolString) obj).length();
            } else if (methodDeclaration.methodName.equals("concat")) {
                return ((CoolString) obj).concat((CoolString) paramObjects.get(0));
            } else if (methodDeclaration.methodName.equals("substr")) {
                return ((CoolString) obj).substr((CoolInt) paramObjects.get(0), (CoolInt) paramObjects.get(1));
            }
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
