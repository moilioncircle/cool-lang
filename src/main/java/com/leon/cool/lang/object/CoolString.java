package com.leon.cool.lang.object;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.factory.TypeFactory;
import com.leon.cool.lang.support.Utils;

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
 * @author leon on 15-10-21
 */
public class CoolString extends CoolObject {
    public String str = "";
    private int length = 0;

    public CoolString(String str, int length) {
        this();
        this.str = str;
        this.length = length;
    }

    public CoolString() {
        this.type = TypeFactory.stringType();
    }

    public CoolInt length() {
        return new CoolInt(length);
    }

    public CoolString concat(CoolString s) {
        return ObjectFactory.coolString(this.str.concat(s.str));
    }

    public CoolString substr(CoolInt i, CoolInt l, String pos) {
        try {
            String str = this.str.substring(i.val, i.val + l.val);
            return ObjectFactory.coolString(str);
        } catch (StringIndexOutOfBoundsException e) {
            Utils.error("runtime.error.range", pos);
        }
        return ObjectFactory.coolStringDefault();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoolString)) return false;

        CoolString that = (CoolString) o;

        return length == that.length && !(str != null ? !str.equals(that.str) : that.str != null);

    }

    @Override
    public int hashCode() {
        int result = str != null ? str.hashCode() : 0;
        result = 31 * result + length;
        return result;
    }

    public CoolString copy() {
        return ObjectFactory.coolString(this.str);
    }

    @Override
    public String toString() {
        return "CoolString{" +
                "str='" + str + '\'' +
                ", length=" + length +
                '}';
    }
}
