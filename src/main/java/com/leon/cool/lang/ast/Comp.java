package com.leon.cool.lang.ast;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.object.CoolBool;
import com.leon.cool.lang.object.CoolInt;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.object.CoolString;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.support.Utils;
import com.leon.cool.lang.tree.TreeVisitor;

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
public class Comp extends Expression {
    public final Expression left;
    public final Expression right;

    public Comp(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "Comp{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyComp(this);
    }

    @Override
    public CoolObject eval(Env env) {
        CoolObject l = left.eval(env);
        CoolObject r = right.eval(env);
        if (Utils.isBasicType(l.type) && Utils.isBasicType(r.type)) {
            if (l instanceof CoolString && r instanceof CoolString) {
                return ObjectFactory.coolBool(((CoolString) l).str.equals(((CoolString) r).str));
            } else if (l instanceof CoolInt && r instanceof CoolInt) {
                return ObjectFactory.coolBool(((CoolInt) l).val == ((CoolInt) r).val);
            } else if (l instanceof CoolBool && r instanceof CoolBool) {
                return ObjectFactory.coolBool(((CoolBool) l).val == ((CoolBool) r).val);
            } else {
                throw new AssertionError("unexpected error.");
            }
        } else {
            return ObjectFactory.coolBool(l.equals(r));
        }
    }
}
