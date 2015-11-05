package com.leon.cool.lang.ast;

import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.tree.TreeVisitor;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.tokenizer.TokenKind;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by leon on 15-10-31.
 */
public class Program extends TreeNode {
    public List<ClassDef> classDef;

    public Program(List<ClassDef> classDef) {
        this.classDef = classDef;
    }

    @Override
    public String toString() {
        return "Program{" +
                "classDef=" + classDef +
                '}';
    }

    public CoolObject run() {
        Expression staticDispatch = new StaticDispatch(new NewDef(new Token("Main", TokenKind.TYPE)), Optional.<Token>empty(), new StaticDispatchBody(new Token("main", TokenKind.ID), new ArrayList<>()));

        CoolObject coolObject = staticDispatch.eval(new Env());
        return coolObject;

    }

    @Override
    public void accept(TreeVisitor visitor) {
        visitor.applyProgram(this);
    }
}
