package com.leon.cool.lang.util;

import com.leon.cool.lang.support.declaration.MethodDeclaration;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Baoyi Chen on 2017/2/24.
 */
public class StringUtil {
    public static <T> String mkString(List<T> tks, String split) {
        return mkString(tks, Optional.empty(), split, Optional.empty());
    }

    public static <T> String mkString(List<T> tks, Optional<String> beforeOpt, String split, Optional<String> endOpt) {
        StringBuilder sb = new StringBuilder();
        if (beforeOpt.isPresent()) sb.append(beforeOpt.get());
        sb.append(tks.stream().map(T::toString).collect(Collectors.joining(split)));
        if (endOpt.isPresent()) sb.append(endOpt.get());
        return sb.toString();
    }

    public static String constructMethod(String id, List<String> params) {
        return id + StringUtil.mkString(params, Optional.of("("), ",", Optional.of(")"));
    }

    public static String constructMethod(MethodDeclaration methodDeclaration) {
        return constructMethod(methodDeclaration.methodName, methodDeclaration.paramTypes);
    }
}
