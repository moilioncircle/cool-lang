package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.*;
import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.factory.TypeFactory;
import com.leon.cool.lang.object.CoolBool;
import com.leon.cool.lang.object.CoolInt;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.object.CoolString;
import com.leon.cool.lang.support.Context;
import com.leon.cool.lang.support.MethodDeclaration;
import com.leon.cool.lang.support.Utils;
import com.leon.cool.lang.type.Type;
import com.leon.cool.lang.type.TypeEnum;
import com.leon.cool.lang.util.Constant;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public CoolObject applyAssign(Assign assign, Context context) {
        CoolObject object = assign.expr.accept(this, context);
        context.environment.update(assign.id.tok.name, object);
        return object;
    }

    @Override
    public CoolObject applyBlocks(Blocks blocks, Context context) {
        CoolObject object = ObjectFactory.coolVoid();
        for (Expression expr : blocks.exprs) {
            object = expr.accept(this, context);
        }
        return object;
    }

    @Override
    public CoolObject applyNewDef(NewDef newDef, Context context) {
        Type type;
        if (Utils.isSelfType(newDef.type)) {
            type = context.selfObject.type;
        } else {
            type = TypeFactory.objectType(newDef.type.name);
        }
        return Utils.newDef(this, type, context);
    }

    @Override
    public CoolObject applyIsVoid(IsVoid isVoid, Context context) {
        CoolObject object = isVoid.expr.accept(this, context);
        return ObjectFactory.coolBool(object.type.type() == TypeEnum.VOID);
    }

    @Override
    public CoolObject applyPlus(Plus plus, Context context) {
        CoolInt l = (CoolInt) plus.left.accept(this, context);
        CoolInt r = (CoolInt) plus.right.accept(this, context);
        return ObjectFactory.coolInt(l.val + r.val);
    }

    @Override
    public CoolObject applySub(Sub sub, Context context) {
        CoolInt l = (CoolInt) sub.left.accept(this, context);
        CoolInt r = (CoolInt) sub.right.accept(this, context);
        return ObjectFactory.coolInt(l.val - r.val);
    }

    @Override
    public CoolObject applyMul(Mul mul, Context context) {
        CoolInt l = (CoolInt) mul.left.accept(this, context);
        CoolInt r = (CoolInt) mul.right.accept(this, context);
        return ObjectFactory.coolInt(l.val * r.val);
    }

    @Override
    public CoolObject applyDivide(Divide divide, Context context) {
        CoolInt l = (CoolInt) divide.left.accept(this, context);
        CoolInt r = (CoolInt) divide.right.accept(this, context);
        if (r.val == 0) {
            Utils.error("runtime.error.divide.zero", Utils.errorPos(divide.right));
        }
        return ObjectFactory.coolInt(l.val / r.val);
    }

    @Override
    public CoolObject applyNeg(Neg neg, Context context) {
        CoolInt val = (CoolInt) neg.expr.accept(this, context);
        return ObjectFactory.coolInt(-val.val);
    }

    @Override
    public CoolObject applyLt(Lt lt, Context context) {
        CoolInt l = (CoolInt) lt.left.accept(this, context);
        CoolInt r = (CoolInt) lt.right.accept(this, context);
        if (l.val < r.val) {
            return ObjectFactory.coolBool(true);
        } else {
            return ObjectFactory.coolBool(false);
        }
    }

    @Override
    public CoolObject applyLtEq(LtEq ltEq, Context context) {
        CoolInt l = (CoolInt) ltEq.left.accept(this, context);
        CoolInt r = (CoolInt) ltEq.right.accept(this, context);
        if (l.val <= r.val) {
            return ObjectFactory.coolBool(true);
        } else {
            return ObjectFactory.coolBool(false);
        }
    }

    @Override
    public CoolObject applyComp(Comp comp, Context context) {
        CoolObject l = comp.left.accept(this, context);
        CoolObject r = comp.right.accept(this, context);
        if (Utils.isBasicType(l.type) && Utils.isBasicType(r.type)) {
            if (l instanceof CoolString && r instanceof CoolString) {
                return ObjectFactory.coolBool(((CoolString) l).str.equals(((CoolString) r).str));
            } else if (l instanceof CoolInt && r instanceof CoolInt) {
                return ObjectFactory.coolBool(((CoolInt) l).val == ((CoolInt) r).val);
            } else if (l instanceof CoolBool && r instanceof CoolBool) {
                return ObjectFactory.coolBool(((CoolBool) l).val == ((CoolBool) r).val);
            } else {
                throw new AssertionError("unexpected error.");
            }
        } else {
            return ObjectFactory.coolBool(l.equals(r));
        }
    }

    @Override
    public CoolObject applyNot(Not not, Context context) {
        CoolBool bool = (CoolBool) not.expr.accept(this, context);
        return ObjectFactory.coolBool(!bool.val);
    }

    @Override
    public CoolObject applyIdConst(IdConst idConst, Context context) {
        if (idConst.tok.name.equals(Constant.SELF)) {
            return context.selfObject;
        } else {
            return context.environment.lookup(idConst.tok.name).get();
        }
    }

    @Override
    public CoolObject applyStringConst(StringConst stringConst, Context context) {
        return ObjectFactory.coolString(stringConst.tok.name);
    }

    @Override
    public CoolObject applyBoolConst(BoolConst boolConst, Context context) {
        return ObjectFactory.coolBool(boolConst.bool);
    }

    @Override
    public CoolObject applyIntConst(IntConst intConst, Context context) {
        return ObjectFactory.coolInt(Integer.parseInt(intConst.tok.name));
    }

    @Override
    public CoolObject applyParen(Paren paren, Context context) {
        return paren.expr.accept(this, context);
    }

    @Override
    public CoolObject applyNoExpression(NoExpression expr, Context context) {
        return ObjectFactory.coolVoid();
    }

    @Override
    public CoolObject applyDispatch(Dispatch dispatch, Context context) {
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
        MethodDeclaration methodDeclaration = Utils.lookupMethodDeclaration(obj.type.className(), dispatch.id.name, paramTypes).get();

        CoolObject str = Utils.buildIn(paramObjects, obj, methodDeclaration, Utils.errorPos(dispatch.starPos, dispatch.endPos));
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
    public CoolObject applyStaticDispatchBody(StaticDispatchBody staticDispatchBody, Context context) {
        return ObjectFactory.coolVoid();
    }

    @Override
    public CoolObject applyStaticDispatch(StaticDispatch staticDispatch, Context context) {
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
            Utils.error("runtime.error.dispatch.void", Utils.errorPos(staticDispatch.expr));
        }
        //如果提供type，则根据type查找方法声明
        //如果没提供type，则根据上述expr值的类型查找方法声明
        if (staticDispatch.type.isPresent()) {
            methodDeclaration = Utils.lookupMethodDeclaration(staticDispatch.type.get().name, staticDispatch.dispatch.id.name, paramTypes).get();
        } else {
            methodDeclaration = Utils.lookupMethodDeclaration(obj.type.className(), staticDispatch.dispatch.id.name, paramTypes).get();
        }

        CoolObject str = Utils.buildIn(paramObjects, obj, methodDeclaration, Utils.errorPos(staticDispatch.starPos, staticDispatch.endPos));
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
    public CoolObject applyCond(Cond cond, Context context) {
        if (((CoolBool) cond.condExpr.accept(this, context)).val) {
            return cond.thenExpr.accept(this, context);
        } else {
            return cond.elseExpr.accept(this, context);
        }
    }

    @Override
    public CoolObject applyLoop(Loop loop, Context context) {
        while (((CoolBool) loop.condExpr.accept(this, context)).val) {
            loop.loopExpr.accept(this, context);
        }
        return ObjectFactory.coolVoid();
    }

    @Override
    public CoolObject applyLet(Let let, Context context) {
        context.environment.enterScope();
        let.attrDefs.forEach(e -> e.accept(this, context));
        CoolObject object = let.expr.accept(this, context);
        context.environment.exitScope();
        return object;
    }

    @Override
    public CoolObject applyCaseDef(CaseDef caseDef, Context context) {
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
                    Utils.error("runtime.error.void", Utils.errorPos(branch.expr));
                }
                return returnVal;
            }
            temp = Utils.classGraph.get(temp);
        }
        Utils.error("runtime.error.case", Utils.errorPos(caseDef.starPos, caseDef.endPos));
        return ObjectFactory.coolVoid();
    }

    @Override
    public CoolObject applyBranch(Branch branch, Context context) {
        return ObjectFactory.coolVoid();
    }

    @Override
    public CoolObject applyLetAttrDef(LetAttrDef letAttrDef, Context context) {
        if (letAttrDef.expr.isPresent()) {
            context.environment.addId(letAttrDef.id.name, letAttrDef.expr.get().accept(this, context));
        } else {
            if (Utils.isStringType(letAttrDef.type)) {
                context.environment.addId(letAttrDef.id.name, ObjectFactory.coolStringDefault());
            } else if (Utils.isIntType(letAttrDef.type)) {
                context.environment.addId(letAttrDef.id.name, ObjectFactory.coolIntDefault());
            } else if (Utils.isBoolType(letAttrDef.type)) {
                context.environment.addId(letAttrDef.id.name, ObjectFactory.coolBoolDefault());
            } else {
                context.environment.addId(letAttrDef.id.name, ObjectFactory.coolVoid());
            }
        }
        return context.selfObject;
    }
}
