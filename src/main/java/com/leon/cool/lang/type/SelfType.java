package com.leon.cool.lang.type;

import com.leon.cool.lang.factory.TypeFactory;
import com.leon.cool.lang.support.Utils;

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
 * @author leon on 15-10-16
 */
public class SelfType implements Type {

    private final String type;

    public SelfType(String type) {
        this.type = type;
    }

    public Type replace() {
        return TypeFactory.objectType(type);
    }

    @Override
    public String className() {
        Utils.error("unexpected.error");
        return toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SelfType)) return false;

        SelfType selfType = (SelfType) o;

        return !(type != null ? !type.equals(selfType.type) : selfType.type != null);

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    @Override
    public TypeEnum type() {
        return TypeEnum.SELF_TYPE;
    }

    @Override
    public String toString() {
        return "SELF_TYPE(" + type + ')';
    }
}
