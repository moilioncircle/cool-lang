package com.leon.cool.lang.ast;

import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.Context;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.tree.EvalTreeVisitor;
import com.leon.cool.lang.tree.TreeVisitor;

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
public class IntConst extends Expression {
    public final Token tok;

    public IntConst(Token tok) {
        this.tok = tok;
    }

    @Override
    public String toString() {
        return "IntConst{" +
                "tok=" + tok +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyIntConst(this);
    }

    @Override
    public CoolObject accept(EvalTreeVisitor visitor, Context context) {
        return visitor.applyIntConst(this, context);
    }

}
