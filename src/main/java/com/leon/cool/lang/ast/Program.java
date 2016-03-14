package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Context;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.tokenizer.TokenKind;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class Program extends TreeNode {
    public final List<ClassDef> classDef;

    public Program(List<ClassDef> classDef) {
        this.classDef = classDef;
    }

    @Override
    public String toString() {
        return "Program{" +
                "classDef=" + classDef +
                '}';
    }

    public CoolObject run() {
        Expression staticDispatch = new StaticDispatch(new NewDef(new Token("Main", TokenKind.TYPE)), Optional.<Token>empty(), new StaticDispatchBody(new Token("main", TokenKind.ID), new ArrayList<>()));
        return staticDispatch.eval(new Context(null,null));
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyProgram(this);
    }
}
