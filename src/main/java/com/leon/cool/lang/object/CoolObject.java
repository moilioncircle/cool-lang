package com.leon.cool.lang.object;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.factory.TypeFactory;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.support._;
import com.leon.cool.lang.type.Type;
import com.leon.cool.lang.util.Constant;

/**
 * Created by leon on 15-10-21.
 */
public class CoolObject {
    ObjectFactory o = new ObjectFactory();
    TypeFactory t = new TypeFactory();
    public Type type = t.objectType(Constant.OBJECT);
    public Env env = new Env();

    public CoolObject() {

    }

    public CoolObject abort() {
        System.out.println(this.type + " abort and exit.");
        _.clear();
        _.close();
        System.exit(0);
        return this;
    }

    public CoolObject copy() {
        CoolObject object = o.coolObject();
        object.type = this.type;
        object.env = this.env;
        return object;
    }
}
