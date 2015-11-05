package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.*;
import com.leon.cool.lang.tokenizer.Token;

/**
 * Created by leon on 15-10-14.
 */
public interface TreeVisitor {
    public abstract void applyProgram(Program program);

    public abstract void applyClassDef(ClassDef classDef);

    public abstract void applyMethodDef(MethodDef methodDef);

    public abstract void applyAttrDef(AttrDef attrDef);

    public abstract void applyLetAttrDef(LetAttrDef letAttrDef);

    public abstract void applyFormal(Formal formal);

    public abstract void applyAssign(Assign assign);

    public abstract void applyDispatch(Dispatch dispatch);

    public abstract void applyStaticDispatchBody(StaticDispatchBody staticDispatchBody);

    public abstract void applyStaticDispatch(StaticDispatch staticDispatch);

    public abstract void applyCond(Cond cond);

    public abstract void applyLoop(Loop loop);

    public abstract void applyBlocks(Blocks blocks);

    public abstract void applyLet(Let let);

    public abstract void applyCaseDef(CaseDef caseDef);

    public abstract void applyBranch(Branch branch);

    public abstract void applyNewDef(NewDef newDef);

    public abstract void applyIsVoid(IsVoid isVoid);

    public abstract void applyPlus(Plus plus);

    public abstract void applySub(Sub sub);

    public abstract void applyMul(Mul mul);

    public abstract void applyDivide(Divide divide);

    public abstract void applyNeg(Neg neg);

    public abstract void applyLt(Lt lt);

    public abstract void applyLtEq(LtEq ltEq);

    public abstract void applyComp(Comp comp);

    public abstract void applyNot(Not not);

    public abstract void applyIdConst(IdConst idConst);

    public abstract void applyStringConst(StringConst stringConst);

    public abstract void applyBoolConst(BoolConst boolConst);

    public abstract void applyIntConst(IntConst intConst);

    public abstract void applyToken(Token token);

    public abstract void applyParen(Paren paren);

    public abstract void applyNoExpression(NoExpression expr);

}
