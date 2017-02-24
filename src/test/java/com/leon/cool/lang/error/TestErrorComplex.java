package com.leon.cool.lang.error;

import com.leon.cool.lang.Bootstrap;
import com.leon.cool.lang.util.FileUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;

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
 * @author leon on 15-11-6
 */
public class TestErrorComplex {
    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    @Test
    public void test() {
        String str = FileUtil.readJarFile("error/complex.cl");
        Bootstrap.run("complex.cl", str);
        assertEquals("Type check error.Type Complex is not same as Int  at Pos{row=4, column=9} to Pos{row=4, column=50}.\r\n" +
                "Type check error.Expected Bool but actual NoType  at Pos{row=4, column=9} to Pos{row=4, column=50}.\r\n" +
                "Type check error.Type String is not same as Int  at Pos{row=19, column=6} to Pos{row=19, column=11}.\r\n" +
                "Type check error.Type String is not same as Int  at Pos{row=25, column=5} to Pos{row=25, column=10}.\r\n" +
                "Class Complex method out_int(String) not defined error  at Pos{row=27, column=34} to Pos{row=27, column=41}.\r\n" +
                "Class NoType method out_string(String) not defined error  at Pos{row=27, column=45} to Pos{row=27, column=55}.\r\n" +
                "Type check error.Expected Bool but actual NoType  at Pos{row=25, column=5} to Pos{row=25, column=10}.\r\n" +
                "Type check error.Expected Int but actual String  at Pos{row=34, column=11} to Pos{row=34, column=12}.\r\n" +
                "Type check error.Type String is not same as NoType  at Pos{row=34, column=6} to Pos{row=34, column=12}.\r\n" +
                "Type check error.Type SELF_TYPE(Complex) is not subclass of Int  at Pos{row=31, column=19} to Pos{row=31, column=22}.\r\n" +
                "Type check error.Expected Int but actual String  at Pos{row=41, column=11} to Pos{row=41, column=12}.\r\n" +
                "Type check error.Type String is not same as NoType  at Pos{row=41, column=6} to Pos{row=41, column=12}.\r\n", systemErrRule.getLog());
    }
}
