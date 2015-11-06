package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.ast.MethodDef;
import com.leon.cool.lang.support.MethodDeclaration;
import com.leon.cool.lang.support.Utils;

import java.util.stream.Collectors;

/**
 * Copyright leon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author leon on 15-10-15
 */
public class MethodDefTreeScanner extends TreeScanner {
    private String className;

    public void applyClassDef(ClassDef classDef) {
        className = classDef.type.name;
        Utils.createMethodGraph(className);
        super.applyClassDef(classDef);
    }

    public void applyMethodDef(MethodDef methodDef) {
        if (Utils.isTypeDefined(methodDef.type)) {
            Utils.error("type.error.undefined", methodDef.type.name, Utils.errorPos(methodDef.type));
        }
        MethodDeclaration methodDeclaration = new MethodDeclaration();
        methodDeclaration.methodName = methodDef.id.name;
        methodDeclaration.returnType = methodDef.type.name;
        methodDeclaration.paramTypes = methodDef.formals.stream().map(e -> {
            if (Utils.isTypeDefined(e.type)) {
                Utils.error("type.error.undefined", e.type.name, Utils.errorPos(e.type));
            }
            return e.type.name;
        }).collect(Collectors.toList());
        methodDeclaration.declaration = methodDef;
        methodDeclaration.belongs = className;
        Utils.putToMethodGraph(className, methodDeclaration);
        super.applyMethodDef(methodDef);
    }
}
