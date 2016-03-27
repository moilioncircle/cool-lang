package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.ast.Program;
import com.leon.cool.lang.ast.StaticDispatch;
import com.leon.cool.lang.support.Utils;
import com.leon.cool.lang.util.Constant;

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

    public void applyProgram(Program program) {
        super.applyProgram(program);
        Utils.checkUndefinedClass();
    }

    @Override
    public void applyClassDef(ClassDef classDef) {
        if (Utils.isSelfType(classDef.type)) {
            Utils.error("type.error.self.type", Utils.errorPos(classDef.type));
        }
        if (classDef.inheritsType.isPresent()) {
            if (Utils.isSelfType(classDef.inheritsType.get())) {
                Utils.error("type.error.inherits.type", Constant.SELF_TYPE, Utils.errorPos(classDef.inheritsType.get()));
            } else if (Utils.isBasicType(classDef.inheritsType.get())) {
                Utils.error("type.error.inherits.type", classDef.inheritsType.get().name, Utils.errorPos(classDef.inheritsType.get()));
            }
        }
        Utils.putToClassGraph(classDef.type.name, classDef.inheritsType.map(e -> e.name));
        super.applyClassDef(classDef);
    }

    public void applyStaticDispatch(StaticDispatch staticDispatch) {
        if (staticDispatch.type.isPresent() && Utils.isSelfType(staticDispatch.type.get())) {
            Utils.error("type.error.cast.type", Utils.errorPos(staticDispatch.type.get()));
        }
        super.applyStaticDispatch(staticDispatch);
    }
}
