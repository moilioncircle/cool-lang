package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.support._;

/**
 * Created by leon on 15-11-1.
 */
public class ParentMethodDefTreeScanner extends TreeScanner {
    private String className;

    public void applyClassDef(ClassDef classDef) {
        className = classDef.type.name;
        _.mergeMethodGraph(className);
        super.applyClassDef(classDef);
    }
}
