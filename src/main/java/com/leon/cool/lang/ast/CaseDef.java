package com.leon.cool.lang.ast;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.Context;
import com.leon.cool.lang.support.Utils;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.type.TypeEnum;

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
 * @author leon on 15-10-31
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
    public CoolObject eval(Context context) {
        CoolObject object = caseExpr.eval(context);
        String temp = object.type.className();
        while (temp != null) {
            final String filterStr = temp;
            Optional<Branch> branchOpt = branchList.stream().filter(e -> e.type.name.equals(filterStr)).findFirst();
            if (branchOpt.isPresent()) {
                Branch branch = branchOpt.get();
                context.environment.enterScope();
                context.environment.addId(branch.id.name, object);
                CoolObject returnVal = branch.expr.eval(context);
                context.environment.exitScope();
                if (returnVal.type.type() == TypeEnum.VOID) {
                    Utils.error("runtime.error.void", Utils.errorPos(branch.expr));
                }
                return returnVal;
            }
            temp = Utils.classGraph.get(temp);
        }
        Utils.error("runtime.error.case", Utils.errorPos(starPos, endPos));
        return ObjectFactory.coolVoid();
    }
}
