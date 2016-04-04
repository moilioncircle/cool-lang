package com.leon.cool.lang.ast;

import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.CgenSupport;
import com.leon.cool.lang.support.ConstantPool;
import com.leon.cool.lang.support.Context;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.tree.EvalTreeVisitor;
import com.leon.cool.lang.tree.TreeVisitor;

import java.io.PrintStream;

/**
 * Copyright (c) 2000 The Regents of the University of California.
 * All rights reserved.
 * <p>
 * Permission to use, copy, modify, and distribute this software for any
 * purpose, without fee, and without written agreement is hereby granted,
 * provided that the above copyright notice and the following two
 * paragraphs appear in all copies of this software.
 * <p>
 * IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
 * OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
 * CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <p>
 * THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
 * ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */
public class StringConst extends Expression {

    public int index = 0;

    public final Token tok;

    public StringConst(Token tok) {
        this.tok = tok;
    }

    @Override
    public String toString() {
        return "StringConst{" +
                "tok=" + tok +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyStringConst(this);
    }

    @Override
    public CoolObject accept(EvalTreeVisitor visitor, Context context) {
        return visitor.applyStringConst(this, context);
    }

    public void codeDef(PrintStream s) {
        IntConst lensym = ConstantPool.getInstance().addInt(tok.name.length());
        s.println(CgenSupport.WORD + "-1");
        codeRef(s, index);
        s.print(CgenSupport.LABEL);
        s.println(CgenSupport.WORD + 4);
        s.println(CgenSupport.WORD + (CgenSupport.DEFAULT_OBJFIELDS + CgenSupport.STRING_SLOTS + (tok.name.length() + 4) / 4));
        s.print(CgenSupport.WORD);

        s.println("String_dispTab");
        s.print(CgenSupport.WORD);
        lensym.codeRef(s, lensym.index);
        s.println();
        CgenSupport.emitStringConstant(tok.name, s);
        s.print(CgenSupport.ALIGN);
    }

    public void codeRef(PrintStream s, int index) {
        s.print(CgenSupport.STRCONST_PREFIX + index);
    }

    public void codeRef(PrintStream s) {
        s.print(CgenSupport.STRCONST_PREFIX + index);
    }

    /**
     * Returns a copy of this symbol
     */
    public Object clone() {
        return new StringConst(new Token(tok.name, tok.kind, tok.startPos));
    }
}
