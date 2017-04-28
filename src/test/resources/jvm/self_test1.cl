class A inherits IO {
    a : SELF_TYPE <- self;

    test() : SELF_TYPE {
        a
    };

    test1() : SELF_TYPE {
        self
    };

    test2() : SELF_TYPE {
        (new SELF_TYPE)
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

    println2() : SELF_TYPE {
            out_string("chen bao yi\n")
    };
};

class Main {
    main() : Object {
        {
            (new B).test().println2();
            (new B).test1().println2();
            (new B).test2().println2();
        }
    };
};