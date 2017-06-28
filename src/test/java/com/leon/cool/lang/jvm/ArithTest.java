package com.leon.cool.lang.jvm;

import com.leon.cool.lang.Bootstrap;
import com.leon.cool.lang.util.FileUtil;

import java.util.Scanner;

/**
 * Created by Baoyi Chen on 2017/3/1.
 */
public class ArithTest {

    private static class A {
        @SuppressWarnings("unused")
        A new$SELF_TYPE() {
            return new A();
        }

        @SuppressWarnings("unused")
        A self() {
            return this;
        }

        int var = 0;

        int value() {
            return A$value();
        }

        A set_var(int num) {
            return A$set_var(num);
        }

        A method1(int num) {
            return A$method1(num);
        }

        B method2(int num1, int num2) {
            return A$method2(num1, num2);
        }

        C method3(int num) {
            return A$method3(num);
        }

        D method4(int num1, int num2) {
            return A$method4(num1, num2);
        }

        @SuppressWarnings("unused")
        E method5(int num) {
            return A$method5(num);
        }

        //
        int A$value() {
            return var;
        }

        A A$set_var(int num) {
            var = num;
            return this;
        }

        A A$method1(int num) {
            return this;
        }

        B A$method2(int num1, int num2) {
            {
                int x;
                x = num1 + num2;
                return (new B()).set_var(x);
            }
        }

        C A$method3(int num) {
            {
                int x;
                x = -num;
                return (new C()).set_var(x);
            }
        }

        D A$method4(int num1, int num2) {
            if (num2 < num1) {
                {
                    int x;
                    x = -num1 - num2;
                    return (new D()).set_var(x);
                }
            } else {
                {
                    int x;
                    x = num2 - num1;
                    return (new D()).set_var(x);
                }
            }
        }

        E A$method5(int num) {
            {
                int x = -1;
                {
                    int y = -1;
                    while (y <= num) {
                        x = x * y;
                        y = y + 1;
                    }
                }
                return (new E()).set_var(x);
            }
        }
    }

    private static class B extends A {
        //返回值是SELF_TYPE子类要重写
        B new$SELF_TYPE() {
            return new B();
        }

        B self() {
            return this;
        }

        B set_var(int num) {
            return (B) super.set_var(num);
        }

        B method1(int num) {
            return (B) super.method1(num);
        }

        //
        E method5(int num) {
            return B$method5(num);
        }

        E B$method5(int num) {
            {
                int x;
                x = num * num;
                return (new E()).set_var(x);
            }
        }
    }

    private static class C extends B {
        //返回值是SELF_TYPE子类要重写
        C new$SELF_TYPE() {
            return new C();
        }

        C self() {
            return this;
        }

        C set_var(int num) {
            return (C) super.set_var(num);
        }

        C method1(int num) {
            return (C) super.method1(num);
        }

        //
        A method6(int num) {
            return C$method6(num);
        }

        E method5(int num) {
            return C$method5(num);
        }

        A C$method6(int num) {
            {
                return (new A()).set_var(num);
            }
        }

        E C$method5(int num) {
            {
                int x;
                x = num * num * num;
                return (new E()).set_var(x);
            }
        }
    }

    private static class D extends B {
        //返回值是SELF_TYPE子类要重写
        D new$SELF_TYPE() {
            return new D();
        }

        D self() {
            return this;
        }

        D set_var(int num) {
            return (D) super.set_var(num);
        }

        D method1(int num) {
            return (D) super.method1(num);
        }

        //
        boolean method7(int num) {
            return D$method7(num);
        }

        boolean D$method7(int num) {
            {
                int x = num;
                if (x < 0) {
                    return method7(-x);
                } else if (0 == x) {
                    return true;
                } else if (1 == x) {
                    return false;
                } else if (2 == x) {
                    return false;
                } else {
                    return method7(x - 3);
                }
            }
        }
    }

    private static class E extends D {
        //返回值是SELF_TYPE子类要重写
        E new$SELF_TYPE() {
            return new E();
        }

        E self() {
            return this;
        }

        E set_var(int num) {
            return (E) super.set_var(num);
        }

        E method1(int num) {
            return (E) super.method1(num);
        }

