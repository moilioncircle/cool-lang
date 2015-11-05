package com.leon.cool.lang.factory;

import com.leon.cool.lang.support._;
import com.leon.cool.lang.type.*;
import com.leon.cool.lang.util.Constant;

/**
 * Created by leon on 15-10-16.
 */
public class TypeFactory {
    public static Type stringType() {
        return new StringType();
    }

    public static Type booleanType() {
        return new BoolType();
    }

    public static Type integerType() {
        return new IntType();
    }

    public static Type objectType(String type, String className) {
        switch (type) {
            case Constant.STRING:
                return stringType();
            case Constant.INT:
                return integerType();
            case Constant.BOOL:
                return booleanType();
            case Constant.SELF_TYPE:
                return selfType(className);
            default:
                return new ObjectType(type);
        }
    }

    public static Type objectType(String type) {
        switch (type) {
            case Constant.STRING:
                return stringType();
            case Constant.INT:
                return integerType();
            case Constant.BOOL:
                return booleanType();
            case Constant.SELF_TYPE:
                _.error("Not allowed SELF_TYPE");
            default:
                return new ObjectType(type);
        }
    }

    public static Type selfType(String className) {
        return new SelfType(className);
    }

    public static Type noType() {
        return new NoType();
    }

    public static Type voidType() {
        return new VoidType();
    }
}
