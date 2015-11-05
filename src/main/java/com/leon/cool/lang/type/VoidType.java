package com.leon.cool.lang.type;

/**
 * Created by leon on 15-10-31.
 */
public class VoidType implements Type {
    @Override
    public TypeEnum type() {
        return TypeEnum.VOID;
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
        return "Void";
    }
}
