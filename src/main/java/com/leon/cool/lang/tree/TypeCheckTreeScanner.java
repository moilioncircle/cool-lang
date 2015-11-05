package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.*;
import com.leon.cool.lang.factory.TypeFactory;
import com.leon.cool.lang.support.MethodDeclaration;
import com.leon.cool.lang.support._;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.type.Type;
import com.leon.cool.lang.type.TypeEnum;
import com.leon.cool.lang.util.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by leon on 15-10-15.
 */
public class TypeCheckTreeScanner extends TreeScanner {
    private TypeFactory t = new TypeFactory();
    private String className = null;
    public List<String> errMsgs = new ArrayList<>();

    @Override
    public void applyProgram(Program program) {
        super.applyProgram(program);
    }

    public void applyClassDef(ClassDef classDef) {
        className = classDef.type.name;
        super.applyClassDef(classDef);
        _.lookupSymbolTable(className).exitScope();
    }

    @Override
    public void applyFormal(Formal formal) {
        super.applyFormal(formal);
    }

    @Override
    public void applyStaticDispatch(StaticDispatch staticDispatch) {
        super.applyStaticDispatch(staticDispatch);
        List<Type> paramsTypes = staticDispatch.dispatch.params.stream().map(e -> e.typeInfo).collect(Collectors.toList());
        if (!staticDispatch.type.isPresent()) {
            Optional<MethodDeclaration> methodDeclaration = _.lookupMethodDeclaration(staticDispatch.expr.typeInfo.replace().className(), staticDispatch.dispatch.id.name, paramsTypes);
            if (!methodDeclaration.isPresent()) {
                String method = constructMethod(staticDispatch.dispatch.id.name, paramsTypes.stream().map(e -> e.toString()).collect(Collectors.toList()));
                reportTypeCheckError("class:" + staticDispatch.expr.typeInfo.replace().className() + " Method not define error. method:" + method + _.errorPos(staticDispatch.dispatch.id));
                staticDispatch.typeInfo = t.noType();
            } else {
                if (_.isSelfType(methodDeclaration.get().returnType)) {
                    staticDispatch.typeInfo = staticDispatch.expr.typeInfo;
                } else {
                    staticDispatch.typeInfo = t.objectType(methodDeclaration.get().returnType, className);
                }
            }
        } else {
            Type type = t.objectType(staticDispatch.type.get().name, className);
            if (!_.isParent(staticDispatch.expr.typeInfo, type)) {
                reportTypeCheckError("type check error. expr type " + staticDispatch.expr.typeInfo + " is not subclass of " + type + _.errorPos(staticDispatch.type.get()));
                staticDispatch.typeInfo = t.noType();
            } else {
                Optional<MethodDeclaration> methodDeclaration = _.lookupMethodDeclaration(type.className(), staticDispatch.dispatch.id.name, paramsTypes);
                if (!methodDeclaration.isPresent()) {
                    String method = constructMethod(staticDispatch.dispatch.id.name, paramsTypes.stream().map(e -> e.toString()).collect(Collectors.toList()));
                    reportTypeCheckError("class:" + type.className() + " Method not define error. method:" + method + _.errorPos(staticDispatch.dispatch.id));
                    staticDispatch.typeInfo = t.noType();
                } else {
                    if (_.isSelfType(methodDeclaration.get().returnType)) {
                        staticDispatch.typeInfo = staticDispatch.expr.typeInfo;
                    } else {
                        staticDispatch.typeInfo = t.objectType(methodDeclaration.get().returnType, className);
                    }
                }
            }
        }
    }

    @Override
    public void applyStaticDispatchBody(StaticDispatchBody staticDispatchBody) {
        super.applyStaticDispatchBody(staticDispatchBody);
    }

