package com.leon.cool.lang.support;

import com.leon.cool.lang.ast.ClassDef;
import com.leon.cool.lang.ast.Expression;
import com.leon.cool.lang.ast.Feature;
import com.leon.cool.lang.factory.TreeFactory;
import com.leon.cool.lang.tokenizer.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.leon.cool.lang.tokenizer.TokenKind.ID;
import static com.leon.cool.lang.tokenizer.TokenKind.TYPE;

public class ClassTable {
    private final TreeFactory f = new TreeFactory();

    /**
     * @see com.leon.cool.lang.ast.StaticDispatch
     * @see com.leon.cool.lang.ast.Dispatch
     *
     * 初始化build-in 类
     * Object:
     *     abort():Object
     *     type_name():String
     *     copy():SELF_TYPE
     * IO:
     *     out_string(String):SELF_TYPE
     *     out_int(Int):SELF_TYPE
     *     in_string():String
     *     in_int():Int
     * Int:
     *     val:Int
     * Bool:
     *     val:Bool
     * String：
     *     val:String
     *     str_field:String
     *     length():Int
     *     concat(String):String
     *     substr(Int,Int):String
     * @return
     */
    public List<ClassDef> installBasicClasses() {
        List<ClassDef> classDefs = new ArrayList<>();
        List<Feature> features = new ArrayList<>();
        features.add(f.methodDef(new Token("abort", ID), Arrays.asList(), new Token("Object", TYPE), f.noExpression()));
        features.add(f.methodDef(new Token("type_name", ID), Arrays.asList(), new Token("String", TYPE), f.noExpression()));
        features.add(f.methodDef(new Token("copy", ID), Arrays.asList(), new Token("SELF_TYPE", TYPE), f.noExpression()));
        ClassDef objectDef = f.classDef(new Token("Object", TYPE), Optional.<Token>empty(), features);
        classDefs.add(objectDef);

        features = new ArrayList<>();
        features.add(f.methodDef(new Token("out_string", ID), Arrays.asList(f.formal(new Token("arg", ID), new Token("String", TYPE))), new Token("SELF_TYPE", TYPE), f.noExpression()));
        features.add(f.methodDef(new Token("out_int", ID), Arrays.asList(f.formal(new Token("arg", ID), new Token("Int", TYPE))), new Token("SELF_TYPE", TYPE), f.noExpression()));
        features.add(f.methodDef(new Token("in_string", ID), Arrays.asList(), new Token("String", TYPE), f.noExpression()));
        features.add(f.methodDef(new Token("in_int", ID), Arrays.asList(), new Token("Int", TYPE), f.noExpression()));
        ClassDef ioDef = f.classDef(new Token("IO", TYPE), Optional.of(new Token("Object", TYPE)), features);
        classDefs.add(ioDef);

        features = new ArrayList<>();
        features.add(f.attrDef(new Token("val", ID), new Token("Int", TYPE), Optional.<Expression>empty()));
        ClassDef intDef = f.classDef(new Token("Int", TYPE), Optional.of(new Token("Object", TYPE)), features);
        classDefs.add(intDef);

        features = new ArrayList<>();
        features.add(f.attrDef(new Token("val", ID), new Token("Bool", TYPE), Optional.<Expression>empty()));
        ClassDef boolDef = f.classDef(new Token("Bool", TYPE), Optional.of(new Token("Object", TYPE)), features);
        classDefs.add(boolDef);

        features = new ArrayList<>();
        features.add(f.attrDef(new Token("val", ID), new Token("Int", TYPE), Optional.<Expression>empty()));
        features.add(f.attrDef(new Token("str_field", ID), new Token("String", TYPE), Optional.<Expression>empty()));
        features.add(f.methodDef(new Token("length", ID), Arrays.asList(), new Token("Int", TYPE), f.noExpression()));
        features.add(f.methodDef(new Token("concat", ID), Arrays.asList(f.formal(new Token("arg", ID), new Token("String", TYPE))), new Token("String", TYPE), f.noExpression()));
        features.add(f.methodDef(new Token("substr", ID), Arrays.asList(f.formal(new Token("arg", ID), new Token("Int", TYPE)), f.formal(new Token("arg2", ID), new Token("Int", TYPE))), new Token("String", TYPE), f.noExpression()));
        ClassDef stringDef = f.classDef(new Token("String", TYPE), Optional.of(new Token("Object", TYPE)), features);
        classDefs.add(stringDef);
        return classDefs;
    }
}



