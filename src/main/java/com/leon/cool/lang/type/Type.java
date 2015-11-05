package com.leon.cool.lang.type;

/**
 * Created by leon on 15-10-14.
 */
public interface Type {
    public abstract TypeEnum type();

    public abstract Type replace();

    public abstract String className();
}
