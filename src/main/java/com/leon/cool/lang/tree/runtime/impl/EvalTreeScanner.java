package com.leon.cool.lang.tree.runtime.impl;

import com.leon.cool.lang.Constant;
import com.leon.cool.lang.ast.*;
import com.leon.cool.lang.glossary.Out;
import com.leon.cool.lang.object.CoolBool;
import com.leon.cool.lang.object.CoolInt;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.object.CoolString;
import com.leon.cool.lang.support.TreeSupport;
import com.leon.cool.lang.support.declaration.MethodDeclaration;
import com.leon.cool.lang.support.infrastructure.Context;
import com.leon.cool.lang.tree.runtime.EvalTreeVisitor;
import com.leon.cool.lang.type.Type;
import com.leon.cool.lang.type.TypeEnum;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.leon.cool.lang.factory.ObjectFactory.*;
import static com.leon.cool.lang.factory.TypeFactory.objectType;
import static com.leon.cool.lang.support.ErrorSupport.error;
import static com.leon.cool.lang.support.ErrorSupport.errorPos;
import static com.leon.cool.lang.support.TypeSupport.*;

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
public class EvalTreeScanner implements EvalTreeVisitor {

    protected TreeSupport treeSupport;

    public EvalTreeScanner(TreeSupport treeSupport) {
        this.treeSupport = treeSupport;
    }

    @Override
    public CoolObject applyAssign(Assign assign, @Out Context context) {
        CoolObject object = assign.expr.accept(this, context);
        context.environment.update(assign.id.tok.name, object);
        return object;
    }

    @Override
    public CoolObject applyBlocks(Blocks blocks, @Out Context context) {
        CoolObject object = coolVoid();
        for (Expression expr : blocks.exprs) {
            object = expr.accept(this, context);
        }
        return object;
    }

    @Override
    public CoolObject applyNewDef(NewDef newDef, @Out Context context) {
        Type type;
        if (isSelfType(newDef.type)) {
            type = context.selfObject.type;
        } else {
            type = objectType(newDef.type.name);
        }
        return treeSupport.newDef(this, type, context);
    }

    @Override
    public CoolObject applyIsVoid(IsVoid isVoid, @Out Context context) {
        CoolObject object = isVoid.expr.accept(this, context);
        return coolBool(object.type.type() == TypeEnum.VOID);
    }

    @Override
    public CoolObject applyPlus(Plus plus, @Out Context context) {
        CoolInt l = (CoolInt) plus.left.accept(this, context);
        CoolInt r = (CoolInt) plus.right.accept(this, context);
        return coolInt(l.val + r.val);
    }

    @Override
    public CoolObject applySub(Sub sub, @Out Context context) {
        CoolInt l = (CoolInt) sub.left.accept(this, context);
        CoolInt r = (CoolInt) sub.right.accept(this, context);
        return coolInt(l.val - r.val);
    }

    @Override
    public CoolObject applyMul(Mul mul, @Out Context context) {
        CoolInt l = (CoolInt) mul.left.accept(this, context);
        CoolInt r = (CoolInt) mul.right.accept(this, context);
        return coolInt(l.val * r.val);
    }

    @Override
    public CoolObject applyDivide(Divide divide, @Out Context context) {
        CoolInt l = (CoolInt) divide.left.accept(this, context);
        CoolInt r = (CoolInt) divide.right.accept(this, context);
        if (r.val == 0) {
            error("runtime.error.divide.zero", errorPos(divide.right));
        }
        return coolInt(l.val / r.val);
    }

    @Override
    public CoolObject applyNeg(Neg neg, @Out Context context) {
        CoolInt val = (CoolInt) neg.expr.accept(this, context);
        return coolInt(-val.val);
    }

    @Override
    public CoolObject applyLt(Lt lt, @Out Context context) {
        CoolInt l = (CoolInt) lt.left.accept(this, context);
        CoolInt r = (CoolInt) lt.right.accept(this, context);
        if (l.val < r.val) {
            return coolBool(true);
        } else {
            return coolBool(false);
        }
    }

    @Override
    public CoolObject applyLtEq(LtEq ltEq, @Out Context context) {
        CoolInt l = (CoolInt) ltEq.left.accept(this, context);
        CoolInt r = (CoolInt) ltEq.right.accept(this, context);
        if (l.val <= r.val) {
            return coolBool(true);
        } else {
            return coolBool(false);
        }
    }

