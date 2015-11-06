package com.leon.cool.lang.ast;

import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.tokenizer.Token;

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
public class MethodDef extends Feature {
    public final Token id;
    public final List<Formal> formals;
    public final Token type;
    public final Expression expr;

    public MethodDef(Token id, List<Formal> formals, Token type, Expression expr) {
        this.id = id;
        this.formals = formals;
        this.type = type;
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "MethodDef{" +
                "id=" + id +
                ", formals=" + formals +
                ", type=" + type +
                ", expr=" + expr +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyMethodDef(this);
    }
}
