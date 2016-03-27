package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.*;
import com.leon.cool.lang.tokenizer.Token;

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
 * @author leon on 15-10-14
 */
public interface TreeVisitor {
    void applyProgram(Program program);

    void applyClassDef(ClassDef classDef);

    void applyMethodDef(MethodDef methodDef);

    void applyAttrDef(AttrDef attrDef);

    void applyLetAttrDef(LetAttrDef letAttrDef);

    void applyFormal(Formal formal);

    void applyAssign(Assign assign);

    void applyDispatch(Dispatch dispatch);

    void applyStaticDispatchBody(StaticDispatchBody staticDispatchBody);

    void applyStaticDispatch(StaticDispatch staticDispatch);

    void applyCond(Cond cond);

    void applyLoop(Loop loop);

    void applyBlocks(Blocks blocks);

    void applyLet(Let let);

    void applyCaseDef(CaseDef caseDef);

    void applyBranch(Branch branch);

    void applyNewDef(NewDef newDef);

    void applyIsVoid(IsVoid isVoid);

    void applyPlus(Plus plus);

    void applySub(Sub sub);

    void applyMul(Mul mul);

    void applyDivide(Divide divide);

    void applyNeg(Neg neg);

    void applyLt(Lt lt);

    void applyLtEq(LtEq ltEq);

    void applyComp(Comp comp);

    void applyNot(Not not);

    void applyIdConst(IdConst idConst);

    void applyStringConst(StringConst stringConst);

    void applyBoolConst(BoolConst boolConst);

    void applyIntConst(IntConst intConst);

    void applyToken(Token token);

    void applyParen(Paren paren);

    void applyNoExpression(NoExpression expr);

}
