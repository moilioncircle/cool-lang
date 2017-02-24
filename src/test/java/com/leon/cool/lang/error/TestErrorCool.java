package com.leon.cool.lang.error;

import com.leon.cool.lang.Bootstrap;
import com.leon.cool.lang.util.FileUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;

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
 * @author leon on 15-11-6
 */
public class TestErrorCool {
    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    @Test
    public void test() {
        String str = FileUtil.readJarFile("error/cool.cl");
        Bootstrap.run("cool.cl", str);
        assertEquals("Expected Type but actual ID at Pos{row=4, column=25}.\r\n" +
                "Expected Type but actual ID at Pos{row=12, column=29}.\r\n", systemErrRule.getLog());
    }
}
