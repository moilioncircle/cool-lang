package com.leon.cool.lang.support;

import com.leon.cool.lang.Constant;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.type.Type;
import com.leon.cool.lang.type.TypeEnum;

import java.util.Map;

import static com.leon.cool.lang.factory.TypeFactory.objectType;
import static com.leon.cool.lang.glossary.TokenKind.ID;
import static com.leon.cool.lang.glossary.TokenKind.TYPE;

/**
 * Created by Baoyi Chen on 2017/2/24.
 */
public class TypeSupport {
    public static boolean isSelfType(Token token) {
        return token.kind == TYPE && isSelfType(token.name);
    }

    public static boolean isStringType(Token token) {
        return token.kind == TYPE && isStringType(token.name);
    }

    public static boolean isIntType(Token token) {
        return token.kind == TYPE && isIntType(token.name);
    }

    public static boolean isBoolType(Token token) {
        return token.kind == TYPE && isBoolType(token.name);
    }

    public static boolean isTypeDefined(Map<String, String> classGraph, Token token) {
        return !isSelfType(token) && !classGraph.containsKey(token.name);
    }

    public static boolean isTypeDefined(Map<String, String> classGraph, String name) {
        return isSelfType(name) || classGraph.containsKey(name);
    }

    public static boolean isSelfType(String name) {
        return name.equals(Constant.SELF_TYPE);
    }

    public static boolean isStringType(String name) {
        return name.equals(Constant.STRING);
    }

    public static boolean isIntType(String name) {
        return name.equals(Constant.INT);
    }

    public static boolean isBoolType(String name) {
        return name.equals(Constant.BOOL);
    }

    public static boolean isObjectType(String name) {
        return name.equals(Constant.OBJECT);
    }

    public static boolean isObjectType(Type typeInfo) {
        return typeInfo.type() == TypeEnum.OBJECT;
    }


    public static boolean isSelf(Token token) {
        return token.kind == ID && token.name.equals(Constant.SELF);
    }

    public static boolean isBasicType(Type typeInfo) {
        return typeInfo.type() == TypeEnum.BOOL || typeInfo.type() == TypeEnum.INT || typeInfo.type() == TypeEnum.STRING;
    }

    public static boolean isBasicType(Token token) {
        return isBasicType(token.name);
    }

    public static boolean isBasicType(String string) {
        return isBoolType(string) || isIntType(string) || isStringType(string);
    }

    /**
     * 判断两个类型是否存在父子关系
     *
     * @param typeInfo       子类型
     * @param parentTypeInfo 父类型
     * @return true：存在父子关系；false：不存在父子关系
     */
    public static boolean isParent(Map<String, String> classGraph, Type typeInfo, Type parentTypeInfo) {
        if (typeInfo.type() == TypeEnum.NO_TYPE) {
            return true;
        } else if (typeInfo.type() == TypeEnum.SELF_TYPE && parentTypeInfo.type() == TypeEnum.SELF_TYPE) {
            return typeInfo.replace().type() == parentTypeInfo.replace().type();
        } else if (typeInfo.type() != TypeEnum.SELF_TYPE && parentTypeInfo.type() == TypeEnum.SELF_TYPE) {
            return false;
        } else if (typeInfo.type() == TypeEnum.SELF_TYPE && parentTypeInfo.type() != TypeEnum.SELF_TYPE) {
            return isParent(classGraph, typeInfo.replace(), parentTypeInfo);
        } else if (typeInfo.type() != TypeEnum.SELF_TYPE && parentTypeInfo.type() != TypeEnum.SELF_TYPE) {
            Type temp = typeInfo;
            while (temp != null) {
                if (temp.type() == parentTypeInfo.type()) return true;
                String parentType = classGraph.get(temp.className());
                if (parentType == null) return false;
                temp = objectType(parentType);
            }
            return false;
        }
        return false;
    }
}
