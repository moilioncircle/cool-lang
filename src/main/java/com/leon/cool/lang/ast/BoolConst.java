package com.leon.cool.lang.ast;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolObject;

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
public class BoolConst extends Expression {
    private final Boolean bool;

    public BoolConst(Boolean bool) {
        this.bool = bool;
    }

    @Override
    public String toString() {
        return "BoolConst{" +
                "bool=" + bool +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyBoolConst(this);
    }

    @Override
    public CoolObject eval(Env env) {
        return ObjectFactory.coolBool(bool);
    }
}
