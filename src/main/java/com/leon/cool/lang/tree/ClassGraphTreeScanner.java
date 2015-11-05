package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.ast.Program;
import com.leon.cool.lang.ast.StaticDispatch;
import com.leon.cool.lang.support._;

/**
 * Created by leon on 15-10-15.
 */
public class ClassGraphTreeScanner extends TreeScanner {

    public void applyProgram(Program program) {
        super.applyProgram(program);
        _.checkUndefinedClass();
    }

    @Override
    public void applyClassDef(ClassDef classDef) {
        if (_.isSelfType(classDef.type)) {
            _.error("class name can not be SELF_TYPE" + _.errorPos(classDef.type));
        }
        if (classDef.inheritsType.isPresent()) {
            if (_.isSelfType(classDef.inheritsType.get())) {
                _.error("inherits name can not be SELF_TYPE" + _.errorPos(classDef.inheritsType.get()));
            } else if (_.isBasicType(classDef.inheritsType.get())) {
                _.error("inherits name can not be " + classDef.inheritsType.get().name + _.errorPos(classDef.inheritsType.get()));
            }
        }
        _.putToClassGraph(classDef.type.name, classDef.inheritsType.map(e -> e.name));
        super.applyClassDef(classDef);
    }

    public void applyStaticDispatch(StaticDispatch staticDispatch) {
        if (staticDispatch.type.isPresent() && _.isSelfType(staticDispatch.type.get())) {
            _.error("can not allowed to cast to 'SELF_TYPE'" + _.errorPos(staticDispatch.type.get()));
        }
        super.applyStaticDispatch(staticDispatch);
    }
}
