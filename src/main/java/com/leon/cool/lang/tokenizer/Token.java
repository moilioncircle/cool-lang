package com.leon.cool.lang.tokenizer;

import com.leon.cool.lang.ast.TreeNode;
import com.leon.cool.lang.tree.TreeElement;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.util.Pos;

/**
 * Created by leon on 15-10-7.
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
