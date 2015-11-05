package com.leon.cool.lang.object;

import com.leon.cool.lang.factory.TypeFactory;

/**
 * Created by leon on 15-10-28.
 */
public class CoolVoid extends CoolObject {
    public CoolVoid() {
        this.type = TypeFactory.voidType();
    }
}
