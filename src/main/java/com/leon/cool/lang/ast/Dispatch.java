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

import java.util.List;
import java.util.stream.Collectors;

/**
* Created by leon on 15-10-31.
*/
public class Dispatch extends Expression {
    public final Token id;
    public final List<Expression> params;

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
        /**
         * 对方法调用的参数求值
         */
        List<CoolObject> paramObjects = params.stream().map(e -> e.eval(env)).collect(Collectors.toList());
        /**
         * 对上述参数表达式求得类型
         */
        List<Type> paramTypes = paramObjects.stream().map(e -> e.type).collect(Collectors.toList());
        CoolObject obj = env.selfObject;
        //根据类型，方法名称，类名lookup方法声明
        MethodDeclaration methodDeclaration = Utils.lookupMethodDeclaration(obj.type.className(), id.name, paramTypes).get();
        /**
         * build-in方法求值
         * =====================================
         */
        switch (methodDeclaration.belongs) {
            case "Object":
                if (methodDeclaration.methodName.equals("type_name")) {
                    return ObjectFactory.coolString(env.selfObject.type.className());
                } else if (methodDeclaration.methodName.equals("copy")) {
                    return env.selfObject.copy();
                } else if (methodDeclaration.methodName.equals("abort")) {
                    return ObjectFactory.coolObject().abort();
                }
                break;
            case "IO":
                if (methodDeclaration.methodName.equals("out_string")) {
                    System.out.print(((CoolString) paramObjects.get(0)).str);
                    return env.selfObject;
                } else if (methodDeclaration.methodName.equals("out_int")) {
                    System.out.print(((CoolInt) paramObjects.get(0)).val);
                    return env.selfObject;
                } else if (methodDeclaration.methodName.equals("in_string")) {
                    try {
                        String str = Utils.reader().readLine();
                        return ObjectFactory.coolString(str);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.error("unexpected.error");
                    }
                    return ObjectFactory.coolStringDefault();
                } else if (methodDeclaration.methodName.equals("in_int")) {
                    try {
                        String str = Utils.reader().readLine();
                        return ObjectFactory.coolInt(Integer.parseInt(str));
                    } catch (Exception e) {
                        Utils.error("unexpected.error");
                    }
                    return ObjectFactory.coolIntDefault();
                }
                break;
            case "String":
                if (methodDeclaration.methodName.equals("length")) {
                    return ((CoolString) env.selfObject).length();
                } else if (methodDeclaration.methodName.equals("concat")) {
                    return ((CoolString) env.selfObject).concat((CoolString) paramObjects.get(0));
                } else if (methodDeclaration.methodName.equals("substr")) {
                    return ((CoolString) env.selfObject).substr((CoolInt) paramObjects.get(0), (CoolInt) paramObjects.get(1),Utils.errorPos(starPos,endPos));
                }
                break;
        }
        /**
         * =====================================
         */
        /**
         * 进入scope
         *
         */
        env.symbolTable.enterScope();
        assert paramObjects.size() == methodDeclaration.declaration.formals.size();
        /**
         * 绑定形参
         */
        for (int i = 0; i < methodDeclaration.declaration.formals.size(); i++) {
            env.symbolTable.addId(methodDeclaration.declaration.formals.get(i).id.name, paramObjects.get(i));
        }
        //对函数体求值
        CoolObject object = methodDeclaration.declaration.expr.eval(env);
        /**
         * 退出scope
         */
        env.symbolTable.exitScope();
        return object;
    }
}
