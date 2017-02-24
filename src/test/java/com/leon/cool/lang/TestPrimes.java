package com.leon.cool.lang;

import com.leon.cool.lang.util.FileUtil;
import net.jcip.annotations.NotThreadSafe;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemOutRule;

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
public class TestPrimes {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void test() {
        exit.expectSystemExit();
        String str = FileUtil.readJarFile("primes.cl");
        Bootstrap.run("primes.cl", str);
        assertEquals("2 is trivially prime.\n" +
                "3 is prime.\n" +
                "5 is prime.\n" +
                "7 is prime.\n" +
                "11 is prime.\n" +
                "13 is prime.\n" +
                "17 is prime.\n" +
                "19 is prime.\n" +
                "23 is prime.\n" +
                "29 is prime.\n" +
                "31 is prime.\n" +
                "37 is prime.\n" +
                "41 is prime.\n" +
                "43 is prime.\n" +
                "47 is prime.\n" +
                "53 is prime.\n" +
                "59 is prime.\n" +
                "61 is prime.\n" +
                "67 is prime.\n" +
                "71 is prime.\n" +
                "73 is prime.\n" +
                "79 is prime.\n" +
                "83 is prime.\n" +
                "89 is prime.\n" +
                "97 is prime.\n" +
                "101 is prime.\n" +
                "103 is prime.\n" +
                "107 is prime.\n" +
                "109 is prime.\n" +
                "113 is prime.\n" +
                "127 is prime.\n" +
                "131 is prime.\n" +
                "137 is prime.\n" +
                "139 is prime.\n" +
                "149 is prime.\n" +
                "151 is prime.\n" +
                "157 is prime.\n" +
                "163 is prime.\n" +
                "167 is prime.\n" +
                "173 is prime.\n" +
                "179 is prime.\n" +
                "181 is prime.\n" +
                "191 is prime.\n" +
                "193 is prime.\n" +
                "197 is prime.\n" +
                "199 is prime.\n" +
                "211 is prime.\n" +
                "223 is prime.\n" +
                "227 is prime.\n" +
                "229 is prime.\n" +
                "233 is prime.\n" +
                "239 is prime.\n" +
                "241 is prime.\n" +
                "251 is prime.\n" +
                "257 is prime.\n" +
                "263 is prime.\n" +
                "269 is prime.\n" +
                "271 is prime.\n" +
                "277 is prime.\n" +
                "281 is prime.\n" +
                "283 is prime.\n" +
                "293 is prime.\n" +
                "307 is prime.\n" +
                "311 is prime.\n" +
                "313 is prime.\n" +
                "317 is prime.\n" +
                "331 is prime.\n" +
                "337 is prime.\n" +
                "347 is prime.\n" +
                "349 is prime.\n" +
                "353 is prime.\n" +
                "359 is prime.\n" +
                "367 is prime.\n" +
                "373 is prime.\n" +
                "379 is prime.\n" +
                "383 is prime.\n" +
                "389 is prime.\n" +
                "397 is prime.\n" +
                "401 is prime.\n" +
                "409 is prime.\n" +
                "419 is prime.\n" +
                "421 is prime.\n" +
                "431 is prime.\n" +
                "433 is prime.\n" +
                "439 is prime.\n" +
                "443 is prime.\n" +
                "449 is prime.\n" +
                "457 is prime.\n" +
                "461 is prime.\n" +
                "463 is prime.\n" +
                "467 is prime.\n" +
                "479 is prime.\n" +
                "487 is prime.\n" +
                "491 is prime.\n" +
                "499 is prime.\n", systemOutRule.getLog());
    }
}
