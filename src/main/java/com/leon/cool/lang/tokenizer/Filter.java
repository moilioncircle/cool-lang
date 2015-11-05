package com.leon.cool.lang.tokenizer;

/**
 * Created by leon on 15-10-8.
 */
public interface Filter<T> {
    boolean accepts(T t);
}