    @Override
    public CoolObject applyComp(Comp comp, @Out Context context) {
        CoolObject l = comp.left.accept(this, context);
        CoolObject r = comp.right.accept(this, context);
        if (isBasicType(l.type) && isBasicType(r.type)) {
            if (l instanceof CoolString && r instanceof CoolString) {
                return coolBool(((CoolString) l).str.equals(((CoolString) r).str));
            } else if (l instanceof CoolInt && r instanceof CoolInt) {
                return coolBool(((CoolInt) l).val == ((CoolInt) r).val);
            } else if (l instanceof CoolBool && r instanceof CoolBool) {
                return coolBool(((CoolBool) l).val == ((CoolBool) r).val);
            } else {
                throw new AssertionError("unexpected error.");
            }
        } else {
            return coolBool(l.equals(r));
        }
    }

    @Override
    public CoolObject applyNot(Not not, @Out Context context) {
        CoolBool bool = (CoolBool) not.expr.accept(this, context);
        return coolBool(!bool.val);
    }

    @Override
    public CoolObject applyIdConst(IdConst idConst, @Out Context context) {
        if (idConst.tok.name.equals(Constant.SELF)) {
            return context.selfObject;
        } else {
            return context.environment.lookup(idConst.tok.name).get();
        }
    }

    @Override
    public CoolObject applyStringConst(StringConst stringConst, @Out Context context) {
        return coolString(stringConst.tok.name);
    }

    @Override
    public CoolObject applyBoolConst(BoolConst boolConst, @Out Context context) {
        return coolBool(boolConst.bool);
    }

    @Override
    public CoolObject applyIntConst(IntConst intConst, @Out Context context) {
        return coolInt(Integer.parseInt(intConst.tok.name));
    }

    @Override
    public CoolObject applyParen(Paren paren, @Out Context context) {
        return paren.expr.accept(this, context);
    }

    @Override
    public CoolObject applyNoExpression(NoExpression expr, @Out Context context) {
        return coolVoid();
    }

    @Override
    public CoolObject applyDispatch(Dispatch dispatch, @Out Context context) {
        /**
         * 对方法调用的参数求值
         */
        List<CoolObject> paramObjects = dispatch.params.stream().map(e -> e.accept(this, context)).collect(Collectors.toList());
        /**
         * 对上述参数表达式求得类型
         */
        List<Type> paramTypes = paramObjects.stream().map(e -> e.type).collect(Collectors.toList());
        CoolObject obj = context.selfObject;
        //根据类型，方法名称，类名lookup方法声明
        MethodDeclaration methodDeclaration = treeSupport.lookupMethodDeclaration(obj.type.className(), dispatch.id.name, paramTypes).get();

        CoolObject str = treeSupport.buildIn(paramObjects, obj, methodDeclaration, errorPos(dispatch.starPos, dispatch.endPos));
        if (str != null) return str;

        /**
         * 进入scope
         *
         */
        context.environment.enterScope();
        assert paramObjects.size() == methodDeclaration.declaration.formals.size();
        /**
         * 绑定形参
         */
        for (int i = 0; i < methodDeclaration.declaration.formals.size(); i++) {
            context.environment.addId(methodDeclaration.declaration.formals.get(i).id.name, paramObjects.get(i));
        }
        //对函数体求值
        CoolObject object = methodDeclaration.declaration.expr.accept(this, context);
        /**
         * 退出scope
         */
        context.environment.exitScope();
        return object;
    }

    @Override
    public CoolObject applyStaticDispatchBody(StaticDispatchBody staticDispatchBody, @Out Context context) {
        return coolVoid();
    }

