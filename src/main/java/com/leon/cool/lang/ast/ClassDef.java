package com.leon.cool.lang.ast;

import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.type.Type;

import java.util.List;
import java.util.Optional;

/**
 * Created by leon on 15-10-31.
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
