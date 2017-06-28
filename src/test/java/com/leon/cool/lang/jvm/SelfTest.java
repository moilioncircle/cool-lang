package com.leon.cool.lang.jvm;

/**
 * Created by Baoyi Chen on 2017/2/28.
 */
public class SelfTest {
//    class Silly {
//        copy() : SELF_TYPE {
//            (new SELF_TYPE);
//        };
//    };
//
//    class Sally inherits Silly {
//    };
//
//    class Main {
//        x : Sally <- (new Sally).copy();
//
//        main() : Sally { x };
//    };

    //    Finally,SELF_TYPE may be used in the following places:
//    new SELF_TYPE,
//    as the return type of a method,
//    as the declared type of a let variable,
//    or as the declared type of an attribute.
//    No other uses of SELF_TYPE are permitted.
    @SuppressWarnings("unused")
    private static class Silly {
        Silly copy() {
            return this;
        }

        Silly Silly$copy() {
            return this;
        }

        Silly new$SELF_TYPE() {
            return new Silly();
        }

        Silly self() {
            return this;
        }
    }

    private static class Sally extends Silly {
        Sally copy() {
            return (Sally) super.copy();
        }

        Sally new$SELF_TYPE() {
            return new Sally();
        }

        Sally self() {
            return this;
        }
    }

    private static class Main {
        Sally x = new Sally().copy();

        Sally main() {
            return x;
        }
    }

    public static void main(String[] args) {
        new Main().main();
    }
}
