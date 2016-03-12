package com.leon.cool.lang.support;

import org.junit.Test;

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
public class TestUtils {
    @Test
    public void testMessage(){
        assertEquals("Contains \\b \\n \\t \\f at row 10 column 15.",Utils.errorMsg("tokenizer.error.illegal.char",10+"",15+""));
    }
}