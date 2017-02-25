package com.leon.cool.lang.ast;

import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.infrastructure.Context;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.tree.EvalTreeVisitor;
import com.leon.cool.lang.tree.TreeVisitor;

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

    /**
     * Returns a copy of this symbol
     */
    @Override
    public Object clone() {
        return new StringConst(new Token(tok.name, tok.kind, tok.startPos));
    }
}
