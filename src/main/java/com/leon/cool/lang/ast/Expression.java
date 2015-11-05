package com.leon.cool.lang.ast;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.object.CoolObject;

/**
 * Created by leon on 15-10-31.
 */
public abstract class Expression extends TreeNode {
    public ObjectFactory o = new ObjectFactory();
    public abstract CoolObject eval(Env env);
}
