package com.leon.cool.lang.factory;

import com.leon.cool.lang.object.*;

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
public class ObjectFactory {

    private ObjectFactory() {
    }

    public static CoolInt coolInt(int i) {
        return new CoolInt(i);
    }

    public static CoolInt coolIntDefault() {
        return new CoolInt();
    }

    public static CoolString coolString(String i) {
        return new CoolString(i, i.length());
    }

    public static CoolString coolStringDefault() {
        return new CoolString();
    }

    public static CoolBool coolBool(boolean i) {
        return new CoolBool(i);
    }

    public static CoolBool coolBoolDefault() {
        return new CoolBool();
    }

    public static CoolObject coolObject() {
        return new CoolObject();
    }

    public static CoolVoid coolVoid() {
        return new CoolVoid();
    }
}
