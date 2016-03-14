package com.leon.cool.lang.ast;

import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.Context;
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
 * Copyright leon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author leon on 15-10-31
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
    public CoolObject eval(Context context) {
        /**
         * 对方法调用的参数求值
         */
        List<CoolObject> paramObjects = dispatch.params.stream().map(e -> e.eval(context)).collect(Collectors.toList());
        /**
         * 对上述参数表达式求得类型
         */
        List<Type> paramTypes = paramObjects.stream().map(e -> e.type).collect(Collectors.toList());
        MethodDeclaration methodDeclaration;
        // expr[@TYPE].ID( [ expr [[, expr]] ∗ ] )对第一个expr求值
        CoolObject obj = expr.eval(context);
        if (obj.type.type() == TypeEnum.VOID) {
            Utils.error("runtime.error.dispatch.void", Utils.errorPos(expr));
        }
        //如果提供type，则根据type查找方法声明
        //如果没提供type，则根据上述expr值的类型查找方法声明
        if (type.isPresent()) {
            methodDeclaration = Utils.lookupMethodDeclaration(type.get().name, dispatch.id.name, paramTypes).get();
        } else {
            methodDeclaration = Utils.lookupMethodDeclaration(obj.type.className(), dispatch.id.name, paramTypes).get();
        }

        CoolObject str = Utils.buildIn(paramObjects, obj, methodDeclaration, Utils.errorPos(starPos, endPos));
        if (str != null) return str;

        /**
         * 进入scope,此scope是上述expr值对象的scope
         */
        obj.variables.enterScope();
        assert paramObjects.size() == methodDeclaration.declaration.formals.size();
        /**
         * 绑定形参
         */
        for (int i = 0; i < methodDeclaration.declaration.formals.size(); i++) {
            obj.variables.addId(methodDeclaration.declaration.formals.get(i).id.name, paramObjects.get(i));
        }
        //对函数体求值
        CoolObject object = methodDeclaration.declaration.expr.eval(new Context(obj,obj.variables));
        /**
         * 退出scope
         */
        obj.variables.exitScope();
        return object;
    }
}
