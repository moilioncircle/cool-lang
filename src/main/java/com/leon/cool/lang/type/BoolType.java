package com.leon.cool.lang.type;

/**
 * Created by leon on 15-10-14.
 */
public class BoolType implements Type {
    @Override
    public TypeEnum type() {
        return TypeEnum.BOOL;
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
        return "Bool";
    }
}
