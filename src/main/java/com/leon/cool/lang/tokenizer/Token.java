package com.leon.cool.lang.tokenizer;

import com.leon.cool.lang.ast.TreeNode;
import com.leon.cool.lang.support.infrastructure.Pos;
import com.leon.cool.lang.tree.TreeElement;
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
 * @author leon on 15-10-7
 */
public class Token extends TreeNode implements TreeElement {
    public final String name;
    public final TokenKind kind;
    public Pos startPos;
    public Pos endPos;

    public Token(String name, TokenKind kind) {
        this.name = name;
        this.kind = kind;
    }

    public Token(String name, TokenKind kind, Pos startPos) {
        this(name, kind);
        this.startPos = startPos;
        this.endPos = new Pos(startPos.column + (name == null ? 0 : name.length()), startPos.row);
    }

    @Override
    public String toString() {
        return "Token{" +
                "name='" + name + '\'' +
                ", kind=" + kind +
                ", startPos=" + startPos +
                ", endPos=" + endPos +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyToken(this);
    }
}
