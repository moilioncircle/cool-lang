package com.leon.cool.lang.tokenizer;

import com.leon.cool.lang.util.FileUtil;
import net.jcip.annotations.NotThreadSafe;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
 * @author leon on 15-11-2
 */
@NotThreadSafe
public class TestTokenizer {
    @Test
    public void test() {
        String str = FileUtil.readJarFile("tokenizer/tokenizer.cl");
        CoolTokenizer tokenizer = new CoolTokenizer(str.toCharArray());

        CoolScanner scanner = new CoolScanner(tokenizer);
        scanner.nextToken();
        Token token = scanner.token();
        StringBuilder sb = new StringBuilder();
        while (token.kind != TokenKind.EOF) {
            sb.append(token.toString());
            sb.append("\n");
            scanner.nextToken();
            token = scanner.token();
        }
        assertEquals("Token{name='class', kind='class', startPos=Pos{row=1, column=1}, endPos=Pos{row=1, column=6}}\n" +
                "Token{name='Main', kind=Type, startPos=Pos{row=1, column=7}, endPos=Pos{row=1, column=11}}\n" +
                "Token{name='inherits', kind='inherits', startPos=Pos{row=1, column=12}, endPos=Pos{row=1, column=20}}\n" +
                "Token{name='IO', kind=Type, startPos=Pos{row=1, column=21}, endPos=Pos{row=1, column=23}}\n" +
                "Token{name='{', kind='{', startPos=Pos{row=1, column=24}, endPos=Pos{row=1, column=25}}\n" +
                "Token{name='main', kind=ID, startPos=Pos{row=3, column=5}, endPos=Pos{row=3, column=9}}\n" +
                "Token{name='(', kind='(', startPos=Pos{row=3, column=9}, endPos=Pos{row=3, column=10}}\n" +
                "Token{name=')', kind=')', startPos=Pos{row=3, column=10}, endPos=Pos{row=3, column=11}}\n" +
                "Token{name=':', kind=':', startPos=Pos{row=3, column=12}, endPos=Pos{row=3, column=13}}\n" +
                "Token{name='Object', kind=Type, startPos=Pos{row=3, column=14}, endPos=Pos{row=3, column=20}}\n" +
                "Token{name='{', kind='{', startPos=Pos{row=3, column=21}, endPos=Pos{row=3, column=22}}\n" +
                "Token{name='self', kind=ID, startPos=Pos{row=5, column=9}, endPos=Pos{row=5, column=13}}\n" +
                "Token{name='.', kind='.', startPos=Pos{row=5, column=13}, endPos=Pos{row=5, column=14}}\n" +
                "Token{name='out_string', kind=ID, startPos=Pos{row=5, column=14}, endPos=Pos{row=5, column=24}}\n" +
                "Token{name='(', kind='(', startPos=Pos{row=5, column=24}, endPos=Pos{row=5, column=25}}\n" +
                "Token{name='Hello, world!', kind=String, startPos=Pos{row=5, column=25}, endPos=Pos{row=5, column=38}}\n" +
                "Token{name=')', kind=')', startPos=Pos{row=5, column=40}, endPos=Pos{row=5, column=41}}\n" +
                "Token{name='.', kind='.', startPos=Pos{row=5, column=41}, endPos=Pos{row=5, column=42}}\n" +
                "Token{name='out_string', kind=ID, startPos=Pos{row=5, column=42}, endPos=Pos{row=5, column=52}}\n" +
                "Token{name='(', kind='(', startPos=Pos{row=5, column=52}, endPos=Pos{row=5, column=53}}\n" +
                "Token{name=' chen bao yi\n" +
                "', kind=String, startPos=Pos{row=5, column=53}, endPos=Pos{row=5, column=66}}\n" +
                "Token{name=')', kind=')', startPos=Pos{row=5, column=69}, endPos=Pos{row=5, column=70}}\n" +
                "Token{name='}', kind='}', startPos=Pos{row=6, column=5}, endPos=Pos{row=6, column=6}}\n" +
                "Token{name=';', kind=';', startPos=Pos{row=6, column=6}, endPos=Pos{row=6, column=7}}\n" +
                "Token{name='}', kind='}', startPos=Pos{row=7, column=1}, endPos=Pos{row=7, column=2}}\n" +
                "Token{name=';', kind=';', startPos=Pos{row=7, column=2}, endPos=Pos{row=7, column=3}}\n", sb.toString());
    }
}
