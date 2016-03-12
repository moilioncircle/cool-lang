package com.leon.cool.lang.ast;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.support.Context;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolObject;

import java.util.List;

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
public class Blocks extends Expression {
    public final List<Expression> exprs;

    public Blocks(List<Expression> exprs) {
        this.exprs = exprs;
    }

    @Override
    public String toString() {
        return "Blocks{" +
                "exprs=" + exprs +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyBlocks(this);
    }

    @Override
    public CoolObject eval(Context context) {
        CoolObject object = ObjectFactory.coolVoid();
        for (Expression expr : exprs) {
            object = expr.eval(context);
        }
        return object;
    }
}