        //
        A method6(int num) {
            return E$method6(num);
        }

        A E$method6(int num) {
            {
                return (new A()).set_var(num);
            }
        }
    }

    private static class A2I {
        @SuppressWarnings("unused")
        A2I new$SELF_TYPE() {
            return new A2I();
        }

        @SuppressWarnings("unused")
        A2I self() {
            return this;
        }

        int c2i(String c) {
            return A2I$c2i(c);
        }

        String i2c(int i) {
            return A2I$i2c(i);
        }

        int a2i(String s) {
            return A2I$a2i(s);
        }

        int a2i_aux(String s) {
            return A2I$a2i_aux(s);
        }

        String i2a(int i) {
            return A2I$i2a(i);
        }

        String i2a_aux(int i) {
            return A2I$i2a_aux(i);
        }

        //
        int A2I$c2i(String c) {
            if (c.equals("0")) {
                return 0;
            } else if (c.equals("1")) {
                return 1;
            } else if (c.equals("2")) {
                return 2;
            } else if (c.equals("3")) {
                return 3;
            } else if (c.equals("4")) {
                return 4;
            } else if (c.equals("5")) {
                return 5;
            } else if (c.equals("6")) {
                return 6;
            } else if (c.equals("7")) {
                return 7;
            } else if (c.equals("8")) {
                return 8;
            } else if (c.equals("9")) {
                return 9;
            } else {
                System.exit(0);
                return 0;
            }
        }

        String A2I$i2c(int i) {
            if (i == 0) {
                return "0";
            } else if (i == 1) {
                return "1";
            } else if (i == 2) {
                return "2";
            } else if (i == 3) {
                return "3";
            } else if (i == 4) {
                return "4";
            } else if (i == 5) {
                return "5";
            } else if (i == 6) {
                return "6";
            } else if (i == 7) {
                return "7";
            } else if (i == 8) {
                return "8";
            } else if (i == 9) {
                return "9";
            } else {
                System.exit(0);
                return "";
            }
        }

        int A2I$a2i(String s) {
            if (s.length() == 0) {
                return 0;
            } else if (s.substring(0, 1).equals("-")) {
                return -a2i_aux(s.substring(1, s.length() - 1));
            } else if (s.substring(0, 1).equals("+")) {
                return a2i_aux(s.substring(1, s.length() - 1));
            } else {
                return a2i_aux(s);
            }
        }

        int A2I$a2i_aux(String s) {
            {
                int in = 0;
                {
                    int j = s.length();
                    {
                        int i = 0;
                        while (i < j) {
                            in = in * 10 + c2i(s.substring(i, 1));
                            i = i + 1;
                        }
                    }
                }
                return in;
            }
        }

        String A2I$i2a(int i) {
            if (i == 0) {
                return "0";
            } else if (0 < i) {
                return i2a_aux(i);
            } else {
                return "-".concat(i2a_aux(i * -1));
            }
        }

