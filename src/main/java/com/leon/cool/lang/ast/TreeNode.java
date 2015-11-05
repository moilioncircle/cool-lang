package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Dumpable;
import com.leon.cool.lang.tree.TreeElement;
import com.leon.cool.lang.type.Type;
import com.leon.cool.lang.util.Pos;

/**
 * Created by leon on 15-10-8.
 */
public abstract class TreeNode implements Dumpable, TreeElement {
    public Type typeInfo;
    public Pos starPos;
    public Pos endPos;

    @Override
    public void dump() {
        System.out.println(this.toString());
    }
}

