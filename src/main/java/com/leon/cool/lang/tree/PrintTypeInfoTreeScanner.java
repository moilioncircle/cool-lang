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
 * @author leon on 15-10-17
 */
public class PrintTypeInfoTreeScanner extends TreeScanner {
    @Override
    public void applyProgram(Program program) {
        scan(program.classDef);
        System.out.println("program = [" + program.typeInfo + "]");
        System.out.println("program = [" + program.starPos + "," + program.endPos + "]");
    }

    @Override
    public void applyClassDef(ClassDef classDef) {
        super.applyClassDef(classDef);
        System.out.println("classDef = [" + classDef.typeInfo + "]");
        System.out.println("classDef = [" + classDef.starPos + "," + classDef.endPos + "]");
    }


    @Override
    public void applyMethodDef(MethodDef methodDef) {
        super.applyMethodDef(methodDef);
        System.out.println("methodDef = [" + methodDef.typeInfo + "]");
        System.out.println("methodDef = [" + methodDef.starPos + "," + methodDef.endPos + "]");
    }

    @Override
    public void applyAttrDef(AttrDef attrDef) {
        super.applyAttrDef(attrDef);
        System.out.println("attrDef = [" + attrDef.typeInfo + "]");
        System.out.println("attrDef = [" + attrDef.starPos + "," + attrDef.endPos + "]");
    }

    @Override
    public void applyLetAttrDef(LetAttrDef letAttrDef) {
        super.applyLetAttrDef(letAttrDef);
        System.out.println("letAttrDef = [" + letAttrDef.typeInfo + "]");
        System.out.println("letAttrDef = [" + letAttrDef.starPos + "," + letAttrDef.endPos + "]");
    }

    @Override
    public void applyFormal(Formal formal) {
        super.applyFormal(formal);
        System.out.println("formal = [" + formal.typeInfo + "]");
        System.out.println("formal = [" + formal.starPos + "," + formal.endPos + "]");
    }

    @Override
    public void applyAssign(Assign assign) {
        super.applyAssign(assign);
        System.out.println("assign = [" + assign.typeInfo + "]");
        System.out.println("assign = [" + assign.starPos + "," + assign.endPos + "]");
    }

    @Override
    public void applyDispatch(Dispatch dispatch) {
        super.applyDispatch(dispatch);
        System.out.println("dispatch = [" + dispatch.typeInfo + "]");
        System.out.println("dispatch = [" + dispatch.starPos + "," + dispatch.endPos + "]");
    }

    @Override
    public void applyStaticDispatchBody(StaticDispatchBody staticDispatchBody) {
        super.applyStaticDispatchBody(staticDispatchBody);
        System.out.println("staticDispatchBody = [" + staticDispatchBody.typeInfo + "]");
        System.out.println("staticDispatchBody = [" + staticDispatchBody.starPos + "," + staticDispatchBody.endPos + "]");
    }

    @Override
    public void applyStaticDispatch(StaticDispatch staticDispatch) {
        super.applyStaticDispatch(staticDispatch);
        System.out.println("staticDispatch = [" + staticDispatch.typeInfo + "]");
        System.out.println("staticDispatch = [" + staticDispatch.starPos + "," + staticDispatch.endPos + "]");
    }

    @Override
    public void applyCond(Cond cond) {
        super.applyCond(cond);
        System.out.println("cond = [" + cond.typeInfo + "]");
        System.out.println("cond = [" + cond.starPos + "," + cond.endPos + "]");
    }

    @Override
    public void applyLoop(Loop loop) {
        super.applyLoop(loop);
        System.out.println("loop = [" + loop.typeInfo + "]");
        System.out.println("loop = [" + loop.starPos + "," + loop.endPos + "]");
    }

    @Override
    public void applyBlocks(Blocks blocks) {
        super.applyBlocks(blocks);
        System.out.println("blocks = [" + blocks.typeInfo + "]");
        System.out.println("blocks = [" + blocks.starPos + "," + blocks.endPos + "]");
    }

    @Override
    public void applyLet(Let let) {
        super.applyLet(let);
        System.out.println("let = [" + let.typeInfo + "]");
        System.out.println("let = [" + let.starPos + "," + let.endPos + "]");
    }

    @Override
    public void applyCaseDef(CaseDef caseDef) {
        super.applyCaseDef(caseDef);
        System.out.println("caseDef = [" + caseDef.typeInfo + "]");
        System.out.println("caseDef = [" + caseDef.starPos + "," + caseDef.endPos + "]");
    }

    @Override
    public void applyBranch(Branch branch) {
        super.applyBranch(branch);
        System.out.println("branch = [" + branch.typeInfo + "]");
        System.out.println("branch = [" + branch.starPos + "," + branch.endPos + "]");
    }