        String A2I$i2a_aux(int i) {
            if (i == 0) {
                return "";
            } else {
                {
                    int next = i / 10;
                    return i2a_aux(next).concat(i2c(i - next * 10));
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private static class Main {
        String char$ = "";
        A avar;
        A a_var;
        boolean flag = true;

        Main class_type(A var) {
            return Main$class_type(var);
        }

        String prompt() {
            return Main$prompt();
        }

        int get_int() {
            return Main$get_int();
        }

        boolean is_even(int num) {
            return Main$is_even(num);
        }

        Main print(A var) {
            return Main$print(var);
        }

        String menu() {
            return Main$menu();
        }

        void main() {
            Main$main();
        }

        String Main$menu() {
            System.out.print("\n\tTo add a number to ");
            print(avar);
            System.out.print("...enter a:\n");
            System.out.print("\tTo negate ");
            print(avar);
            System.out.print("...enter b:\n");
            System.out.print("\tTo find the difference between ");
            print(avar);
            System.out.print("and another number...enter c:\n");
            System.out.print("\tTo find the factorial of ");
            print(avar);
            System.out.print("...enter d:\n");
            System.out.print("\tTo square ");
            print(avar);
            System.out.print("...enter e:\n");
            System.out.print("\tTo cube ");
            print(avar);
            System.out.print("...enter f:\n");
            System.out.print("\tTo find out if ");
            print(avar);
            System.out.print("is a multiple of 3...enter g:\n");
            System.out.print("\tTo divide ");
            print(avar);
            System.out.print("by 8...enter h:\n");
            System.out.print("\tTo get a new number...enter j:\n");
            System.out.print("\tTo quit...enter q:\n\n");
            print(avar);
            return prompt();
        }

        Main Main$print(A var) {
            {
                A2I z = new A2I();
                System.out.print(z.i2a(var.value()) + " ");
                return this;
            }
        }

        boolean Main$is_even(int num) {
            {
                int x = num;
                if (x < 0) {
                    return is_even(-x);
                } else if (0 == x) {
                    return true;
                } else if (1 == x) {
                    return false;
                } else {
                    return is_even(x - 2);
                }
            }
        }

        int Main$get_int() {
            {
                A2I z = new A2I();
                {
                    String s = prompt();
                    return z.a2i(s);
                }
            }
        }

        String Main$prompt() {
            System.out.print("\n");
            System.out.print("Please enter a number...  ");
            @SuppressWarnings("resource")
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNext()) return scanner.next();
            return "";
        }

        Main Main$class_type(A var) {
            if (var.getClass() == A.class) {
                System.out.println("Class type is now A");
            } else if (var.getClass() == B.class) {
                System.out.println("Class type is now B");
            } else if (var.getClass() == C.class) {
                System.out.println("Class type is now C");
            } else if (var.getClass() == D.class) {
                System.out.println("Class type is now D");
            } else if (var.getClass() == E.class) {
                System.out.println("Class type is now E");
            } else if (var.getClass() == (Class<?>) Object.class) {
                System.out.println("Oooops");
            }
            return this;
        }

        void Main$main() {
            avar = new A();
            while (flag) {
                System.out.print("number ");
                print(avar);
                if (is_even(avar.value())) {
                    System.out.print("is even!\n");
                } else {
                    System.out.print("is odd!\n");
                }
                class_type(avar);
                char$ = menu();
                if (char$.equals("a")) {
                    a_var = (new A()).set_var(get_int());
                    avar = (new B()).method2(avar.value(), a_var.value());
                } else if (char$.equals("b")) {
                    if (avar.getClass() == C.class) {
                        C c = (C) avar;
                        c.method6(c.value());
                    } else if (avar.getClass() == A.class) {
                        A a = (A) avar;
                        a.method3(a.value());
                    } else if (avar.getClass() == (Class<?>) Object.class) {
                        System.out.println("Oooops");
                        System.exit(0);
                        return;
                    }
                } else if (char$.equals("c")) {
                    a_var = (new A()).set_var(get_int());
                    avar = (new D()).method4(avar.value(), a_var.value());
                } else if (char$.equals("d")) {
                    avar = (new C()).A$method5(avar.value());
                } else if (char$.equals("e")) {
                    avar = (new C()).B$method5(avar.value());
                } else if (char$.equals("f")) {
                    avar = (new C()).C$method5(avar.value());
                } else if (char$.equals("g")) {
                    if ((new D()).method7(avar.value())) {
                        System.out.print("number ");
                        print(avar);
                        System.out.print("is divisible by 3.\n");
                    } else {
                        System.out.print("number ");
                        print(avar);
                        System.out.print("is not divisible by 3.\n");
                    }
                } else if (char$.equals("h")) {
                    {
                        A x;
                        x = (new E()).method6(avar.value());
                        {
                            int r = avar.value() - x.value() * 8;
                            System.out.print("number ");
                            print(avar);
                            System.out.print("is equal to ");
                            print(x);
                            System.out.print("times 8 with a remainder of ");
                            {
                                A2I a = new A2I();
                                System.out.println(a.i2a(r));
                            }
                        }
                        avar = x;
                    }
                } else if (char$.equals("j")) {
                    avar = new A();
                } else if (char$.equals("q")) {
                    flag = false;
                } else {
                    avar = (new A()).method1(avar.value());
                }
            }
        }
    }

    public static void main(String[] args) {
//        new Main().main();
        String str = FileUtil.readJarFile("arith.cl");
        Bootstrap.run( str);
    }
}
