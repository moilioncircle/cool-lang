package com.leon.cool.lang.jvm;

/**
 * Created by Baoyi Chen on 2017/2/28.
 */
public class DispatchTest {
    public static void main(String[] args) {
        Next n = new Next();
        // static dispatch
        // n@Prev.test();
        n.Prev$test();

        // dispatch
        // n.test();
        n.test();
    }

    private static class Prev {
        @SuppressWarnings("unused")
        protected void test() {
            Prev$test();
        }

        protected void Prev$test() {
            System.out.println("prev");
        }
    }

    private static class Curr extends Prev {
        protected void test() {
            Curr$test();
        }

        protected void Curr$test() {
            System.out.println("curr");
        }
    }

    private static class Next extends Curr {
        protected void test() {
            Next$test();
        }

        protected void Next$test() {
            System.out.println("next");
        }
    }
}
