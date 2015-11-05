package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.AttrDef;
import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.support.AttrDeclaration;
import com.leon.cool.lang.support.Utils;

/**
 * Created by leon on 15-10-15.
 */
public class AttrDefTreeScanner extends TreeScanner {
    private String className = null;

    public void applyClassDef(ClassDef classDef) {
        className = classDef.type.name;
        Utils.createAttrGraph(className);
        super.applyClassDef(classDef);
    }

    public void applyAttrDef(AttrDef attrDef) {
        if (Utils.isSelf(attrDef.id)) {
            Utils.error("Not allowed to assign to 'self'" + Utils.errorPos(attrDef.id));
        }
        AttrDeclaration attrDeclaration = new AttrDeclaration();
        attrDeclaration.id = attrDef.id.name;
        attrDeclaration.type = attrDef.type.name;
        attrDeclaration.expr = attrDef.expr;
        Utils.putToAttrGraph(className, attrDef.id.name, attrDeclaration);
        super.applyAttrDef(attrDef);
    }
}
