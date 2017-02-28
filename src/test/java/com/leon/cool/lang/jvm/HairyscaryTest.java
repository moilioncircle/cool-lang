package com.leon.cool.lang.jvm;

/**
 * Created by Baoyi Chen on 2017/2/28.
 */
public class HairyscaryTest {
    public static void main(String[] args) {
        //17141611714163171416511714161171416317141653117141611714163171416511714161171416317141653171416117141631714165171416
        new Main();
    }

    public static class Main {
        Bazz a = new Bazz();
        Foo b = new Foo();
        Razz c = new Razz();
        Bar d = new Bar();
    }

    private static class Bazz {
        int h = 1;
        Foo g = (this.getClass() == Bazz.class) ? new Foo() : (this.getClass() == Razz.class) ? new Bar() : (this.getClass() == Foo.class) ? new Razz() : (this.getClass() == Bar.class) ? (Foo) this : null;
        Object i = printh();

        int printh() {
            System.out.print(h);
            return 0;
        }

        int doh() {
            return Bazz$doh();
        }

        int Bazz$doh() {
            int i = h;
            h = h + 1;
            return i;
        }
    }

    private static class Foo extends Bazz {
        Razz a = (this.getClass() == Razz.class) ? new Bar() : (this.getClass() == Foo.class) ? new Razz() : (this.getClass() == Bar.class) ? (Razz) this : null;
        int b = a.doh() + g.doh() + doh() + printh();

        int doh() {
            return Foo$doh();
        }

        int Foo$doh() {
            int i = h;
            h = h + 2;
            return i;
        }
    }

    private static class Razz extends Foo {
        Bar e = (this.getClass() == Razz.class) ? new Bar() : (this.getClass() == Bar.class) ? (Bar) this : null;
        int f = a.Bazz$doh() + g.doh() + e.doh() + doh() + printh();
    }

    private static class Bar extends Razz {
        int c = doh();
        Object d = printh();
    }
}
