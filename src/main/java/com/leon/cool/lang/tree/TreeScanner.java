package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.*;
import com.leon.cool.lang.tokenizer.Token;

import java.util.List;
import java.util.Optional;

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
 * @author leon on 15-10-14
 */
public class TreeScanner implements TreeVisitor {
    public TreeScanner() {
    }

    protected <T extends TreeNode> void scan(T node) {
        node.accept(this);
    }

    protected <T extends TreeNode> void scan(List<T> nodes) {
        for (TreeNode node : nodes) {
            node.accept(this);
        }
    }

    protected <T extends TreeNode> void scan(Optional<T> nodeOpt) {
        if (nodeOpt.isPresent()) {
            nodeOpt.get().accept(this);
        }
    }

    @Override
    public void applyProgram(Program program) {
        scan(program.classDef);
    }

    @Override
    public void applyClassDef(ClassDef classDef) {
        scan(classDef.type);
        scan(classDef.inheritsType);
        scan(classDef.features);
    }


    @Override
    public void applyMethodDef(MethodDef methodDef) {
        scan(methodDef.id);
        scan(methodDef.formals);
        scan(methodDef.type);
        scan(methodDef.expr);
    }

    @Override
    public void applyAttrDef(AttrDef attrDef) {
        scan(attrDef.id);
        scan(attrDef.type);
        scan(attrDef.expr);
    }

    @Override
    public void applyLetAttrDef(LetAttrDef letAttrDef) {
        scan(letAttrDef.id);
        scan(letAttrDef.type);
        scan(letAttrDef.expr);
    }

    @Override
    public void applyFormal(Formal formal) {
        scan(formal.id);
        scan(formal.type);
    }

    @Override
    public void applyAssign(Assign assign) {
        scan(assign.id);
        scan(assign.expr);
    }

    @Override
    public void applyDispatch(Dispatch dispatch) {
        scan(dispatch.id);
        scan(dispatch.params);
    }

    @Override
    public void applyStaticDispatchBody(StaticDispatchBody staticDispatchBody) {
        scan(staticDispatchBody.id);
        scan(staticDispatchBody.params);
    }

    @Override
    public void applyStaticDispatch(StaticDispatch staticDispatch) {
        scan(staticDispatch.expr);
        scan(staticDispatch.type);
        scan(staticDispatch.dispatch);
    }

    @Override
    public void applyCond(Cond cond) {
        scan(cond.condExpr);
        scan(cond.thenExpr);
        scan(cond.elseExpr);
    }

    @Override
    public void applyLoop(Loop loop) {
        scan(loop.condExpr);
        scan(loop.loopExpr);
    }

    @Override
    public void applyBlocks(Blocks blocks) {
        scan(blocks.exprs);
    }

    @Override
    public void applyLet(Let let) {
        scan(let.attrDefs);
        scan(let.expr);
    }

    @Override
    public void applyCaseDef(CaseDef caseDef) {
        scan(caseDef.caseExpr);
        scan(caseDef.branchList);
    }

    @Override
    public void applyBranch(Branch branch) {
        scan(branch.id);
        scan(branch.type);
        scan(branch.expr);
    }

    @Override
    public void applyNewDef(NewDef newDef) {
        scan(newDef.type);
    }

    @Override
    public void applyIsVoid(IsVoid isVoid) {
        scan(isVoid.expr);
    }

    @Override
    public void applyPlus(Plus plus) {
        scan(plus.left);
        scan(plus.right);
    }

    @Override
    public void applySub(Sub sub) {
        scan(sub.left);
        scan(sub.right);
    }

    @Override
    public void applyMul(Mul mul) {
        scan(mul.left);
        scan(mul.right);
    }

    @Override
    public void applyDivide(Divide divide) {
        scan(divide.left);
        scan(divide.right);
    }

    @Override
    public void applyNeg(Neg neg) {
        scan(neg.expr);
    }

    @Override
    public void applyLt(Lt lt) {
        scan(lt.left);
        scan(lt.right);
    }

    @Override
    public void applyLtEq(LtEq ltEq) {
        scan(ltEq.left);
        scan(ltEq.right);
    }

    @Override
    public void applyComp(Comp comp) {
        scan(comp.left);
        scan(comp.right);
    }

    @Override
    public void applyNot(Not not) {
        scan(not.expr);
    }

    @Override
    public void applyIdConst(IdConst idConst) {
        scan(idConst.tok);
    }

    @Override
    public void applyStringConst(StringConst stringConst) {
        scan(stringConst.tok);
    }

    @Override
    public void applyBoolConst(BoolConst boolConst) {
    }

    @Override
    public void applyIntConst(IntConst intConst) {
        scan(intConst.tok);
    }

    @Override
    public void applyToken(Token token) {
    }

    @Override
    public void applyParen(Paren paren) {
        scan(paren.expr);
    }

    @Override
    public void applyNoExpression(NoExpression expr) {

    }
}
