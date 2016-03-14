package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Context;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.tokenizer.Token;

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
public class Branch extends Expression {
    public final Token id;
    public final Token type;
    public final Expression expr;

    public Branch(Token id, Token type, Expression expr) {
        this.id = id;
        this.type = type;
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", type=" + type +
                ", expr=" + expr +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyBranch(this);
    }

    @Override
    public CoolObject eval(Context context) {
        return null;
    }
}