    @Override
    public void applyDispatch(Dispatch dispatch) {
        super.applyDispatch(dispatch);
        List<Type> paramsTypes = dispatch.params.stream().map(e -> e.typeInfo).collect(Collectors.toList());
        Optional<MethodDeclaration> methodDeclaration = _.lookupMethodDeclaration(className, dispatch.id.name, paramsTypes);
        if (!methodDeclaration.isPresent()) {
            String method = constructMethod(dispatch.id.name, paramsTypes.stream().map(e -> e.toString()).collect(Collectors.toList()));
            reportTypeCheckError("class:" + className + " Method not define error. method:" + method + _.errorPos(dispatch.id));
            dispatch.typeInfo = t.noType();
        } else {
            dispatch.typeInfo = t.objectType(methodDeclaration.get().returnType, className);
        }
    }

    public void applyCaseDef(CaseDef caseDef) {
        int size = caseDef.branchList.stream().map(e -> {
            if (_.isSelf(e.id)) {
                reportTypeCheckError("case expression can allowed to bind name 'self'" + _.errorPos(e.id));
            }
            return e.type.name;
        }).collect(Collectors.toSet()).size();
        if (caseDef.branchList.size() != size) {
            reportTypeCheckError("each branch of a case must all have distinct types" + _.errorPos(caseDef));
            caseDef.typeInfo = t.noType();
            super.applyCaseDef(caseDef);
            return;
        } else {
            super.applyCaseDef(caseDef);
            caseDef.typeInfo = _.lub(caseDef.branchList.stream().map(e -> e.typeInfo).collect(Collectors.toList()));
        }
    }

    @Override
    public void applyBranch(Branch branch) {
        _.lookupSymbolTable(className).enterScope();
        _.lookupSymbolTable(className).addId(branch.id.name, branch.type.name);
        super.applyBranch(branch);
        branch.typeInfo = branch.expr.typeInfo;
        _.lookupSymbolTable(className).exitScope();
    }

    public void applyMethodDef(MethodDef methodDef) {
        _.lookupSymbolTable(className).enterScope();
        methodDef.formals.forEach(e -> {
            if (_.isSelf(e.id)) {
                reportTypeCheckError("parameter name 'self' not allowed" + _.errorPos(e.id));
            } else {
                _.lookupSymbolTable(className).addId(e.id.name, e.type.name);
            }
        });
        super.applyMethodDef(methodDef);
        if (!_.isParent(methodDef.expr.typeInfo, t.objectType(methodDef.type.name, className))) {
            reportTypeCheckError("type check error. return expr type " + methodDef.expr.typeInfo + " is not subclass of " + t.objectType(methodDef.type.name, className) + _.errorPos(methodDef.type));
        }
        _.lookupSymbolTable(className).exitScope();
    }

    @Override
    public void applyAttrDef(AttrDef attrDef) {
        super.applyAttrDef(attrDef);
        if (attrDef.expr.isPresent()) {
            Type t0 = t.objectType((String) _.lookupSymbolTable(className).lookup(attrDef.id.name).get(), className);
            Type t1 = attrDef.expr.get().typeInfo;
            if (!_.isParent(t1, t0)) {
                reportTypeCheckError("type check error. assign expr type " + t1 + " is not subclass of " + t0 + _.errorPos(attrDef));
            }
        }
    }

    public void applyLet(Let let) {
        _.lookupSymbolTable(className).enterScope();
        super.applyLet(let);
        let.typeInfo = let.expr.typeInfo;
        _.lookupSymbolTable(className).exitScope();
    }

    @Override
    public void applyLetAttrDef(LetAttrDef letAttrDef) {
        super.applyLetAttrDef(letAttrDef);
        if (_.isSelf(letAttrDef.id)) {
            reportTypeCheckError("let expression can not allowed to bind name 'self'" + _.errorPos(letAttrDef.id));
        }
        if (letAttrDef.expr.isPresent()) {
            Type t0 = t.objectType(letAttrDef.type.name, className);
            if (!_.isParent(letAttrDef.expr.get().typeInfo, t0)) {
                reportTypeCheckError("type check error. assign expr type " + letAttrDef.expr.get().typeInfo + " is not subclass of " + t0 + _.errorPos(letAttrDef));
            }
        }
        _.lookupSymbolTable(className).addId(letAttrDef.id.name, letAttrDef.type.name);
    }

