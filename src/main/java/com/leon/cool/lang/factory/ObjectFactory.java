package com.leon.cool.lang.factory;

import com.leon.cool.lang.object.*;

/**
 * Created by leon on 15-10-21.
 */
public class ObjectFactory {
    public static CoolInt coolInt(int i) {
        return new CoolInt(i);
    }

    public static CoolInt coolIntDefault() {
        return new CoolInt();
    }

    public static CoolString coolString(String i) {
        return new CoolString(i, i.length());
    }

    public static CoolString coolStringDefault() {
        return new CoolString();
    }

    public static CoolBool coolBool(boolean i) {
        return new CoolBool(i);
    }

    public static CoolBool coolBoolDefault() {
        return new CoolBool();
    }

    public static CoolObject coolObject() {
        return new CoolObject();
    }

    public static CoolVoid coolVoid() {
        return new CoolVoid();
    }
}
