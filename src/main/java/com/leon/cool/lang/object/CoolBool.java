package com.leon.cool.lang.object;

import static com.leon.cool.lang.factory.ObjectFactory.coolBool;
import static com.leon.cool.lang.factory.TypeFactory.booleanType;

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
public class CoolBool extends CoolObject {
    public boolean val = false;

    public CoolBool(boolean val) {
        this();
        this.val = val;
    }

    public CoolBool() {
        this.type = booleanType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoolBool)) return false;

        CoolBool coolBool = (CoolBool) o;

        return val == coolBool.val;
    }

    @Override
    public int hashCode() {
        return (val ? 1 : 0);
    }

    @Override
    public CoolBool copy() {
        return coolBool(this.val);
    }
}
