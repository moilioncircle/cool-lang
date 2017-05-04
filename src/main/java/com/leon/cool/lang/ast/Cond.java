package com.leon.cool.lang.ast;

import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.infrastructure.Context;
import com.leon.cool.lang.tree.compile.TreeVisitor;
import com.leon.cool.lang.tree.runtime.EvalTreeVisitor;

/**
 * Copyright leon
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author leon on 15-10-31
 */
public class Cond extends Expression {
    public final Expression condExpr;
    public final Expression thenExpr;
    public final Expression elseExpr;

    public Cond(Expression condExpr, Expression thenExpr, Expression elseExpr) {
        this.condExpr = condExpr;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
    }

    @Override
    public String toString() {
        return "Cond{" +
                "condExpr=" + condExpr +
                ", thenExpr=" + thenExpr +
                ", elseExpr=" + elseExpr +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyCond(this);
    }

    @Override
    public CoolObject accept(EvalTreeVisitor visitor, Context context) {
        return visitor.applyCond(this, context);
    }

}
