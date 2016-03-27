package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.*;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.Context;

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

    CoolObject applyAssign(Assign assign, Context context);

    CoolObject applyBlocks(Blocks blocks, Context context);

    CoolObject applyNewDef(NewDef newDef, Context context);

    CoolObject applyIsVoid(IsVoid isVoid, Context context);

    CoolObject applyPlus(Plus plus, Context context);

    CoolObject applySub(Sub sub, Context context);

    CoolObject applyMul(Mul mul, Context context);

    CoolObject applyDivide(Divide divide, Context context);

    CoolObject applyNeg(Neg neg, Context context);

    CoolObject applyLt(Lt lt, Context context);

    CoolObject applyLtEq(LtEq ltEq, Context context);

    CoolObject applyComp(Comp comp, Context context);

    CoolObject applyNot(Not not, Context context);

    CoolObject applyIdConst(IdConst idConst, Context context);

    CoolObject applyStringConst(StringConst stringConst, Context context);

    CoolObject applyBoolConst(BoolConst boolConst, Context context);

    CoolObject applyIntConst(IntConst intConst, Context context);

    CoolObject applyParen(Paren paren, Context context);

    CoolObject applyNoExpression(NoExpression expr, Context context);

    CoolObject applyDispatch(Dispatch dispatch, Context context);

    CoolObject applyStaticDispatchBody(StaticDispatchBody staticDispatchBody, Context context);

    CoolObject applyStaticDispatch(StaticDispatch staticDispatch, Context context);

    CoolObject applyCond(Cond cond, Context context);

    CoolObject applyLoop(Loop loop, Context context);

    CoolObject applyLet(Let let, Context context);

    CoolObject applyCaseDef(CaseDef caseDef, Context context);

    CoolObject applyBranch(Branch branch, Context context);

    CoolObject applyLetAttrDef(LetAttrDef letAttrDef, Context context);
}
