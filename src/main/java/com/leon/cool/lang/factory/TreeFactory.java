package com.leon.cool.lang.factory;

import com.leon.cool.lang.ast.*;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.util.Pos;

import java.util.List;
import java.util.Optional;

/**
 * Created by leon on 15-10-8.
 */
public class TreeFactory {
    private Pos startPos;
    private Pos endPos;

    public TreeFactory at(Pos startPos, Pos endPos) {
        this.startPos = startPos;
        this.endPos = endPos;
        return this;
    }

    private <T> T setPos(TreeNode node) {
        node.starPos = this.startPos;
        node.endPos = this.endPos;
        return (T) node;
    }

    public Assign assign(IdConst id, Expression expr) {
        return setPos(new Assign(id, expr));
    }

    public AttrDef attrDef(Token id, Token type, Optional<Expression> expr) {
        return setPos(new AttrDef(id, type, expr));
    }

    public LetAttrDef letAttrDef(Token id, Token type, Optional<Expression> expr) {
        return setPos(new LetAttrDef(id, type, expr));
    }

    public Blocks blocks(List<Expression> exprs) {
        return setPos(new Blocks(exprs));
    }

    public BoolConst boolConst(Boolean bool) {
        return setPos(new BoolConst(bool));
    }

    public Branch branch(Token id, Token type, Expression expr) {
        return setPos(new Branch(id, type, expr));
    }

    public CaseDef caseDef(Expression caseExpr, List<Branch> branchList) {
        return setPos(new CaseDef(caseExpr, branchList));
    }

    public ClassDef classDef(Token type, Optional<Token> inheritsType, List<Feature> features) {
        return setPos(new ClassDef(type, inheritsType, features));
    }

    public Comp comp(Expression left, Expression right) {
        return setPos(new Comp(left, right));
    }

    public Cond cond(Expression condExpr, Expression thenExpr, Expression elseExpr) {
        return setPos(new Cond(condExpr, thenExpr, elseExpr));
    }

    public Dispatch dispatch(Token id, List<Expression> params) {
        return setPos(new Dispatch(id, params));
    }

    public StaticDispatchBody staticDispatchBody(Token id, List<Expression> params) {
        return setPos(new StaticDispatchBody(id, params));
    }

    public Divide divide(Expression left, Expression right) {
        return setPos(new Divide(left, right));
    }

    public Formal formal(Token id, Token type) {
        return setPos(new Formal(id, type));
    }

    public IdConst idConst(Token id) {
        return setPos(new IdConst(id));
    }

    public IntConst intConst(Token tok) {
        return setPos(new IntConst(tok));
    }

    public IsVoid isVoid(Expression expr) {
        return setPos(new IsVoid(expr));
    }

    public Let let(List<LetAttrDef> attrDefs, Expression expr) {
        return setPos(new Let(attrDefs, expr));
    }

    public Loop loop(Expression condExpr, Expression loopExpr) {
        return setPos(new Loop(condExpr, loopExpr));
    }

    public Lt lt(Expression left, Expression right) {
        return setPos(new Lt(left, right));
    }

    public LtEq ltEq(Expression left, Expression right) {
        return setPos(new LtEq(left, right));
    }

    public MethodDef methodDef(Token id, List<Formal> formals, Token type, Expression expr) {
        return setPos(new MethodDef(id, formals, type, expr));
    }

    public Mul mul(Expression left, Expression right) {
        return setPos(new Mul(left, right));
    }

    public Neg neg(Expression expr) {
        return setPos(new Neg(expr));
    }

    public NewDef newDef(Token type) {
        return setPos(new NewDef(type));
    }

    public Not not(Expression expr) {
        return setPos(new Not(expr));
    }

    public Plus plus(Expression left, Expression right) {
        return setPos(new Plus(left, right));
    }

    public Program program(List<ClassDef> classDef) {
        return setPos(new Program(classDef));
    }

    public StaticDispatch staticDispatch(Expression expr, Optional<Token> type, StaticDispatchBody dispatch) {
        return setPos(new StaticDispatch(expr, type, dispatch));
    }

    public StringConst stringConst(Token tok) {
        return setPos(new StringConst(tok));
    }

    public Sub sub(Expression left, Expression right) {
        return setPos(new Sub(left, right));
    }

    public Paren paren(Expression expr) {
        return setPos(new Paren(expr));
    }

    public NoExpression noExpression() {
        return setPos(new NoExpression());
    }

}
