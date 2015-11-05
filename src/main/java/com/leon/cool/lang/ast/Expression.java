package com.leon.cool.lang.ast;

import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.support.Env;

/**
 * Created by leon on 15-10-31.
 */
public abstract class Expression extends TreeNode {
    public abstract CoolObject eval(Env env);
}
