package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.support.Utils;

/**
 * Created by leon on 15-11-1.
 */
public class ParentMethodDefTreeScanner extends TreeScanner {

    public void applyClassDef(ClassDef classDef) {
        String className = classDef.type.name;
        Utils.mergeMethodGraph(className);
        super.applyClassDef(classDef);
    }
}
