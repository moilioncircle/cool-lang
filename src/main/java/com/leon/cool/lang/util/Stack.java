package com.leon.cool.lang.util;

import com.leon.cool.lang.support.Dumpable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 15-10-11.
 */
public class Stack<T> implements Dumpable {
    private final List<T> list = new ArrayList<>();

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }

    public T push(T t) {
        list.add(0, t);
        return t;
    }

    public T elementAt(int i) {
        return list.get(i);
    }

    public T pop() {
        if (isEmpty()) {
            return null;
        }

        return list.remove(0);
    }

    public T top() {
        if (isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void dump() {
        list.forEach(System.out::println);
    }
}
