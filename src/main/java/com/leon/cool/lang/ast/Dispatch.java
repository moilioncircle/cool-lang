package com.leon.cool.lang.ast;

import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.Context;
import com.leon.cool.lang.support.MethodDeclaration;
import com.leon.cool.lang.support.Utils;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.type.Type;

import java.util.List;
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
    public CoolObject eval(Context context) {
        /**
         * 对方法调用的参数求值
         */
        List<CoolObject> paramObjects = params.stream().map(e -> e.eval(context)).collect(Collectors.toList());
        /**
         * 对上述参数表达式求得类型
         */
        List<Type> paramTypes = paramObjects.stream().map(e -> e.type).collect(Collectors.toList());
        CoolObject obj = context.selfObject;
        //根据类型，方法名称，类名lookup方法声明
        MethodDeclaration methodDeclaration = Utils.lookupMethodDeclaration(obj.type.className(), id.name, paramTypes).get();

        CoolObject str = Utils.buildIn(paramObjects, obj, methodDeclaration, Utils.errorPos(starPos, endPos));
        if (str != null) return str;

        /**
         * 进入scope
         *
         */
        context.environment.enterScope();
        assert paramObjects.size() == methodDeclaration.declaration.formals.size();
        /**
         * 绑定形参
         */
        for (int i = 0; i < methodDeclaration.declaration.formals.size(); i++) {
            context.environment.addId(methodDeclaration.declaration.formals.get(i).id.name, paramObjects.get(i));
        }
        //对函数体求值
        CoolObject object = methodDeclaration.declaration.expr.eval(context);
        /**
         * 退出scope
         */
        context.environment.exitScope();
        return object;
    }

}
