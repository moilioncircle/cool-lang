package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.*;
import com.leon.cool.lang.factory.TypeFactory;
import com.leon.cool.lang.support.MethodDeclaration;
import com.leon.cool.lang.support.Utils;
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
 * @author leon on 15-10-15
 */
public class TypeCheckTreeScanner extends TreeScanner {
    private String className = null;
    public final List<String> errMsgs = new ArrayList<>();

    @Override
    public void applyProgram(Program program) {
        super.applyProgram(program);
    }

    public void applyClassDef(ClassDef classDef) {
        className = classDef.type.name;
        super.applyClassDef(classDef);
        Utils.lookupSymbolTable(className).exitScope();
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
            Optional<MethodDeclaration> methodDeclaration = Utils.lookupMethodDeclaration(staticDispatch.expr.typeInfo.replace().className(), staticDispatch.dispatch.id.name, paramsTypes);
            if (!methodDeclaration.isPresent()) {
                String method = Utils.constructMethod(staticDispatch.dispatch.id.name, paramsTypes.stream().map(Object::toString).collect(Collectors.toList()));
                reportTypeCheckError("type.error.method.undefined", staticDispatch.expr.typeInfo.replace().className(), method, Utils.errorPos(staticDispatch.dispatch.id));
                staticDispatch.typeInfo = TypeFactory.noType();
            } else {
                if (Utils.isSelfType(methodDeclaration.get().returnType)) {
                    staticDispatch.typeInfo = staticDispatch.expr.typeInfo;
                } else {
                    staticDispatch.typeInfo = TypeFactory.objectType(methodDeclaration.get().returnType, className);
                }
            }
        } else {
            Type type = TypeFactory.objectType(staticDispatch.type.get().name, className);
            if (!Utils.isParent(staticDispatch.expr.typeInfo, type)) {
                reportTypeCheckError("type.error.subclass", staticDispatch.expr.typeInfo.toString(), type.toString(), Utils.errorPos(staticDispatch.type.get()));
                staticDispatch.typeInfo = TypeFactory.noType();
            } else {
                Optional<MethodDeclaration> methodDeclaration = Utils.lookupMethodDeclaration(type.className(), staticDispatch.dispatch.id.name, paramsTypes);
                if (!methodDeclaration.isPresent()) {
                    String method = Utils.constructMethod(staticDispatch.dispatch.id.name, paramsTypes.stream().map(Object::toString).collect(Collectors.toList()));
                    reportTypeCheckError("type.error.method.undefined", type.className(), method, Utils.errorPos(staticDispatch.dispatch.id));
                    staticDispatch.typeInfo = TypeFactory.noType();
                } else {
                    if (Utils.isSelfType(methodDeclaration.get().returnType)) {
                        staticDispatch.typeInfo = staticDispatch.expr.typeInfo;
                    } else {
                        staticDispatch.typeInfo = TypeFactory.objectType(methodDeclaration.get().returnType, className);
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
        Optional<MethodDeclaration> methodDeclaration = Utils.lookupMethodDeclaration(className, dispatch.id.name, paramsTypes);
        if (!methodDeclaration.isPresent()) {
            String method = Utils.constructMethod(dispatch.id.name, paramsTypes.stream().map(Object::toString).collect(Collectors.toList()));
            reportTypeCheckError("type.error.method.undefined", className, method, Utils.errorPos(dispatch.id));
            dispatch.typeInfo = TypeFactory.noType();
        } else {
            dispatch.typeInfo = TypeFactory.objectType(methodDeclaration.get().returnType, className);
        }
    }

    public void applyCaseDef(CaseDef caseDef) {
        int size = caseDef.branchList.stream().map(e -> {
            if (Utils.isSelf(e.id)) {
                reportTypeCheckError("type.error.bind.self", Utils.errorPos(e.id));
            }
            return e.type.name;
        }).collect(Collectors.toSet()).size();
        if (caseDef.branchList.size() != size) {
            reportTypeCheckError("type.error.case.distinct", Utils.errorPos(caseDef));
            caseDef.typeInfo = TypeFactory.noType();
            super.applyCaseDef(caseDef);
        } else {
            super.applyCaseDef(caseDef);
            caseDef.typeInfo = Utils.lub(caseDef.branchList.stream().map(e -> e.typeInfo).collect(Collectors.toList()));
        }
    }

    @Override
    public void applyBranch(Branch branch) {
        Utils.lookupSymbolTable(className).enterScope();
        Utils.lookupSymbolTable(className).addId(branch.id.name, branch.type.name);
        super.applyBranch(branch);
        branch.typeInfo = branch.expr.typeInfo;
        Utils.lookupSymbolTable(className).exitScope();
    }

    public void applyMethodDef(MethodDef methodDef) {
        Utils.lookupSymbolTable(className).enterScope();
        methodDef.formals.forEach(e -> {
            if (Utils.isSelf(e.id)) {
                reportTypeCheckError("type.error.assign.self", Utils.errorPos(e.id));
            } else {
                Utils.lookupSymbolTable(className).addId(e.id.name, e.type.name);
            }
        });
        super.applyMethodDef(methodDef);
        if (!Utils.isParent(methodDef.expr.typeInfo, TypeFactory.objectType(methodDef.type.name, className))) {
            reportTypeCheckError("type.error.subclass", methodDef.expr.typeInfo.toString(), TypeFactory.objectType(methodDef.type.name, className).toString(), Utils.errorPos(methodDef.type));
        }
        Utils.lookupSymbolTable(className).exitScope();
    }

    @Override
    public void applyAttrDef(AttrDef attrDef) {
        super.applyAttrDef(attrDef);
        if (attrDef.expr.isPresent()) {
            Type t0 = TypeFactory.objectType(Utils.lookupSymbolTable(className).lookup(attrDef.id.name).get(), className);
            Type t1 = attrDef.expr.get().typeInfo;
            if (!Utils.isParent(t1, t0)) {
                reportTypeCheckError("type.error.subclass", t1.toString(), t0.toString(), Utils.errorPos(attrDef));
            }
        }
    }

    public void applyLet(Let let) {
        Utils.lookupSymbolTable(className).enterScope();
        super.applyLet(let);
        let.typeInfo = let.expr.typeInfo;
        Utils.lookupSymbolTable(className).exitScope();
    }

    @Override
    public void applyLetAttrDef(LetAttrDef letAttrDef) {
        super.applyLetAttrDef(letAttrDef);
        if (Utils.isSelf(letAttrDef.id)) {
            reportTypeCheckError("type.error.bind.self", Utils.errorPos(letAttrDef.id));
        }
        if (letAttrDef.expr.isPresent()) {
            Type t0 = TypeFactory.objectType(letAttrDef.type.name, className);
            if (!Utils.isParent(letAttrDef.expr.get().typeInfo, t0)) {
                reportTypeCheckError("type.error.subclass", letAttrDef.expr.get().typeInfo.toString(), t0.toString(), Utils.errorPos(letAttrDef));
            }
        }
        Utils.lookupSymbolTable(className).addId(letAttrDef.id.name, letAttrDef.type.name);
    }

    public void applyAssign(Assign assign) {
        if (Utils.isSelf(assign.id.tok)) {
            reportTypeCheckError("type.error.assign.self", Utils.errorPos(assign.id));
        }
        super.applyAssign(assign);
        if (Utils.isParent(assign.expr.typeInfo, assign.id.typeInfo)) {
            assign.typeInfo = assign.expr.typeInfo;
        } else {
            reportTypeCheckError("type.error.subclass", assign.expr.typeInfo.toString(), assign.id.typeInfo.toString(), Utils.errorPos(assign));
            assign.typeInfo = TypeFactory.noType();
        }
    }

    @Override
    public void applyCond(Cond cond) {
        super.applyCond(cond);
        if (cond.condExpr.typeInfo.type() != TypeEnum.BOOL) {
            reportTypeCheckError("type.error.expected", Constant.BOOL, cond.condExpr.typeInfo.toString(), Utils.errorPos(cond.condExpr));
        }
        cond.typeInfo = Utils.lub(Arrays.asList(cond.thenExpr.typeInfo, cond.elseExpr.typeInfo));
    }

    @Override
    public void applyLoop(Loop loop) {
        super.applyLoop(loop);
        if (loop.condExpr.typeInfo.type() != TypeEnum.BOOL) {
            reportTypeCheckError("type.error.expected", Constant.BOOL, loop.condExpr.typeInfo.toString(), Utils.errorPos(loop.condExpr));
        }
        loop.typeInfo = TypeFactory.objectType(Constant.OBJECT);
    }

    @Override
    public void applyBlocks(Blocks blocks) {
        super.applyBlocks(blocks);
        blocks.typeInfo = blocks.exprs.get(blocks.exprs.size() - 1).typeInfo;
    }

    @Override
    public void applyNewDef(NewDef newDef) {
        super.applyNewDef(newDef);
        newDef.typeInfo = TypeFactory.objectType(newDef.type.name, className);
    }

    @Override
    public void applyIsVoid(IsVoid isVoid) {
        super.applyIsVoid(isVoid);
        isVoid.typeInfo = TypeFactory.booleanType();
    }

    @Override
    public void applyPlus(Plus plus) {
        super.applyPlus(plus);
        if (plus.left.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type.error.expected", Constant.INT, plus.left.typeInfo.toString(), Utils.errorPos(plus.left));
            plus.typeInfo = TypeFactory.noType();
            return;
        }
        if (plus.right.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type.error.expected", Constant.INT, plus.right.typeInfo.toString(), Utils.errorPos(plus.right));
            plus.typeInfo = TypeFactory.noType();
            return;
        }
        plus.typeInfo = TypeFactory.integerType();
    }

    @Override
    public void applySub(Sub sub) {
        super.applySub(sub);
        if (sub.left.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type.error.expected", Constant.INT, sub.left.typeInfo.toString(), Utils.errorPos(sub.left));
            sub.typeInfo = TypeFactory.noType();
            return;
        }
        if (sub.right.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type.error.expected", Constant.INT, sub.right.typeInfo.toString(), Utils.errorPos(sub.right));
            sub.typeInfo = TypeFactory.noType();
            return;
        }
        sub.typeInfo = TypeFactory.integerType();
    }

    @Override
    public void applyMul(Mul mul) {
        super.applyMul(mul);
        if (mul.left.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type.error.expected", Constant.INT, mul.left.typeInfo.toString(), Utils.errorPos(mul.left));
            mul.typeInfo = TypeFactory.noType();
            return;
        }
        if (mul.right.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type.error.expected", Constant.INT, mul.right.typeInfo.toString(), Utils.errorPos(mul.right));
            mul.typeInfo = TypeFactory.noType();
            return;
        }
        mul.typeInfo = TypeFactory.integerType();
    }

    @Override
    public void applyDivide(Divide divide) {
        super.applyDivide(divide);
        if (divide.left.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type.error.expected", Constant.INT, divide.left.typeInfo.toString(), Utils.errorPos(divide.left));
            divide.typeInfo = TypeFactory.noType();
            return;
        }
        if (divide.right.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type.error.expected", Constant.INT, divide.right.typeInfo.toString(), Utils.errorPos(divide.right));
            divide.typeInfo = TypeFactory.noType();
            return;
        }
        divide.typeInfo = TypeFactory.integerType();
    }

    @Override
    public void applyNeg(Neg neg) {
        super.applyNeg(neg);
        if (neg.expr.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type.error.expected", Constant.INT, neg.expr.typeInfo.toString(), Utils.errorPos(neg.expr));
            neg.typeInfo = TypeFactory.noType();
        } else {
            neg.typeInfo = neg.expr.typeInfo;
        }
    }

    @Override
    public void applyLt(Lt lt) {
        super.applyLt(lt);
        if (lt.left.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type.error.expected", Constant.INT, lt.left.typeInfo.toString(), Utils.errorPos(lt.left));
            lt.typeInfo = TypeFactory.noType();
            return;
        }
        if (lt.right.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type.error.expected", Constant.INT, lt.right.typeInfo.toString(), Utils.errorPos(lt.right));
            lt.typeInfo = TypeFactory.noType();
            return;
        }
        lt.typeInfo = TypeFactory.booleanType();
    }

    @Override
    public void applyLtEq(LtEq ltEq) {
        super.applyLtEq(ltEq);
        if (ltEq.left.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type.error.expected", Constant.INT, ltEq.left.typeInfo.toString(), Utils.errorPos(ltEq.left));
            ltEq.typeInfo = TypeFactory.noType();
            return;
        }
        if (ltEq.right.typeInfo.type() != TypeEnum.INT) {
            reportTypeCheckError("type.error.expected", Constant.INT, ltEq.right.typeInfo.toString(), Utils.errorPos(ltEq.right));
            ltEq.typeInfo = TypeFactory.noType();
            return;
        }
        ltEq.typeInfo = TypeFactory.booleanType();
    }

    @Override
    public void applyComp(Comp comp) {
        super.applyComp(comp);
        if (Utils.isBasicType(comp.left.typeInfo) && Utils.isBasicType(comp.right.typeInfo) && comp.left.typeInfo.type() == comp.right.typeInfo.type()) {
            comp.typeInfo = TypeFactory.booleanType();
        } else if (!Utils.isBasicType(comp.left.typeInfo) && !Utils.isBasicType(comp.right.typeInfo)) {
            comp.typeInfo = TypeFactory.booleanType();
        } else {
            reportTypeCheckError("type.error.same", comp.left.typeInfo.toString(), comp.right.typeInfo.toString(), Utils.errorPos(comp));
            comp.typeInfo = TypeFactory.noType();
        }
    }

    @Override
    public void applyNot(Not not) {
        super.applyNot(not);
        if (not.expr.typeInfo.type() != TypeEnum.BOOL) {
            reportTypeCheckError("type.error.expected", Constant.BOOL, not.expr.typeInfo.toString(), Utils.errorPos(not.expr));
            not.typeInfo = TypeFactory.noType();
        } else {
            not.typeInfo = TypeFactory.booleanType();
        }

    }

    public void applyIdConst(IdConst idConst) {
        Optional<String> type = Utils.lookupSymbolTable(className).lookup(idConst.tok.name);
        if (type.isPresent()) {
            String typeStr = type.get();
            if (!Utils.isTypeDefined(typeStr)) {
                reportTypeCheckError("type.error.type.undefined", className, typeStr, Utils.errorPos(idConst));
                idConst.typeInfo = TypeFactory.noType();
            } else {
                idConst.typeInfo = TypeFactory.objectType(typeStr, className);
            }
        } else {
            reportTypeCheckError("type.error.id.undefined", className, idConst.tok.name, Utils.errorPos(idConst));
            idConst.typeInfo = TypeFactory.noType();
        }
    }

    @Override
    public void applyStringConst(StringConst stringConst) {
        super.applyStringConst(stringConst);
        stringConst.typeInfo = TypeFactory.stringType();
    }

    @Override
    public void applyBoolConst(BoolConst boolConst) {
        super.applyBoolConst(boolConst);
        boolConst.typeInfo = TypeFactory.booleanType();
    }

    @Override
    public void applyIntConst(IntConst intConst) {
        super.applyIntConst(intConst);
        intConst.typeInfo = TypeFactory.integerType();
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
        expr.typeInfo = TypeFactory.noType();
    }

    private void reportTypeCheckError(String key, String... params) {
        errMsgs.add(Utils.errorMsg(key, params));
    }
}
