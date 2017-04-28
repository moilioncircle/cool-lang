package com.leon.cool.lang.jvm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Baoyi Chen on 2017/3/7.
 */
public class BuiltIn {
    static class CoolObject implements Cloneable {

        CoolObject new$SELF_TYPE() {
            return new CoolObject();
        }

        CoolObject self() {
            return this;
        }

        CoolObject abort() {
            return CoolObject$abort();
        }

        CoolString type_name() {
            return CoolObject$type_name();
        }

        CoolObject copy() {
            return CoolObject$copy();
        }

        CoolObject CoolObject$abort() {
            System.exit(0);
            return self();
        }

        CoolString CoolObject$type_name() {
            return new CoolString("Object");
        }

        CoolObject CoolObject$copy() {
            try {
                return (CoolObject) clone();
            } catch (CloneNotSupportedException e) {
                return new CoolObject();
            }
        }
    }

    static class CoolString extends CoolObject {

        CoolString new$SELF_TYPE() {
            return new CoolString("");
        }

        CoolString self() {
            return this;
        }

        CoolString(String _val) {
            this._val = _val;
        }

        String _val = "";

        CoolInt length() {
            return CoolString$length();
        }

        CoolString concat(CoolString str) {
            return CoolString$concat(str);
        }

        CoolString substr(CoolInt idx, CoolInt len) {
            return CoolString$substr(idx, len);
        }

        CoolInt CoolString$length() {
            return new CoolInt(_val.length());
        }

        CoolString CoolString$concat(CoolString str) {
            return new CoolString(this._val.concat(str._val));
        }

        CoolString CoolString$substr(CoolInt idx, CoolInt len) {
            return new CoolString(this._val.substring(idx._val, len._val));
        }

        //object
        CoolObject abort() {
            return CoolString$abort();
        }

        CoolString type_name() {
            return CoolString$type_name();
        }

        CoolString copy() {
            return CoolString$copy();
        }

        CoolObject CoolString$abort() {
            return (CoolObject) super.abort();
        }

        CoolString CoolString$type_name() {
            return new CoolString("String");
        }

        CoolString CoolString$copy() {
            return (CoolString) super.copy();
        }
    }

    static class CoolInt extends CoolObject {

        CoolInt new$SELF_TYPE() {
            return new CoolInt(0);
        }

        CoolInt self() {
            return this;
        }

        CoolInt(int _val) {
            this._val = _val;
        }

        int _val = 0;

        //object
        CoolObject abort() {
            return CoolInt$abort();
        }

        CoolString type_name() {
            return CoolInt$type_name();
        }

        CoolInt copy() {
            return CoolInt$copy();
        }

        CoolObject CoolInt$abort() {
            return (CoolObject) super.abort();
        }

        CoolString CoolInt$type_name() {
            return new CoolString("Int");
        }

        CoolInt CoolInt$copy() {
            return (CoolInt) super.copy();
        }
    }

    static class CoolBool extends CoolObject {
        CoolBool new$SELF_TYPE() {
            return new CoolBool(false);
        }

        CoolBool self() {
            return this;
        }

        CoolBool(boolean _val) {
            this._val = _val;
        }

        boolean _val = false;

        //object
        CoolObject abort() {
            return CoolBool$abort();
        }

        CoolString type_name() {
            return CoolBool$type_name();
        }

        CoolBool copy() {
            return CoolBool$copy();
        }

        CoolObject CoolBool$abort() {
            return (CoolObject) super.abort();
        }

        CoolString CoolBool$type_name() {
            return new CoolString("Bool");
        }

        CoolBool CoolBool$copy() {
            return (CoolBool) super.copy();
        }
    }

    static class CoolIO extends CoolObject {
        private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        static {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }));
        }

        CoolIO new$SELF_TYPE() {
            return new CoolIO();
        }

        CoolIO self() {
            return this;
        }

        //io
        CoolString in_string() {
            return CoolIO$in_string();
        }

        CoolInt in_int() {
            return CoolIO$in_int();
        }

        CoolIO out_string(CoolString str) {
            return CoolIO$out_string(str);
        }

        CoolIO out_int(CoolInt i) {
            return CoolIO$out_int(i);
        }

        CoolString CoolIO$in_string() {
            try {
                return new CoolString(reader.readLine());
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        CoolInt CoolIO$in_int() {
            try {
                return new CoolInt(Integer.parseInt(reader.readLine()));
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        CoolIO CoolIO$out_string(CoolString str) {
            System.out.print(str._val);
            return self();
        }

        CoolIO CoolIO$out_int(CoolInt i) {
            System.out.print(i._val);
            return self();
        }

        //object
        CoolObject abort() {
            return CoolIO$abort();
        }

        CoolString type_name() {
            return CoolIO$type_name();
        }

        CoolIO copy() {
            return CoolIO$copy();
        }

        CoolObject CoolIO$abort() {
            return (CoolObject) super.abort();
        }

        CoolString CoolIO$type_name() {
            return new CoolString("IO");
        }

        CoolIO CoolIO$copy() {
            return (CoolIO) super.copy();
        }
    }
}