    public void applyAssign(Assign assign) {
        if (_.isSelf(assign.id.tok)) {
            reportTypeCheckError("not allowed to assign to 'self'" + _.errorPos(assign.id));
        }
        super.applyAssign(assign);
        if (_.isParent(assign.expr.typeInfo, assign.id.typeInfo)) {
            assign.typeInfo = assign.expr.typeInfo;
        } else {
            reportTypeCheckError("type check error. type " + assign.expr.typeInfo + " is not subclass of " + assign.id.typeInfo + _.errorPos(assign));
            assign.typeInfo = t.noType();
        }
    }

    @Override
    public void applyCond(Cond cond) {
        super.applyCond(cond);
        if (cond.condExpr.typeInfo.type() != TypeEnum.BOOL) {
            reportTypeCheckError("type check error. condExpr Expected 'Bool' but " + cond.condExpr.typeInfo + _.errorPos(cond.condExpr));
        }
        cond.typeInfo = _.lub(Arrays.asList(cond.thenExpr.typeInfo, cond.elseExpr.typeInfo));
    }

    @Override
    public void applyLoop(Loop loop) {
        super.applyLoop(loop);
        if (loop.condExpr.typeInfo.type() != TypeEnum.BOOL) {
            reportTypeCheckError("type check error. condExpr Expected 'Bool' but " + loop.condExpr.typeInfo + _.errorPos(loop.condExpr));
        }
        loop.typeInfo = t.objectType(Constant.OBJECT);
    }

    @Override
    public void applyBlocks(Blocks blocks) {
        super.applyBlocks(blocks);
        blocks.typeInfo = blocks.exprs.get(blocks.exprs.size() - 1).typeInfo;
    }

    @Override
    public void applyNewDef(NewDef newDef) {
        super.applyNewDef(newDef);
        newDef.typeInfo = t.objectType(newDef.type.name, className);
    }

    @Override
    public void applyIsVoid(IsVoid isVoid) {
        super.applyIsVoid(isVoid);
        isVoid.typeInfo = t.booleanType();
    }

    @Override
    public void applyPlus(Plus plus) {
        super.applyPlus(plus);
        if (plus.left.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type check error. Expected 'Int' but " + plus.left.typeInfo + _.errorPos(plus.left));
            plus.typeInfo = t.noType();
            return;
        }
        if (plus.right.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type check error. Expected 'Int' but " + plus.right.typeInfo + _.errorPos(plus.right));
            plus.typeInfo = t.noType();
            return;
        }
        plus.typeInfo = t.integerType();
    }

    @Override
    public void applySub(Sub sub) {
        super.applySub(sub);
        if (sub.left.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type check error. Expected 'Int' but " + sub.left.typeInfo + _.errorPos(sub.left));
            sub.typeInfo = t.noType();
            return;
        }
        if (sub.right.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type check error. Expected 'Int' but " + sub.right.typeInfo + _.errorPos(sub.right));
            sub.typeInfo = t.noType();
            return;
        }
        sub.typeInfo = t.integerType();
    }

    @Override
    public void applyMul(Mul mul) {
        super.applyMul(mul);
        if (mul.left.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type check error. Expected 'Int' but " + mul.left.typeInfo + _.errorPos(mul.left));
            mul.typeInfo = t.noType();
            return;
        }
        if (mul.right.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type check error. Expected 'Int' but " + mul.right.typeInfo + _.errorPos(mul.right));
            mul.typeInfo = t.noType();
            return;
        }
        mul.typeInfo = t.integerType();
    }

    @Override
    public void applyDivide(Divide divide) {
        super.applyDivide(divide);
        if (divide.left.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type check error. Expected 'Int' but " + divide.left.typeInfo + _.errorPos(divide.left));
            divide.typeInfo = t.noType();
            return;
        }
        if (divide.right.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type check error. Expected 'Int' but " + divide.right.typeInfo + _.errorPos(divide.right));
            divide.typeInfo = t.noType();
            return;
        }
        divide.typeInfo = t.integerType();
    }

    @Override
    public void applyNeg(Neg neg) {
        super.applyNeg(neg);
        if (neg.expr.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type check error. Expected 'Int' but " + neg.expr.typeInfo + _.errorPos(neg.expr));
            neg.typeInfo = t.noType();
        } else {
            neg.typeInfo = neg.expr.typeInfo;
        }
    }