    @Override
    public void applyNewDef(NewDef newDef) {
        super.applyNewDef(newDef);
        System.out.println("newDef = [" + newDef.typeInfo + "]");
        System.out.println("newDef = [" + newDef.starPos + "," + newDef.endPos + "]");
    }

    @Override
    public void applyIsVoid(IsVoid isVoid) {
        super.applyIsVoid(isVoid);
        System.out.println("isVoid = [" + isVoid.typeInfo + "]");
        System.out.println("isVoid = [" + isVoid.starPos + "," + isVoid.endPos + "]");
    }

    @Override
    public void applyPlus(Plus plus) {
        super.applyPlus(plus);
        System.out.println("plus = [" + plus.typeInfo + "]");
        System.out.println("plus = [" + plus.starPos + "," + plus.endPos + "]");
    }

    @Override
    public void applySub(Sub sub) {
        super.applySub(sub);
        System.out.println("sub = [" + sub.typeInfo + "]");
        System.out.println("sub = [" + sub.starPos + "," + sub.endPos + "]");
    }

    @Override
    public void applyMul(Mul mul) {
        super.applyMul(mul);
        System.out.println("mul = [" + mul.typeInfo + "]");
        System.out.println("mul = [" + mul.starPos + "," + mul.endPos + "]");
    }

    @Override
    public void applyDivide(Divide divide) {
        super.applyDivide(divide);
        System.out.println("divide = [" + divide.typeInfo + "]");
        System.out.println("divide = [" + divide.starPos + "," + divide.endPos + "]");
    }

    @Override
    public void applyNeg(Neg neg) {
        super.applyNeg(neg);
        System.out.println("neg = [" + neg.typeInfo + "]");
        System.out.println("neg = [" + neg.starPos + "," + neg.endPos + "]");
    }

    @Override
    public void applyLt(Lt lt) {
        super.applyLt(lt);
        System.out.println("lt = [" + lt.typeInfo + "]");
        System.out.println("lt = [" + lt.starPos + "," + lt.endPos + "]");
    }

    @Override
    public void applyLtEq(LtEq ltEq) {
        super.applyLtEq(ltEq);
        System.out.println("ltEq = [" + ltEq.typeInfo + "]");
        System.out.println("ltEq = [" + ltEq.starPos + "," + ltEq.endPos + "]");
    }

    @Override
    public void applyComp(Comp comp) {
        super.applyComp(comp);
        System.out.println("comp = [" + comp.typeInfo + "]");
        System.out.println("comp = [" + comp.starPos + "," + comp.endPos + "]");
    }

    @Override
    public void applyNot(Not not) {
        super.applyNot(not);
        System.out.println("not = [" + not.typeInfo + "]");
        System.out.println("not = [" + not.starPos + "," + not.endPos + "]");
    }

    @Override
    public void applyIdConst(IdConst idConst) {
        super.applyIdConst(idConst);
        System.out.println("idConst = [" + idConst.typeInfo + "]");
        System.out.println("idConst = [" + idConst.starPos + "," + idConst.endPos + "]");
    }

    @Override
    public void applyStringConst(StringConst stringConst) {
        super.applyStringConst(stringConst);
        System.out.println("stringConst = [" + stringConst.typeInfo + "]");
        System.out.println("stringConst = [" + stringConst.starPos + "," + stringConst.endPos + "]");
    }

    @Override
    public void applyBoolConst(BoolConst boolConst) {
        super.applyBoolConst(boolConst);
        System.out.println("boolConst = [" + boolConst.typeInfo + "]");
        System.out.println("boolConst = [" + boolConst.starPos + "," + boolConst.endPos + "]");
    }

    @Override
    public void applyIntConst(IntConst intConst) {
        super.applyIntConst(intConst);
        System.out.println("intConst = [" + intConst.typeInfo + "]");
        System.out.println("intConst = [" + intConst.starPos + "," + intConst.endPos + "]");
    }

    @Override
    public void applyToken(Token token) {
        System.out.println("token = [" + token.startPos + "," + token.endPos + "]");
    }

    @Override
    public void applyParen(Paren paren) {
        super.applyParen(paren);
        System.out.println("paren = [" + paren.typeInfo + "]");
        System.out.println("paren = [" + paren.starPos + "," + paren.endPos + "]");
    }

    @Override
    public void applyNoExpression(NoExpression expr) {
        super.applyNoExpression(expr);
        System.out.println("NoExpr = [" + expr.typeInfo + "]");
        System.out.println("NoExpr = [" + expr.starPos + "," + expr.endPos + "]");
    }
}
