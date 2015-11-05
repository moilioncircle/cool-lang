package com.leon.cool.lang.object;

/**
 * Created by leon on 15-10-21.
 */
public class CoolBool extends CoolObject {
    public boolean val = false;

    public CoolBool(boolean val) {
        this();
        this.val = val;
    }

    public CoolBool() {
        this.type = t.booleanType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoolBool)) return false;

        CoolBool coolBool = (CoolBool) o;

        if (val != coolBool.val) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (val ? 1 : 0);
    }

    public CoolBool copy() {
        return new CoolBool(this.val);
    }
}
