package com.leon.cool.lang.type;

/**
 * Created by leon on 15-10-16.
 */
public class NoType implements Type {
    @Override
    public TypeEnum type() {
        return TypeEnum.NO_TYPE;
    }

    @Override
    public Type replace() {
        return this;
    }

    @Override
    public String className() {
        return toString();
    }

    @Override
    public String toString() {
        return "NoType";
    }
}
