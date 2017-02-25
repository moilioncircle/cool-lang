package com.leon.cool.lang;

import com.leon.cool.lang.util.FileUtil;
import net.jcip.annotations.NotThreadSafe;
import org.junit.Rule;
import org.junit.Test;
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
public class TestHairyscary {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void test() {
        String str = FileUtil.readJarFile("hairyscary.cl");
        Bootstrap.run(str);
        assertEquals("17141611714163171416511714161171416317141653117141611714163171416511714161171416317141653171416117141631714165171416", systemOutRule.getLog());
    }

}
