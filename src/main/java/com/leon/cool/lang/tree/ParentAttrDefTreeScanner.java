package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.support.Utils;

/**
 * Created by leon on 15-10-19.
 */
public class ParentAttrDefTreeScanner extends TreeScanner {

    public void applyClassDef(ClassDef classDef) {
        String className = classDef.type.name;
        Utils.createSymbolTable(className);
        Utils.lookupSymbolTable(className).enterScope();
        Utils.mergeAttrGraph(className);
        super.applyClassDef(classDef);
    }
}
