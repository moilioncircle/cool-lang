package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.AttrDef;
import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.support.AttrDeclaration;
import com.leon.cool.lang.support._;

/**
 * Created by leon on 15-10-15.
 */
public class AttrDefTreeScanner extends TreeScanner {
    private String className = null;

    public void applyClassDef(ClassDef classDef) {
        className = classDef.type.name;
        _.createAttrGraph(className);
        super.applyClassDef(classDef);
    }

    public void applyAttrDef(AttrDef attrDef) {
        if (_.isSelf(attrDef.id)) {
            _.error("Not allowed to assign to 'self'" + _.errorPos(attrDef.id));
        }
        AttrDeclaration attrDeclaration = new AttrDeclaration();
        attrDeclaration.id = attrDef.id.name;
        attrDeclaration.type = attrDef.type.name;
        attrDeclaration.expr = attrDef.expr;
        _.putToAttrGraph(className, attrDef.id.name, attrDeclaration);
        super.applyAttrDef(attrDef);
    }
}
