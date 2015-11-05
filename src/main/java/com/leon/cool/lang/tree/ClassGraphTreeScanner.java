package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.ast.Program;
import com.leon.cool.lang.ast.StaticDispatch;
import com.leon.cool.lang.support.Utils;

/**
 * Created by leon on 15-10-15.
 */
public class ClassGraphTreeScanner extends TreeScanner {

    public void applyProgram(Program program) {
        super.applyProgram(program);
        Utils.checkUndefinedClass();
    }

    @Override
    public void applyClassDef(ClassDef classDef) {
        if (Utils.isSelfType(classDef.type)) {
            Utils.error("class name can not be SELF_TYPE" + Utils.errorPos(classDef.type));
        }
        if (classDef.inheritsType.isPresent()) {
            if (Utils.isSelfType(classDef.inheritsType.get())) {
                Utils.error("inherits name can not be SELF_TYPE" + Utils.errorPos(classDef.inheritsType.get()));
            } else if (Utils.isBasicType(classDef.inheritsType.get())) {
                Utils.error("inherits name can not be " + classDef.inheritsType.get().name + Utils.errorPos(classDef.inheritsType.get()));
            }
        }
        Utils.putToClassGraph(classDef.type.name, classDef.inheritsType.map(e -> e.name));
        super.applyClassDef(classDef);
    }

    public void applyStaticDispatch(StaticDispatch staticDispatch) {
        if (staticDispatch.type.isPresent() && Utils.isSelfType(staticDispatch.type.get())) {
            Utils.error("can not allowed to cast to 'SELF_TYPE'" + Utils.errorPos(staticDispatch.type.get()));
        }
        super.applyStaticDispatch(staticDispatch);
    }
}
