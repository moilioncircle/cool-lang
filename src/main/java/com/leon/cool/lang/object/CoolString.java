package com.leon.cool.lang.object;

import com.leon.cool.lang.support._;

/**
 * Created by leon on 15-10-21.
 */
public class CoolString extends CoolObject {
    public String str = "";
    public int length = 0;

    public CoolString(String str, int length) {
        this();
        this.str = str;
        this.length = length;
    }

    public CoolString() {
        this.type = t.stringType();
    }

    public CoolInt length() {
        return new CoolInt(length);
    }

    public CoolString concat(CoolString s) {
        return o.coolString(this.str.concat(s.str));
    }

    public CoolString substr(CoolInt i, CoolInt l) {
        try{
            String str = this.str.substring(i.val, i.val+l.val);
            return o.coolString(str);
        }catch (StringIndexOutOfBoundsException e){
            _.error("Substring out of range.");
        }
        return o.coolStringDefault();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoolString)) return false;

        CoolString that = (CoolString) o;

        if (length != that.length) return false;
        if (str != null ? !str.equals(that.str) : that.str != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = str != null ? str.hashCode() : 0;
        result = 31 * result + length;
        return result;
    }

    public CoolString copy() {
        return o.coolString(this.str);
    }

    @Override
    public String toString() {
        return "CoolString{" +
                "str='" + str + '\'' +
                ", length=" + length +
                '}';
    }
}
