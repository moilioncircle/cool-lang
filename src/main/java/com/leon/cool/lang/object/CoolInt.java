package com.leon.cool.lang.object;

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
        this.type = t.integerType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoolInt)) return false;

        CoolInt coolInt = (CoolInt) o;

        if (val != coolInt.val) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return val;
    }

    public CoolInt copy() {
        return o.coolInt(this.val);
    }
}
