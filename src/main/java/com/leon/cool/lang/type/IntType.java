package com.leon.cool.lang.type;

import static com.leon.cool.lang.Constant.INT;

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
 * @author leon on 15-10-14
 */
public class IntType implements Type {

    @Override
    public TypeEnum type() {
        return TypeEnum.INT;
    }

    @Override
    public String className() {
        return toString();
    }

    @Override
    public String toString() {
        return INT;
    }
}
