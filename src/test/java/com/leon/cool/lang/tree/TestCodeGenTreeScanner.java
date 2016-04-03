package com.leon.cool.lang.tree;

import com.leon.cool.lang.Main;
import com.leon.cool.lang.util.FileUtils;
import org.junit.Test;

import static org.junit.Assert.*;

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
public class TestCodeGenTreeScanner {

    @Test
    public void testApplyAssign() throws Exception {

    }

    @Test
    public void testApplyBlocks() throws Exception {

    }

    @Test
    public void testApplyNewDef() throws Exception {

    }

    @Test
    public void testApplyIsVoid() throws Exception {

    }

    @Test
    public void testApplyPlus() throws Exception {

    }

    @Test
    public void testApplySub() throws Exception {
        String str = FileUtils.readJarFile("tree/sub.cl");
        Main.run(str);
    }

    @Test
    public void testApplyMul() throws Exception {
    }

    @Test
    public void testApplyDivide() throws Exception {

    }

    @Test
    public void testApplyNeg() throws Exception {

    }

    @Test
    public void testApplyLt() throws Exception {

    }

    @Test
    public void testApplyLtEq() throws Exception {

    }

    @Test
    public void testApplyComp() throws Exception {

    }

    @Test
    public void testApplyNot() throws Exception {

    }

    @Test
    public void testApplyIdConst() throws Exception {

    }

    @Test
    public void testApplyStringConst() throws Exception {

    }

    @Test
    public void testApplyBoolConst() throws Exception {

    }

    @Test
    public void testApplyIntConst() throws Exception {

    }

    @Test
    public void testApplyParen() throws Exception {

    }

    @Test
    public void testApplyNoExpression() throws Exception {

    }

    @Test
    public void testApplyDispatch() throws Exception {

    }

    @Test
    public void testApplyStaticDispatchBody() throws Exception {

    }

    @Test
    public void testApplyStaticDispatch() throws Exception {

    }

    @Test
    public void testApplyCond() throws Exception {

    }

    @Test
    public void testApplyLoop() throws Exception {

    }

    @Test
    public void testApplyLet() throws Exception {

    }

    @Test
    public void testApplyCaseDef() throws Exception {

    }

    @Test
    public void testApplyBranch() throws Exception {

    }

    @Test
    public void testApplyLetAttrDef() throws Exception {

    }
}