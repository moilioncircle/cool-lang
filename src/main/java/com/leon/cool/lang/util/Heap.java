package com.leon.cool.lang.util;

import com.leon.cool.lang.object.CoolObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
 * @author leon on 16-3-13
 */
public class Heap {
    private static Map<CoolObject, Boolean> heap = new HashMap<>();

    public static void add(CoolObject obj) {
        System.out.println("$$add to heap:"+obj.type+"@"+obj.toString());
        heap.put(obj, false);
    }

    public static void canReach(CoolObject obj) {
        if (heap.containsKey(obj)) {
            heap.put(obj, true);
        }
        System.out.println("$$can reach not found:"+obj.type+"@"+obj.toString());
    }

    public static boolean isReach(CoolObject obj){
        if(heap.containsKey(obj)){
            return heap.get(obj);
        }
        System.out.println("$$is reach not found:"+obj.type+"@"+obj.toString());
        return true;
    }

    /**
     * 删除不可达对象，并将可达对象重新设置成false
     */
    public static void clearUnreachable() {
        System.out.println("total object size:"+heap.size());
        Set<Map.Entry<CoolObject, Boolean>> sets = heap.entrySet().stream().filter(e -> e.getValue()).collect(Collectors.toSet());
        System.out.println("reachable object size:"+sets.size());
        heap = new HashMap<>();
        sets.stream().forEach(e -> heap.put(e.getKey(), false));
    }

    public static void dump() {
        heap.entrySet().stream().forEach(System.out::println);
    }

    public static int size() {
        return heap.size();
    }

    public static void clear() {
        heap.clear();
    }
}
