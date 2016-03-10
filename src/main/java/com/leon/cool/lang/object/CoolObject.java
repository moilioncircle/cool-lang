package com.leon.cool.lang.object;

import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.factory.TypeFactory;
import com.leon.cool.lang.support.Env;
import com.leon.cool.lang.support.Utils;
import com.leon.cool.lang.type.Type;
import com.leon.cool.lang.util.Constant;

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
public class CoolObject {
    public Type type = TypeFactory.objectType(Constant.OBJECT);

    /**
     * 对象中的环境
     */
    public Env env = new Env();

    public CoolObject() {

    }

    public CoolObject abort() {
        System.out.println(this.type + " abort and exit.");
        Utils.clear();
        Utils.close();
        System.exit(0);
        return this;
    }

    public CoolObject copy() {
        CoolObject object = ObjectFactory.coolObject();
        object.type = this.type;
        object.env = this.env;
        return object;
    }
}
