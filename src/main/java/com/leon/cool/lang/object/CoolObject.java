package com.leon.cool.lang.object;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.factory.TypeFactory;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.support.Utils;
import com.leon.cool.lang.type.Type;
import com.leon.cool.lang.util.Constant;

/**
 * Created by leon on 15-10-21.
 */
public class CoolObject {
    public Type type = TypeFactory.objectType(Constant.OBJECT);
    public Env env = new Env();

    public CoolObject() {

    }

    public CoolObject abort() {
        System.out.println(this.type + " abort and exit.");
        Utils.clear();
        Utils.close();
        System.exit(0);
        return this;
    }

    public CoolObject copy() {
        CoolObject object = ObjectFactory.coolObject();
        object.type = this.type;
        object.env = this.env;
        return object;
    }
}
