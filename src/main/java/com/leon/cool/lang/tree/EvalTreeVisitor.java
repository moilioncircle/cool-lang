package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.*;
import com.leon.cool.lang.glossary.Out;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.infrastructure.Context;

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
 * @author leon on 16-3-27
 */
public interface EvalTreeVisitor {

    CoolObject applyAssign(Assign assign, @Out Context context);

    CoolObject applyBlocks(Blocks blocks, @Out Context context);

    CoolObject applyNewDef(NewDef newDef, @Out Context context);

    CoolObject applyIsVoid(IsVoid isVoid, @Out Context context);

    CoolObject applyPlus(Plus plus, @Out Context context);

    CoolObject applySub(Sub sub, @Out Context context);

    CoolObject applyMul(Mul mul, @Out Context context);

    CoolObject applyDivide(Divide divide, @Out Context context);

    CoolObject applyNeg(Neg neg, @Out Context context);

    CoolObject applyLt(Lt lt, @Out Context context);

    CoolObject applyLtEq(LtEq ltEq, @Out Context context);

    CoolObject applyComp(Comp comp, @Out Context context);

    CoolObject applyNot(Not not, @Out Context context);

    CoolObject applyIdConst(IdConst idConst, @Out Context context);

    CoolObject applyStringConst(StringConst stringConst, @Out Context context);

    CoolObject applyBoolConst(BoolConst boolConst, @Out Context context);

    CoolObject applyIntConst(IntConst intConst, @Out Context context);

    CoolObject applyParen(Paren paren, @Out Context context);

    CoolObject applyNoExpression(NoExpression expr, @Out Context context);

    CoolObject applyDispatch(Dispatch dispatch, @Out Context context);

    CoolObject applyStaticDispatchBody(StaticDispatchBody staticDispatchBody, @Out Context context);

    CoolObject applyStaticDispatch(StaticDispatch staticDispatch, @Out Context context);

    CoolObject applyCond(Cond cond, @Out Context context);

    CoolObject applyLoop(Loop loop, @Out Context context);

    CoolObject applyLet(Let let, @Out Context context);

    CoolObject applyCaseDef(CaseDef caseDef, @Out Context context);

    CoolObject applyBranch(Branch branch, @Out Context context);

    CoolObject applyLetAttrDef(LetAttrDef letAttrDef, @Out Context context);
}
