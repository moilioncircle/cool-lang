package com.leon.cool.lang.ast;

import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.CgenSupport;
import com.leon.cool.lang.support.Context;
import com.leon.cool.lang.tree.EvalTreeVisitor;
import com.leon.cool.lang.tree.TreeVisitor;

import java.io.PrintStream;

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
public class BoolConst extends Expression {

    public final Boolean bool;

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
    public CoolObject accept(EvalTreeVisitor visitor, Context context) {
        return visitor.applyBoolConst(this, context);
    }

    public void codeRef(PrintStream s) {
        s.print(CgenSupport.BOOLCONST_PREFIX + (bool ? "1" : "0"));
    }

    public void codeDef(PrintStream s) {
        s.println(CgenSupport.WORD + "-1");
        codeRef(s);
        s.print(CgenSupport.LABEL); // label
        s.println(CgenSupport.WORD + 3); // tag
        s.println(CgenSupport.WORD + (CgenSupport.DEFAULT_OBJFIELDS + CgenSupport.BOOL_SLOTS)); // size
        s.print(CgenSupport.WORD);
        s.println("Bool_dispTab");
        s.println(CgenSupport.WORD + (bool ? "1" : "0"));
    }
}
