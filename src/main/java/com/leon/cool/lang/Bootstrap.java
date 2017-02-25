package com.leon.cool.lang;

import com.leon.cool.lang.ast.Program;
import com.leon.cool.lang.factory.TreeFactory;
import com.leon.cool.lang.parser.CoolParser;
import com.leon.cool.lang.support.TreeSupport;
import com.leon.cool.lang.support.infrastructure.Context;
import com.leon.cool.lang.tokenizer.CoolScanner;
import com.leon.cool.lang.tokenizer.CoolTokenizer;
import com.leon.cool.lang.tree.*;
import com.leon.cool.lang.util.FileUtil;

/**
 * Copyright leon
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author leon on 15-10-8
 */
public class Bootstrap {

    public static void run(String str) {
        try (TreeSupport treeSupport = new TreeSupport()) {
            //compile
            CoolTokenizer tokenizer = new CoolTokenizer(str.toCharArray());
            CoolScanner scanner = new CoolScanner(tokenizer);
            CoolParser parser = new CoolParser(scanner, new TreeFactory());
            Program expr = parser.parseProgram();
            if (!parser.errMsgs.isEmpty()) {
                parser.errMsgs.forEach(System.err::println);
                return;
            }
            TypeCheckTreeScanner typeCheckTreeScanner;
            expr.accept(new ClassGraphTreeScanner(treeSupport));
            expr.accept(new MethodDefTreeScanner(treeSupport));
            expr.accept(new ParentMethodDefTreeScanner(treeSupport));
            expr.accept(new AttrDefTreeScanner(treeSupport));
            expr.accept(new ParentAttrDefTreeScanner(treeSupport));
            expr.accept(typeCheckTreeScanner = new TypeCheckTreeScanner(treeSupport));
            if (!typeCheckTreeScanner.errMsgs.isEmpty()) {
                typeCheckTreeScanner.errMsgs.forEach(System.err::println);
                return;
            }
            //expr.accept(new PrintTypeInfoTreeScanner());
            //runtime
            expr.accept(new EvalTreeScanner(treeSupport), new Context(null, null));
        }
    }

    public static void main(String[] args) {
        String str = FileUtil.readFile(args[0]);
        Bootstrap.run(str);
    }
}
