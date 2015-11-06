package com.leon.cool.lang;

import com.leon.cool.lang.util.FileUtils;
import net.jcip.annotations.NotThreadSafe;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import static org.junit.Assert.assertEquals;

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
 * @author leon on 15-11-1
 */
@NotThreadSafe
public class TestArith {
    @Rule
    public final TextFromStandardInputStream systemInMock = TextFromStandardInputStream.emptyStandardInputStream();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @org.junit.Test
    public void test() {
        systemInMock.provideText("q");
        String str = FileUtils.readJarFile("arith.cl");
        Main.run(str);
        assertEquals("number 0 is even!\n" +
                "Class type is now A\n" +
                "\n" +
                "\tTo add a number to 0 ...enter a:\n" +
                "\tTo negate 0 ...enter b:\n" +
                "\tTo find the difference between 0 and another number...enter c:\n" +
                "\tTo find the factorial of 0 ...enter d:\n" +
                "\tTo square 0 ...enter e:\n" +
                "\tTo cube 0 ...enter f:\n" +
                "\tTo find out if 0 is a multiple of 3...enter g:\n" +
                "\tTo divide 0 by 8...enter h:\n" +
                "\tTo get a new number...enter j:\n" +
                "\tTo quit...enter q:\n" +
                "\n", systemOutRule.getLog());
    }
}
