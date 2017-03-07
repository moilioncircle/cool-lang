package com.leon.cool.lang.jvm;

/**
 * Created by Baoyi Chen on 2017/2/28.
 */
public class HairyscaryTest {
    public static void main(String[] args) {
        //17141611714163171416511714161171416317141653117141611714163171416511714161171416317141653171416117141631714165171416
        new Main().main();
    }

    public static class Main {
        Bazz a = attr$a();
        Foo b = attr$b();
        Razz c = attr$c();
        Bar d = attr$d();

        Bazz attr$a() {
            return new Bazz();
        }

        Foo attr$b() {
            return new Foo();
        }

        Razz attr$c() {
            return new Razz();
        }

        Bar attr$d() {
            return new Bar();
        }

        String main() {
            return "do nothing";
        }
    }

    private static class Bazz {
        int h = attr$h();
        Foo g = attr$g();
        Object i = attr$i();

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

        int attr$h() {
            return 1;
        }

        Object attr$i() {
            return printh();
        }

        Foo attr$g() {
            return (this.getClass() == Bazz.class) ? new Foo() : (this.getClass() == Razz.class) ? new Bar() : (this.getClass() == Foo.class) ? new Razz() : (this.getClass() == Bar.class) ? (Foo) this : null;
        }
    }

    private static class Foo extends Bazz {
        Razz a = attr$a();
        int b = attr$b();

        int doh() {
            return Foo$doh();
        }

        int Foo$doh() {
            int i = h;
            h = h + 2;
            return i;
        }

        Razz attr$a() {
            return (this.getClass() == Razz.class) ? new Bar() : (this.getClass() == Foo.class) ? new Razz() : (this.getClass() == Bar.class) ? (Razz) this : null;
        }

        int attr$b() {
            return a.doh() + g.doh() + doh() + printh();
        }
    }

    private static class Razz extends Foo {
        Bar e = attr$e();
        int f = attr$f();

        Bar attr$e() {
            return (this.getClass() == Razz.class) ? new Bar() : (this.getClass() == Bar.class) ? (Bar) this : null;
        }

        int attr$f() {
            return a.Bazz$doh() + g.doh() + e.doh() + doh() + printh();
        }
    }

    private static class Bar extends Razz {
        int c = attr$c();
        Object d = attr$d();

        int attr$c() {
            return doh();
        }

        Object attr$d() {
            return printh();
        }
    }
}
