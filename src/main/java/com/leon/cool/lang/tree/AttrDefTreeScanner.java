package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.AttrDef;
import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.support.AttrDeclaration;
import com.leon.cool.lang.support.Utils;

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
public class AttrDefTreeScanner extends TreeScanner {
    private String className = null;

    public void applyClassDef(ClassDef classDef) {
        className = classDef.type.name;
        Utils.createAttrGraph(className);
        super.applyClassDef(classDef);
    }

    public void applyAttrDef(AttrDef attrDef) {
        if (Utils.isSelf(attrDef.id)) {
            Utils.error("type.error.assign.self", Utils.errorPos(attrDef.id));
        }
        AttrDeclaration attrDeclaration = new AttrDeclaration();
        attrDeclaration.id = attrDef.id.name;
        attrDeclaration.type = attrDef.type.name;
        attrDeclaration.expr = attrDef.expr;
        Utils.putToAttrGraph(className, attrDef.id.name, attrDeclaration);
        super.applyAttrDef(attrDef);
    }
}
