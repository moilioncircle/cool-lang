package com.leon.cool.lang.support.infrastructure;

import com.leon.cool.lang.ast.BoolConst;
import com.leon.cool.lang.ast.IntConst;
import com.leon.cool.lang.ast.StringConst;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.tokenizer.TokenKind;

import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;

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
 * @author leon on 15-10-28
 */
public class ConstantPool {

    public static int STRING_INDEX = 0;

    public static int INT_INDEX = 0;

    private Map<Integer, IntConst> intPool = new LinkedHashMap<>();

    private Map<String, StringConst> stringPool = new LinkedHashMap<>();

    private static final ConstantPool INSTANCE = new ConstantPool();

    public static final BoolConst falseBool = new BoolConst(false);
    public static final BoolConst trueBool = new BoolConst(true);

    public static ConstantPool getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE.addInt(0);
        INSTANCE.addString("");
        INSTANCE.addBool(false);
        INSTANCE.addBool(true);
    }

    public IntConst addInt(int i) {
        if (intPool.containsKey(i)) {
            return intPool.get(i);
        }
        IntConst intConst = new IntConst(new Token(String.valueOf(i), TokenKind.INTEGER, Pos.constPos));
        intConst.index = INT_INDEX++;
        intPool.put(i, intConst);
        return intConst;
    }

    public StringConst addString(String str) {
        if (stringPool.containsKey(str)) {
            return stringPool.get(str);
        }
        StringConst stringConst = new StringConst(new Token(str, TokenKind.STRING, Pos.constPos));
        stringConst.index = STRING_INDEX++;
        stringPool.put(str, stringConst);
        return stringConst;
    }

    public BoolConst addBool(boolean bool) {
        return bool ? trueBool : falseBool;
    }

    public void genConstantPool(final PrintStream s) {
        stringPool.values().stream().forEach(e -> e.codeDef(s));
        intPool.values().stream().forEach(e -> e.codeDef(s));
        falseBool.codeDef(s);
        trueBool.codeDef(s);
    }

    public void clear() {
        STRING_INDEX = 0;
        INT_INDEX = 0;
    }
}