    @Override
    public CoolObject applyStaticDispatch(StaticDispatch staticDispatch, @Out Context context) {
        /**
         * 对方法调用的参数求值
         */
        List<CoolObject> paramObjects = staticDispatch.dispatch.params.stream().map(e -> e.accept(this, context)).collect(Collectors.toList());
        /**
         * 对上述参数表达式求得类型
         */
        List<Type> paramTypes = paramObjects.stream().map(e -> e.type).collect(Collectors.toList());
        MethodDeclaration methodDeclaration;
        // expr[@TYPE].ID( [ expr [[, expr]] ∗ ] )对第一个expr求值
        CoolObject obj = staticDispatch.expr.accept(this, context);
        if (obj.type.type() == TypeEnum.VOID) {
            error("runtime.error.dispatch.void", errorPos(staticDispatch.expr));
        }
        //如果提供type，则根据type查找方法声明
        //如果没提供type，则根据上述expr值的类型查找方法声明
        if (staticDispatch.type.isPresent()) {
            methodDeclaration = treeSupport.lookupMethodDeclaration(staticDispatch.type.get().name, staticDispatch.dispatch.id.name, paramTypes).get();
        } else {
            methodDeclaration = treeSupport.lookupMethodDeclaration(obj.type.className(), staticDispatch.dispatch.id.name, paramTypes).get();
        }

        CoolObject str = treeSupport.buildIn(paramObjects, obj, methodDeclaration, errorPos(staticDispatch.starPos, staticDispatch.endPos));
        if (str != null) return str;

        /**
         * 进入scope,此scope是上述expr值对象的scope
         */
        obj.variables.enterScope();
        assert paramObjects.size() == methodDeclaration.declaration.formals.size();
        /**
         * 绑定形参
         */
        for (int i = 0; i < methodDeclaration.declaration.formals.size(); i++) {
            obj.variables.addId(methodDeclaration.declaration.formals.get(i).id.name, paramObjects.get(i));
        }
        //对函数体求值
        CoolObject object = methodDeclaration.declaration.expr.accept(this, new Context(obj, obj.variables));
        /**
         * 退出scope
         */
        obj.variables.exitScope();
        return object;
    }

    @Override
    public CoolObject applyCond(Cond cond, @Out Context context) {
        if (((CoolBool) cond.condExpr.accept(this, context)).val) {
            return cond.thenExpr.accept(this, context);
        } else {
            return cond.elseExpr.accept(this, context);
        }
    }

    @Override
    public CoolObject applyLoop(Loop loop, @Out Context context) {
        while (((CoolBool) loop.condExpr.accept(this, context)).val) {
            loop.loopExpr.accept(this, context);
        }
        return coolVoid();
    }

    @Override
    public CoolObject applyLet(Let let, @Out Context context) {
        context.environment.enterScope();
        let.attrDefs.forEach(e -> e.accept(this, context));
        CoolObject object = let.expr.accept(this, context);
        context.environment.exitScope();
        return object;
    }

    @Override
    public CoolObject applyCaseDef(CaseDef caseDef, @Out Context context) {
        CoolObject object = caseDef.caseExpr.accept(this, context);
        String temp = object.type.className();
        while (temp != null) {
            final String filterStr = temp;
            Optional<Branch> branchOpt = caseDef.branchList.stream().filter(e -> e.type.name.equals(filterStr)).findFirst();
            if (branchOpt.isPresent()) {
                Branch branch = branchOpt.get();
                context.environment.enterScope();
                context.environment.addId(branch.id.name, object);
                CoolObject returnVal = branch.expr.accept(this, context);
                context.environment.exitScope();
                if (returnVal.type.type() == TypeEnum.VOID) {
                    error("runtime.error.void", errorPos(branch.expr));
                }
                return returnVal;
            }
            temp = treeSupport.classGraph.get(temp);
        }
        error("runtime.error.case", errorPos(caseDef.starPos, caseDef.endPos));
        return coolVoid();
    }

    @Override
    public CoolObject applyBranch(Branch branch, @Out Context context) {
        return coolVoid();
    }

    @Override
    public CoolObject applyLetAttrDef(LetAttrDef letAttrDef, @Out Context context) {
        if (letAttrDef.expr.isPresent()) {
            context.environment.addId(letAttrDef.id.name, letAttrDef.expr.get().accept(this, context));
        } else {
            if (isStringType(letAttrDef.type)) {
                context.environment.addId(letAttrDef.id.name, coolStringDefault());
            } else if (isIntType(letAttrDef.type)) {
                context.environment.addId(letAttrDef.id.name, coolIntDefault());
            } else if (isBoolType(letAttrDef.type)) {
                context.environment.addId(letAttrDef.id.name, coolBoolDefault());
            } else {
                context.environment.addId(letAttrDef.id.name, coolVoid());
            }
        }
        return context.selfObject;
    }
}
