package com.leon.cool.lang.factory;

import com.leon.cool.lang.Constant;
import com.leon.cool.lang.type.*;

import static com.leon.cool.lang.support.ErrorSupport.error;

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
public class TypeFactory {
    public static Type stringType() {
        return new StringType();
    }

    public static Type booleanType() {
        return new BoolType();
    }

    public static Type integerType() {
        return new IntType();
    }

    public static Type objectType(String type, String className) {
        switch (type) {
            case Constant.STRING:
                return stringType();
            case Constant.INT:
                return integerType();
            case Constant.BOOL:
                return booleanType();
            case Constant.SELF_TYPE:
                return selfType(className);
            default:
                return new ObjectType(type);
        }
    }

    public static Type objectType(String type) {
        switch (type) {
            case Constant.STRING:
                return stringType();
            case Constant.INT:
                return integerType();
            case Constant.BOOL:
                return booleanType();
            case Constant.SELF_TYPE:
                error("unexpected.error");
            default:
                return new ObjectType(type);
        }
    }

    public static Type selfType(String className) {
        return new SelfType(className);
    }

    public static Type noType() {
        return new NoType();
    }

    public static Type voidType() {
        return new VoidType();
    }
}
