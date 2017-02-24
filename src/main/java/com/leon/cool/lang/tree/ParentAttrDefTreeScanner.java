package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.support.ScannerSupport;

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
 * @author leon on 15-10-19
 */
public class ParentAttrDefTreeScanner extends TreeScanner {

    public ParentAttrDefTreeScanner(ScannerSupport scannerSupport) {
        super(scannerSupport);
    }

    public void applyClassDef(ClassDef classDef) {
        String className = classDef.type.name;
        scannerSupport.createSymbolTable(className);
        scannerSupport.lookupSymbolTable(className).enterScope();
        scannerSupport.mergeAttrGraph(className);
        super.applyClassDef(classDef);
    }
}
