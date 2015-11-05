package com.leon.cool.lang.ast;

import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.support._;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.type.TypeEnum;

import java.util.List;
import java.util.Optional;

/**
 * Created by leon on 15-10-31.
 */
public class CaseDef extends Expression {
    public Expression caseExpr;
    public List<Branch> branchList;

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
                    _.error("A case on void" + _.errorPos(branch.expr));
                }
                return returnVal;
            }
            temp = _.classGraph.get(temp);
        }
        _.error("Execution of a case statement without a matching branch" + _.errorPos(starPos, endPos));
        return o.coolVoid();
    }
}