    @Override
    public void applyLt(Lt lt) {
        super.applyLt(lt);
        if (lt.left.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type check error. Expected 'Int' but " + lt.left.typeInfo + _.errorPos(lt.left));
            lt.typeInfo = t.noType();
            return;
        }
        if (lt.right.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type check error. Expected 'Int' but " + lt.right.typeInfo + _.errorPos(lt.right));
            lt.typeInfo = t.noType();
            return;
        }
        lt.typeInfo = t.booleanType();
    }

    @Override
    public void applyLtEq(LtEq ltEq) {
        super.applyLtEq(ltEq);
        if (ltEq.left.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type check error. Expected 'Int' but " + ltEq.left.typeInfo + _.errorPos(ltEq.left));
            ltEq.typeInfo = t.noType();
            return;
        }
        if (ltEq.right.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type check error. Expected 'Int' but " + ltEq.right.typeInfo + _.errorPos(ltEq.right));
            ltEq.typeInfo = t.noType();
            return;
        }
        ltEq.typeInfo = t.booleanType();
    }

    @Override
    public void applyComp(Comp comp) {
        super.applyComp(comp);
        if (_.isBasicType(comp.left.typeInfo) && _.isBasicType(comp.right.typeInfo) && comp.left.typeInfo.type() == comp.right.typeInfo.type()) {
            comp.typeInfo = t.booleanType();
        } else if (!_.isBasicType(comp.left.typeInfo) && !_.isBasicType(comp.right.typeInfo)) {
            comp.typeInfo = t.booleanType();
        } else {
            reportTypeCheckError("type check error. type " + comp.left.typeInfo + " is not same as " + comp.right.typeInfo + _.errorPos(comp));
            comp.typeInfo = t.noType();
        }
    }

    @Override
    public void applyNot(Not not) {
        super.applyNot(not);
        if (not.expr.typeInfo.type() != TypeEnum.BOOL) {
            reportTypeCheckError("type check error. Expected 'Bool' but " + not.expr.typeInfo + _.errorPos(not.expr));
            not.typeInfo = t.noType();
        } else {
            not.typeInfo = t.booleanType();
        }

    }

    public void applyIdConst(IdConst idConst) {
        Optional<Object> type = _.lookupSymbolTable(className).lookup(idConst.tok.name);
        if (type.isPresent()) {
            String typeStr = (String) type.get();
            if (!_.isTypeDefined(typeStr)) {
                reportTypeCheckError("type not defined,type:" + typeStr + _.errorPos(idConst));
                idConst.typeInfo = t.noType();
            } else {
                idConst.typeInfo = t.objectType(typeStr, className);
            }
        } else {
            reportTypeCheckError("id not defined in class " + className + ",id:" + idConst.tok.name + _.errorPos(idConst));
            idConst.typeInfo = t.noType();
        }
    }

    @Override
    public void applyStringConst(StringConst stringConst) {
        super.applyStringConst(stringConst);
        stringConst.typeInfo = t.stringType();
    }

    @Override
    public void applyBoolConst(BoolConst boolConst) {
        super.applyBoolConst(boolConst);
        boolConst.typeInfo = t.booleanType();
    }

    @Override
    public void applyIntConst(IntConst intConst) {
        super.applyIntConst(intConst);
        intConst.typeInfo = t.integerType();
    }

    @Override
    public void applyToken(Token token) {
        super.applyToken(token);
    }

    @Override
    public void applyParen(Paren paren) {
        super.applyParen(paren);
        paren.typeInfo = paren.expr.typeInfo;
    }

    @Override
    public void applyNoExpression(NoExpression expr) {
        super.applyNoExpression(expr);
        expr.typeInfo = t.noType();
    }

    private void reportTypeCheckError(String message) {
//        System.err.println(message);
        errMsgs.add(message);
    }

    private String constructMethod(String id, List<String> params) {
        return id + _.mkString(params, Optional.of("("), ",", Optional.of(")"));
    }
}
