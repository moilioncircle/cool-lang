package com.leon.cool.lang.object;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.factory.TypeFactory;

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
 * @author leon on 15-10-21
 */
public class CoolInt extends CoolObject {
    public int val = 0;

    public CoolInt(int val) {
        this();
        this.val = val;
    }

    public CoolInt() {
        this.type = TypeFactory.integerType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoolInt)) return false;

        CoolInt coolInt = (CoolInt) o;

        return val == coolInt.val;
    }

    @Override
    public int hashCode() {
        return val;
    }

    @Override
    public CoolInt copy() {
        return ObjectFactory.coolInt(this.val);
    }
}
