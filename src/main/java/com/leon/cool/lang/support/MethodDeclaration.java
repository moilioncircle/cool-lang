package com.leon.cool.lang.support;

import com.leon.cool.lang.ast.MethodDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 15-10-15.
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
