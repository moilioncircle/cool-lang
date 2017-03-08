package com.leon.cool.lang.jvm;

/**
 * Created by Baoyi Chen on 2017/3/8.
 */
public class SelfTest3 {

    static class A extends BuiltIn.CoolIO {
        //
        A new$SELF_TYPE() {
            return new A();
        }

        A self() {
            return this;
        }

        //
        BuiltIn.CoolInt test() {
            return A$test();
        }

        A println() {
            return A$println();
        }

        BuiltIn.CoolInt A$test() {
            {
                A a = new$SELF_TYPE();
                a.println();
            }
            return new BuiltIn.CoolInt(0);
        }

        A A$println() {
            return out_string(new BuiltIn.CoolString("hello\n"));
        }

        //io
        BuiltIn.CoolString in_string() {
            return A$in_string();
        }

        BuiltIn.CoolInt in_int() {
            return A$in_int();
        }

        A out_string(BuiltIn.CoolString str) {
            return A$out_string(str);
        }

        A out_int(BuiltIn.CoolInt i) {
            return A$out_int(i);
        }

        BuiltIn.CoolString A$in_string() {
            return (BuiltIn.CoolString) super.in_string();
        }

        BuiltIn.CoolInt A$in_int() {
            return (BuiltIn.CoolInt) super.in_int();
        }

        A A$out_string(BuiltIn.CoolString str) {
            return (A) super.out_string(str);
        }

        A A$out_int(BuiltIn.CoolInt i) {
            return (A) super.out_int(i);
        }

        //object
        BuiltIn.CoolObject abort() {
            return A$abort();
        }

        BuiltIn.CoolString type_name() {
            return A$type_name();
        }

        A copy() {
            return A$copy();
        }

        BuiltIn.CoolObject A$abort() {
            return (BuiltIn.CoolObject) super.abort();
        }

        BuiltIn.CoolString A$type_name() {
            return new BuiltIn.CoolString("A");
        }

        A A$copy() {
            return (A) super.copy();
        }
    }

    static class B extends A {
        //
        B new$SELF_TYPE() {
            return new B();
        }

        B self() {
            return this;
        }

        //
        BuiltIn.CoolInt test() {
            return B$test();
        }

        B println() {
            return B$println();
        }

        BuiltIn.CoolInt B$test() {
            return (BuiltIn.CoolInt) super.test();
        }

        B B$println() {
            return out_string(new BuiltIn.CoolString("world\n"));
        }

        //io
        BuiltIn.CoolString in_string() {
            return B$in_string();
        }

        BuiltIn.CoolInt in_int() {
            return B$in_int();
        }

        B out_string(BuiltIn.CoolString str) {
            return B$out_string(str);
        }

        B out_int(BuiltIn.CoolInt i) {
            return B$out_int(i);
        }

        BuiltIn.CoolString B$in_string() {
            return (BuiltIn.CoolString) super.in_string();
        }

        BuiltIn.CoolInt B$in_int() {
            return (BuiltIn.CoolInt) super.in_int();
        }

        B B$out_string(BuiltIn.CoolString str) {
            return (B) super.out_string(str);
        }

        B B$out_int(BuiltIn.CoolInt i) {
            return (B) super.out_int(i);
        }

        //object
        BuiltIn.CoolObject abort() {
            return B$abort();
        }

        BuiltIn.CoolString type_name() {
            return B$type_name();
        }

        B copy() {
            return B$copy();
        }

        BuiltIn.CoolObject B$abort() {
            return (BuiltIn.CoolObject) super.abort();
        }

        BuiltIn.CoolString B$type_name() {
            return new BuiltIn.CoolString("B");
        }

        B B$copy() {
            return (B) super.copy();
        }
    }

    static class Main extends BuiltIn.CoolObject {
        //
        Main new$SELF_TYPE() {
            return new Main();
        }

        Main self() {
            return this;
        }

        //
        Object main() {
            return Main$main();
        }

        Object Main$main() {
            new A().test();
            return new B().test();
        }

        //object
        BuiltIn.CoolObject abort() {
            return Main$abort();
        }

        BuiltIn.CoolString type_name() {
            return Main$type_name();
        }

        Main copy() {
            return Main$copy();
        }

        BuiltIn.CoolObject Main$abort() {
            return (BuiltIn.CoolObject) super.abort();
        }

        BuiltIn.CoolString Main$type_name() {
            return new BuiltIn.CoolString("Main");
        }

        Main Main$copy() {
            return (Main) super.copy();
        }
    }

    public static void main(String[] args) {
        new Main().main();
    }
}
