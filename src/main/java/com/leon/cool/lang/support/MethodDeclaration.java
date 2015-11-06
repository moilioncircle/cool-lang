package com.leon.cool.lang.support;

import com.leon.cool.lang.ast.MethodDef;

import java.util.ArrayList;
import java.util.List;

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
 * @author leon on 15-10-15
 */
public class MethodDeclaration {
    public String methodName;
    public List<String> paramTypes = new ArrayList<>();
    public String returnType;
    public MethodDef declaration;
    public String belongs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodDeclaration)) return false;

        MethodDeclaration that = (MethodDeclaration) o;

        return !(methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) && !(paramTypes != null ? !paramTypes.toString().equals(that.paramTypes.toString()) : that.paramTypes != null);

    }

    @Override
    public int hashCode() {
        int result = methodName != null ? methodName.hashCode() : 0;
        result = 31 * result + (paramTypes != null ? paramTypes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MethodDeclaration{" +
                "methodName='" + methodName + '\'' +
                ", paramTypes=" + paramTypes +
                ", returnType='" + returnType + '\'' +
//                ", declaration=" + declaration +
//                ", belongs='" + belongs + '\'' +
                '}';
    }
}
