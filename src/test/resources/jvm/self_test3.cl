class A inherits IO {
    test() : Int {
        {
            let a : SELF_TYPE <- (new SELF_TYPE) in {
                {
                    a.println();
                };
            };
            0;
        }
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