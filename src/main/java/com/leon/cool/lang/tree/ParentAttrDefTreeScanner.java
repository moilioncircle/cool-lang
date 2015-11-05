package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.support._;

/**
 * Created by leon on 15-10-19.
 */
public class ParentAttrDefTreeScanner extends TreeScanner {
    private String className;

    public void applyClassDef(ClassDef classDef) {
        className = classDef.type.name;
        _.createSymbolTable(className);
        _.lookupSymbolTable(className).enterScope();
        _.mergeAttrGraph(className);
        super.applyClassDef(classDef);
    }
}
