class Main inherits IO {
    main() : SELF_TYPE {
    {
        out_string((new object).type_name().substr(4,1)).
        out_string((isvoid self).type_name().substr(1,3));
        out_string("\n");
    }
    };

    main1() : SELF_TYPE {
        {
            out_string((new object).type_name().substr(4,1)).
            out_string((isvoid self).type_name().substr(1,3));
            out_string("\n");
        }
        };
};
