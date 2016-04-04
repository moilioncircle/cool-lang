package com.leon.cool.lang.ast;

import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.CgenSupport;
import com.leon.cool.lang.support.Context;
import com.leon.cool.lang.tokenizer.Token;
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
public class IntConst extends Expression {

    public int index = 0;

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

    public void codeDef(PrintStream s) {
        s.println(CgenSupport.WORD + "-1");
        codeRef(s, index);
        s.print(CgenSupport.LABEL);
        s.println(CgenSupport.WORD + 2);
        s.println(CgenSupport.WORD + (CgenSupport.DEFAULT_OBJFIELDS + CgenSupport.INT_SLOTS)); // size
        s.print(CgenSupport.WORD);
        s.println("Int_dispTab");
        s.println(CgenSupport.WORD + tok.name);
    }

    public void codeRef(PrintStream s, int index) {
        s.print(CgenSupport.INTCONST_PREFIX + index);
    }

    public void codeRef(PrintStream s) {
        s.print(CgenSupport.INTCONST_PREFIX + index);
    }

    /**
     * Returns a copy of this symbol
     */
    public Object clone() {
        return new IntConst(new Token(tok.name, tok.kind, tok.startPos));
    }
}
