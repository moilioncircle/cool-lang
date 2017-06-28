package com.leon.cool.lang.util;

import com.leon.cool.lang.glossary.Nullable;
import com.leon.cool.lang.support.declaration.MethodDeclaration;

import java.util.List;
import java.util.stream.Collectors;

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
 * @author leon on 17-2-24
 */
public class StringUtil {

    private StringUtil() {
    }

    public static <T> String mkString(List<T> tks, String split) {
        return mkString(tks, null, split, null);
    }

    public static <T> String mkString(List<T> tks, @Nullable String prefix, String split, @Nullable String suffix) {
        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";
        return tks.stream().map(T::toString).collect(Collectors.joining(split, prefix, suffix));
    }

    public static String constructMethod(String id, List<String> params) {
        return id + mkString(params, "(", ",", ")");
    }

    public static String constructMethod(MethodDeclaration methodDeclaration) {
        return constructMethod(methodDeclaration.methodName, methodDeclaration.paramTypes);
    }
}
