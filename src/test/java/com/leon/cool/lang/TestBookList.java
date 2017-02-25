package com.leon.cool.lang;

import com.leon.cool.lang.util.FileUtil;
import net.jcip.annotations.NotThreadSafe;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;

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
 * @author leon on 15-11-1
 */
@NotThreadSafe
public class TestBookList {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @org.junit.Test
    public void test() {
        String str = FileUtil.readJarFile("book_list.cl");
        Bootstrap.run(str);
        assertEquals("title:      The Top 100 CD_ROMs\n" +
                "author:     Ulanoff\n" +
                "periodical:  PC Magazine\n" +
                "- dynamic type was Article -\n" +
                "title:      Compilers, Principles, Techniques, and Tools\n" +
                "author:     Aho, Sethi, and Ullman\n" +
                "- dynamic type was Book -\n", systemOutRule.getLog());
    }
}
