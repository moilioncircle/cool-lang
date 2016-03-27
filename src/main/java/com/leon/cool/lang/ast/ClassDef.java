package com.leon.cool.lang.ast;

import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.type.Type;

import java.util.List;
import java.util.Optional;

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
public class ClassDef extends TreeNode {
    public Type typeInfo;
    public final Token type;
    public final Optional<Token> inheritsType;
    public final List<Feature> features;

    public ClassDef(Token type, Optional<Token> inheritsType, List<Feature> features) {
        this.type = type;
        this.inheritsType = inheritsType;
        this.features = features;
    }

    @Override
    public String toString() {
        return "ClassDef{" +
                "typeInfo=" + typeInfo +
                ", type=" + type +
                ", inheritsType=" + inheritsType +
                ", features=" + features +
                '}';
    }

    public void accept(TreeVisitor visitor) {
        visitor.applyClassDef(this);
    }
}
