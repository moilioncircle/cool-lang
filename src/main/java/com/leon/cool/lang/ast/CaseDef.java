package com.leon.cool.lang.ast;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.support.Utils;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.type.TypeEnum;

import java.util.List;
import java.util.Optional;

/**
 * Created by leon on 15-10-31.
 */
public class CaseDef extends Expression {
    public final Expression caseExpr;
    public final List<Branch> branchList;

    public CaseDef(Expression caseExpr, List<Branch> branchList) {
        this.caseExpr = caseExpr;
        this.branchList = branchList;
    }

    @Override
    public String toString() {
        return "CaseDef{" +
                "caseExpr=" + caseExpr +
                ", branchList=" + branchList +
                '}';
    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyCaseDef(this);
    }

    @Override
    public CoolObject eval(Env env) {
        CoolObject object = caseExpr.eval(env);
        String temp = object.type.className();
        while (temp != null) {
            final String filterStr = temp;
            Optional<Branch> branchOpt = branchList.stream().filter(e -> e.type.name.equals(filterStr)).findFirst();
            if (branchOpt.isPresent()) {
                Branch branch = branchOpt.get();
                env.env.enterScope();
                env.env.addId(branch.id.name, object);
                CoolObject returnVal = branch.expr.eval(env);
                env.env.exitScope();
                if (returnVal.type.type() == TypeEnum.VOID) {
                    Utils.error("A case on void" + Utils.errorPos(branch.expr));
                }
                return returnVal;
            }
            temp = Utils.classGraph.get(temp);
        }
        Utils.error("Execution of a case statement without a matching branch" + Utils.errorPos(starPos, endPos));
        return ObjectFactory.coolVoid();
    }
}
