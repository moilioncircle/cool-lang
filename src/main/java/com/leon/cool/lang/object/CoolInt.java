package com.leon.cool.lang.object;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.factory.TypeFactory;

/**
 * Created by leon on 15-10-21.
 */
public class CoolInt extends CoolObject {
    public int val = 0;

    public CoolInt(int val) {
        this();
        this.val = val;
    }

    public CoolInt() {
        this.type = TypeFactory.integerType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoolInt)) return false;

        CoolInt coolInt = (CoolInt) o;

        return val == coolInt.val;
    }

    @Override
    public int hashCode() {
        return val;
    }

    public CoolInt copy() {
        return ObjectFactory.coolInt(this.val);
    }
}
