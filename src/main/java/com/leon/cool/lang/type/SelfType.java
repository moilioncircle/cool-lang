package com.leon.cool.lang.type;

import com.leon.cool.lang.factory.TypeFactory;
import com.leon.cool.lang.support.Utils;

/**
 * Created by leon on 15-10-16.
 */
public class SelfType implements Type {
    private final String type;

    public SelfType(String type) {
        this.type = type;
    }

    public Type replace() {
        return TypeFactory.objectType(type);
    }

    @Override
    public String className() {
        Utils.error("Not allowed SELF_TYPE");
        return toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SelfType)) return false;

        SelfType selfType = (SelfType) o;

        return !(type != null ? !type.equals(selfType.type) : selfType.type != null);

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    @Override
    public TypeEnum type() {
        return TypeEnum.SELF_TYPE;
    }

    @Override
    public String toString() {
        return "SELF_TYPE(" + type + ')';
    }
}