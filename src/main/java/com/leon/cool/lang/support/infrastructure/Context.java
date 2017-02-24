package com.leon.cool.lang.support.infrastructure;

import com.leon.cool.lang.object.CoolObject;

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
 * @author leon on 15-10-28
 */
public class Context {

    public final CoolObject selfObject;

    public final SymbolTable<CoolObject> environment;

    public Context(CoolObject selfObject, SymbolTable<CoolObject> environment) {
        this.selfObject = selfObject;
        this.environment = environment;
    }
}