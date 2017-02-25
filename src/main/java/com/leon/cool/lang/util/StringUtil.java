package com.leon.cool.lang.util;

import com.leon.cool.lang.glossary.Nullable;
import com.leon.cool.lang.support.declaration.MethodDeclaration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Baoyi Chen on 2017/2/24.
 */
public class StringUtil {

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
