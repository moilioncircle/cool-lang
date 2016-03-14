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
 * Copyright leon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author leon on 15-10-8
 */
public class Main {
    public static void run(String str) {
        try{
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
        }finally {
            Utils.clear();
            Utils.close();
        }

    }

    public static void main(String[] args) {
        System.out.println(args[0]);
        String str = FileUtils.readFile(args[0]);
        Main.run(str);
    }
}
