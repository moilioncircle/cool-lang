package com.leon.cool.lang.util;

import com.leon.cool.lang.support.Dumpable;

import java.util.ArrayList;
import java.util.List;

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
 * @author leon on 15-10-11
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
