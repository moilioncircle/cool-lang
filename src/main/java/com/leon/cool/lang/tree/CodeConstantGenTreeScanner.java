package com.leon.cool.lang.tree;

import com.leon.cool.lang.Constant;
import com.leon.cool.lang.ast.*;
import com.leon.cool.lang.support.CgenSupport;
import com.leon.cool.lang.support.ScannerSupport;
import com.leon.cool.lang.support.infrastructure.ConstantPool;

import java.io.PrintStream;

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
 * @author leon on 16-04-03
 */
public class CodeConstantGenTreeScanner extends TreeScanner {

    private final PrintStream str;

    private final String fileName;

    public CodeConstantGenTreeScanner(ScannerSupport scannerSupport, String fileName) {
        super(scannerSupport);
        str = System.err;
        this.fileName = fileName;
        codeGlobalData();
        codeSelectGc();
        codeConstants();
    }

    @Override
    public void applyClassDef(ClassDef classDef) {
        ConstantPool.getInstance().addString(classDef.type.name);
        super.applyClassDef(classDef);
    }

    @Override
    public void applyIdConst(IdConst idConst) {
        super.applyIdConst(idConst);
    }

    @Override
    public void applyStringConst(StringConst stringConst) {
        ConstantPool.getInstance().addString(stringConst.tok.name);
        super.applyStringConst(stringConst);
    }

    @Override
    public void applyBoolConst(BoolConst boolConst) {
        super.applyBoolConst(boolConst);
    }

    @Override
    public void applyIntConst(IntConst intConst) {
        ConstantPool.getInstance().addInt(Integer.parseInt(intConst.tok.name));
        super.applyIntConst(intConst);
    }

    private void codeGlobalData() {
        str.print("\t.data\n" + CgenSupport.ALIGN);
        str.println(CgenSupport.GLOBAL + CgenSupport.CLASSNAMETAB);
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitProtObjRef(Constant.MAIN_CLASS, str);
        str.println();
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitProtObjRef(Constant.INT, str);
        str.println();
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitProtObjRef(Constant.STRING, str);
        str.println();
        str.print(CgenSupport.GLOBAL);
        ConstantPool.getInstance().addBool(false).codeRef(str);
        str.println();
        str.print(CgenSupport.GLOBAL);
        ConstantPool.getInstance().addBool(true).codeRef(str);
        str.println();
        str.println(CgenSupport.GLOBAL + CgenSupport.INTTAG);
        str.println(CgenSupport.GLOBAL + CgenSupport.BOOLTAG);
        str.println(CgenSupport.GLOBAL + CgenSupport.STRINGTAG);
        str.println(CgenSupport.INTTAG + CgenSupport.LABEL + CgenSupport.WORD + Constant.INT_TAG);
        str.println(CgenSupport.BOOLTAG + CgenSupport.LABEL + CgenSupport.WORD + Constant.BOOL_TAG);
        str.println(CgenSupport.STRINGTAG + CgenSupport.LABEL + CgenSupport.WORD + Constant.STRING_TAG);

    }

    private void codeSelectGc() {
        str.println(CgenSupport.GLOBAL + "_MemMgr_INITIALIZER");
        str.println("_MemMgr_INITIALIZER:");
        str.println(CgenSupport.WORD
                + CgenSupport.gcInitNames[0]);

        str.println(CgenSupport.GLOBAL + "_MemMgr_COLLECTOR");
        str.println("_MemMgr_COLLECTOR:");
        str.println(CgenSupport.WORD
                + CgenSupport.gcCollectNames[0]);

        str.println(CgenSupport.GLOBAL + "_MemMgr_TEST");
        str.println("_MemMgr_TEST:");
        str.println(CgenSupport.WORD + "0");
    }

    private void codeConstants() {
        ConstantPool.getInstance().addString("<basic class>");
        ConstantPool.getInstance().addString(fileName);
    }
}
