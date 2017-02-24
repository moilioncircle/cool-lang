package com.leon.cool.lang.tree;

import com.leon.cool.lang.Constant;
import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.ast.Program;
import com.leon.cool.lang.ast.StaticDispatch;
import com.leon.cool.lang.support.ErrorSupport;
import com.leon.cool.lang.support.ScannerSupport;
import com.leon.cool.lang.support.TypeSupport;

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

    public ClassGraphTreeScanner(ScannerSupport scannerSupport) {
        super(scannerSupport);
    }

    public void applyProgram(Program program) {
        super.applyProgram(program);
        scannerSupport.checkUndefinedClass();
    }

    @Override
    public void applyClassDef(ClassDef classDef) {
        if (TypeSupport.isSelfType(classDef.type)) {
            ErrorSupport.error("type.error.self.type", ErrorSupport.errorPos(classDef.type));
        }
        if (classDef.inheritsType.isPresent()) {
            if (TypeSupport.isSelfType(classDef.inheritsType.get())) {
                ErrorSupport.error("type.error.inherits.type", Constant.SELF_TYPE, ErrorSupport.errorPos(classDef.inheritsType.get()));
            } else if (TypeSupport.isBasicType(classDef.inheritsType.get())) {
                ErrorSupport.error("type.error.inherits.type", classDef.inheritsType.get().name, ErrorSupport.errorPos(classDef.inheritsType.get()));
            }
        }
        scannerSupport.putToClassGraph(classDef.type.name, classDef.inheritsType.map(e -> e.name));
        super.applyClassDef(classDef);
    }

    public void applyStaticDispatch(StaticDispatch staticDispatch) {
        if (staticDispatch.type.isPresent() && TypeSupport.isSelfType(staticDispatch.type.get())) {
            ErrorSupport.error("type.error.cast.type", ErrorSupport.errorPos(staticDispatch.type.get()));
        }
        super.applyStaticDispatch(staticDispatch);
    }
}
