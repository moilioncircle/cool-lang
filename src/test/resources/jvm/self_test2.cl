class A inherits IO {
    test() : SELF_TYPE {
        let a : SELF_TYPE <- (new SELF_TYPE) in {
            {
                a.println();
                a.println1();
                a@A.println();
            };
        }
    };

    println1() :SELF_TYPE {
        out_string("belongs A\n")
    };

    println() : SELF_TYPE {
        out_string("hello\n")
    };
};

class B inherits A {
    println() : SELF_TYPE {
        out_string("world\n")
    };
};

class Main {
    main() : Object {
        {
            (new A).test();
            (new B).test();
        }
    };
};