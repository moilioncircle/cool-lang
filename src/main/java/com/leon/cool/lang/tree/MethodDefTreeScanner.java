package com.leon.cool.lang.tree;

import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.ast.MethodDef;
import com.leon.cool.lang.support.MethodDeclaration;
import com.leon.cool.lang.support._;

import java.util.stream.Collectors;

/**
 * Created by leon on 15-10-15.
 */
public class MethodDefTreeScanner extends TreeScanner {
    private String className;

    public void applyClassDef(ClassDef classDef) {
        className = classDef.type.name;
        _.createMethodGraph(className);
        super.applyClassDef(classDef);
    }

    public void applyMethodDef(MethodDef methodDef) {
        if (!_.isTypeDefined(methodDef.type)) {
            _.error("type not defined,type:" + methodDef.type.name + _.errorPos(methodDef.type));
        }
        MethodDeclaration methodDeclaration = new MethodDeclaration();
        methodDeclaration.methodName = methodDef.id.name;
        methodDeclaration.returnType = methodDef.type.name;
        methodDeclaration.paramTypes = methodDef.formals.stream().map(e -> {
            if (!_.isTypeDefined(e.type)) {
                _.error("type not defined,type:" + e.type.name + _.errorPos(e.type));
            }
            return e.type.name;
        }).collect(Collectors.toList());
        methodDeclaration.declaration = methodDef;
        methodDeclaration.belongs = className;
        _.putToMethodGraph(className, methodDeclaration);
        super.applyMethodDef(methodDef);
    }
}
