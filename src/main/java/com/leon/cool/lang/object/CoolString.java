package com.leon.cool.lang.object;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.factory.TypeFactory;
import com.leon.cool.lang.support.Utils;

/**
 * Created by leon on 15-10-21.
 */
public class CoolString extends CoolObject {
    public String str = "";
    private int length = 0;

    public CoolString(String str, int length) {
        this();
        this.str = str;
        this.length = length;
    }

    public CoolString() {
        this.type = TypeFactory.stringType();
    }

    public CoolInt length() {
        return new CoolInt(length);
    }

    public CoolString concat(CoolString s) {
        return ObjectFactory.coolString(this.str.concat(s.str));
    }

    public CoolString substr(CoolInt i, CoolInt l) {
        try{
            String str = this.str.substring(i.val, i.val+l.val);
            return ObjectFactory.coolString(str);
        }catch (StringIndexOutOfBoundsException e){
            Utils.error("Substring out of range.");
        }
        return ObjectFactory.coolStringDefault();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoolString)) return false;

        CoolString that = (CoolString) o;

        return length == that.length && !(str != null ? !str.equals(that.str) : that.str != null);

    }

    @Override
    public int hashCode() {
        int result = str != null ? str.hashCode() : 0;
        result = 31 * result + length;
        return result;
    }

    public CoolString copy() {
        return ObjectFactory.coolString(this.str);
    }

    @Override
    public String toString() {
        return "CoolString{" +
                "str='" + str + '\'' +
                ", length=" + length +
                '}';
    }
}
