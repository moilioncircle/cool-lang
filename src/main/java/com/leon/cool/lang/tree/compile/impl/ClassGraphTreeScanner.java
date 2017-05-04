package com.leon.cool.lang.tree.compile.impl;

import com.leon.cool.lang.Constant;
import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.ast.Program;
import com.leon.cool.lang.ast.StaticDispatch;
import com.leon.cool.lang.support.TreeSupport;
import com.leon.cool.lang.tree.compile.TreeScanner;

import static com.leon.cool.lang.support.ErrorSupport.error;
import static com.leon.cool.lang.support.ErrorSupport.errorPos;
import static com.leon.cool.lang.support.TypeSupport.isBasicType;
import static com.leon.cool.lang.support.TypeSupport.isSelfType;

/**
 * Copyright leon
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author leon on 15-10-15
 */
public class ClassGraphTreeScanner extends TreeScanner {

    public ClassGraphTreeScanner(TreeSupport treeSupport) {
        super(treeSupport);
    }

    public void applyProgram(Program program) {
        super.applyProgram(program);
        treeSupport.checkUndefinedClass();
    }

    @Override
    public void applyClassDef(ClassDef classDef) {
        if (isSelfType(classDef.type)) {
            error("type.error.self.type", errorPos(classDef.type));
        }
        if (classDef.inheritsType.isPresent()) {
            if (isSelfType(classDef.inheritsType.get())) {
                error("type.error.inherits.type", Constant.SELF_TYPE, errorPos(classDef.inheritsType.get()));
            } else if (isBasicType(classDef.inheritsType.get())) {
                error("type.error.inherits.type", classDef.inheritsType.get().name, errorPos(classDef.inheritsType.get()));
            }
        }
        treeSupport.putToClassGraph(classDef.type.name, classDef.inheritsType.map(e -> e.name));
        super.applyClassDef(classDef);
    }

    public void applyStaticDispatch(StaticDispatch staticDispatch) {
        if (staticDispatch.type.isPresent() && isSelfType(staticDispatch.type.get())) {
            error("type.error.cast.type", errorPos(staticDispatch.type.get()));
        }
        super.applyStaticDispatch(staticDispatch);
    }
}
