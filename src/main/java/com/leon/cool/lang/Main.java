package com.leon.cool.lang;

import com.leon.cool.lang.ast.Program;
import com.leon.cool.lang.factory.TreeFactory;
import com.leon.cool.lang.parser.CoolParser;
import com.leon.cool.lang.support.Utils;
import com.leon.cool.lang.tokenizer.CoolScanner;
import com.leon.cool.lang.tokenizer.CoolTokenizer;
import com.leon.cool.lang.tree.*;
import com.leon.cool.lang.util.FileUtils;

/**
 * Created by leon on 15-10-8.
 */
public class Main {
    public static void run(String str) {
        CoolTokenizer tokenizer = new CoolTokenizer(str.toCharArray());
        CoolScanner scanner = new CoolScanner(tokenizer);
        CoolParser parser = new CoolParser(scanner, new TreeFactory());
        Program expr = parser.parseProgram();
        if (!parser.errMsgs.isEmpty()) {
            parser.errMsgs.forEach(System.err::println);
            return;
        }
        expr.accept(new ClassGraphTreeScanner());
        expr.accept(new MethodDefTreeScanner());
        expr.accept(new ParentMethodDefTreeScanner());
        expr.accept(new AttrDefTreeScanner());
        expr.accept(new ParentAttrDefTreeScanner());
        TypeCheckTreeScanner typeCheckTreeScanner = new TypeCheckTreeScanner();
        expr.accept(typeCheckTreeScanner);
        if (!typeCheckTreeScanner.errMsgs.isEmpty()) {
            typeCheckTreeScanner.errMsgs.forEach(System.err::println);
            return;
        }
        //expr.accept(new PrintTypeInfoTreeScanner());
        expr.run();
        Utils.clear();
        Utils.close();
    }

    public static void main(String[] args) {
        System.out.println(args[0]);
        String str = FileUtils.readFile(args[0]);
        Main.run(str);
    }
}
