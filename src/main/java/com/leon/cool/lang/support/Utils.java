package com.leon.cool.lang.support;

import com.leon.cool.lang.ast.TreeNode;
import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.factory.TypeFactory;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.tokenizer.Token;
import com.leon.cool.lang.type.Type;
import com.leon.cool.lang.type.TypeEnum;
import com.leon.cool.lang.util.Constant;
import com.leon.cool.lang.util.Pos;
import com.leon.cool.lang.util.Stack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.leon.cool.lang.tokenizer.TokenKind.ID;
import static com.leon.cool.lang.tokenizer.TokenKind.TYPE;

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
public class Utils {
    public static Properties messages = new Properties();

    static {
        try (InputStream stream = Utils.class.getClassLoader().getResourceAsStream("messages.properties")) {
            messages.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> classGraph = new HashMap<>();
    private static Map<String, Set<MethodDeclaration>> methodGraph = new HashMap<>();
    private static Map<String, Map<String, AttrDeclaration>> attrGraph = new HashMap<>();
    private static Map<String, SymbolTable> symbolTables = new HashMap<>();
    private static BufferedReader reader;

    public static void createSymbolTable(String className) {
        if (!symbolTables.containsKey(className)) {
            symbolTables.put(className, new SymbolTable());
        }
    }

    public static void createAttrGraph(String className) {
        if (!attrGraph.containsKey(className)) {
            attrGraph.put(className, new LinkedHashMap<>());
        }
    }

    public static void createMethodGraph(String className) {
        if (!methodGraph.containsKey(className)) {
            methodGraph.put(className, new LinkedHashSet<>());
        }
    }

    public static void putToClassGraph(String type, Optional<String> parentType) {
        if (classGraph.containsKey(type)) {
            Utils.error("global.error.class.duplicated", type);
        } else {
            if (parentType.isPresent()) {
                classGraph.put(type, parentType.get());
                checkCircleInherits(classGraph);
            } else {
                if (Utils.isObjectType(type)) {
                    classGraph.put(type, null);
                } else {
                    Utils.error("global.error.inherits.object", type);
                }
            }
        }
    }

    public static void putToMethodGraph(String className, MethodDeclaration methodDeclaration) {
        Set<MethodDeclaration> methodDeclarations = methodGraph.get(className);
        if (methodDeclarations.contains(methodDeclaration)) {
            Utils.error("global.error.method.duplicated", className, constructMethod(methodDeclaration));
        } else {
            methodDeclarations.add(methodDeclaration);
            methodGraph.put(className, methodDeclarations);
        }
    }

    public static void putToAttrGraph(String className, String id, AttrDeclaration attrDeclaration) {
        attrGraph.get(className).put(id, attrDeclaration);
    }

    public static void mergeMethodGraph(String className) {
        String parentClassName = Utils.classGraph.get(className);
        while (parentClassName != null) {
            Set<MethodDeclaration> methodDeclarations = methodGraph.get(className);
            Set<MethodDeclaration> parentDeclarations = methodGraph.get(parentClassName);
            for (MethodDeclaration declaration : methodDeclarations) {
                parentDeclarations.forEach(e -> {
                    if (declaration.equals(e) && !declaration.returnType.equals(e.returnType)) {
                        Utils.error("global.error.override", className, constructMethod(declaration));
                    }
                });
            }
            parentDeclarations.stream().forEach(e -> {
                if (!methodDeclarations.contains(e)) {
                    methodDeclarations.add(e);
                }
            });
            methodGraph.put(className, methodDeclarations);
            parentClassName = Utils.classGraph.get(parentClassName);
        }
    }

    public static void mergeAttrGraph(String className) {
        Stack<String> inheritsLinks = new Stack<>();
        String temp = className;
        while (temp != null) {
            inheritsLinks.push(temp);
            temp = Utils.classGraph.get(temp);
        }
        while (!inheritsLinks.isEmpty()) {
            String parentClassName = inheritsLinks.pop();
            Map<String, AttrDeclaration> attrs = attrGraph.get(parentClassName);
            for (Map.Entry<String, AttrDeclaration> attr : attrs.entrySet()) {
                if (Utils.lookupSymbolTable(className).lookup(attr.getKey()).isPresent()) {
                    Utils.error("global.error.attr.redefined", className, attr.getKey(), parentClassName);
                } else {
                    Utils.lookupSymbolTable(className).addId(attr.getKey(), attr.getValue().type);
                }
            }
        }
        Utils.lookupSymbolTable(className).addId(Constant.SELF, Constant.SELF_TYPE);
    }


    public static Optional<MethodDeclaration> lookupMethodDeclaration(String className, String methodName, List<Type> typeInfo) {
        if (methodGraph.get(className) == null) {
            // Type check for NoType.
            return Optional.empty();
        }
        List<MethodDeclaration> list = methodGraph.get(className).stream().filter(e -> e.methodName.equals(methodName) && checkType(e.paramTypes, typeInfo, className)).collect(Collectors.toList());
        if (list.isEmpty()) {
            return Optional.empty();
        } else if (list.size() > 1) {
            return Utils.minimumMethodDeclaration(list, className, typeInfo);
        } else {
            return Optional.of(list.get(0));
        }
    }

    public static Optional<MethodDeclaration> lookupMethodDeclaration(String className, String methodName) {
        return methodGraph.get(className).stream().filter(e -> e.methodName.equals(methodName)).findFirst();
    }

    public static SymbolTable lookupSymbolTable(String className) {
        return symbolTables.get(className);
    }

    public static void checkUndefinedClass() {
        Set<String> keys = classGraph.keySet();
        keys.forEach(key -> {
            if (!isObjectType(key)) {
                if (!keys.contains(classGraph.get(key))) {
                    error("global.error.class.undefined", classGraph.get(key));
                }
            }
        });
    }

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

    public static boolean isTypeDefined(Token token) {
        return !isSelfType(token) && !classGraph.containsKey(token.name);
    }

    public static boolean isTypeDefined(String name) {
        return isSelfType(name) || classGraph.containsKey(name);
    }

    public static boolean isSelfType(String name) {
        return name.equals(Constant.SELF_TYPE);
    }

    private static boolean isStringType(String name) {
        return name.equals(Constant.STRING);
    }

    private static boolean isIntType(String name) {
        return name.equals(Constant.INT);
    }

    private static boolean isBoolType(String name) {
        return name.equals(Constant.BOOL);
    }

    private static boolean isObjectType(String name) {
        return name.equals(Constant.OBJECT);
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

    private static boolean isBasicType(String string) {
        return isBoolType(string) || isIntType(string) || isStringType(string);
    }

    public static boolean isParent(Type typeInfo, Type parentTypeInfo) {
        if (typeInfo.type() == TypeEnum.NO_TYPE) {
            return true;
        } else if (typeInfo.type() == TypeEnum.SELF_TYPE && parentTypeInfo.type() == TypeEnum.SELF_TYPE) {
            return typeInfo.replace().type() == parentTypeInfo.replace().type();
        } else if (typeInfo.type() != TypeEnum.SELF_TYPE && parentTypeInfo.type() == TypeEnum.SELF_TYPE) {
            return false;
        } else if (typeInfo.type() == TypeEnum.SELF_TYPE && parentTypeInfo.type() != TypeEnum.SELF_TYPE) {
            return isParent(typeInfo.replace(), parentTypeInfo);
        } else if (typeInfo.type() != TypeEnum.SELF_TYPE && parentTypeInfo.type() != TypeEnum.SELF_TYPE) {
            Type temp = typeInfo;
            while (temp != null) {
                if (temp.type() == parentTypeInfo.type()) {
                    return true;
                }
                String parentType = classGraph.get(temp.className());
                if (parentType == null) {
                    return false;
                }
                temp = TypeFactory.objectType(parentType);
            }
            return false;
        }
        return false;
    }

    //clear method
    public static void clear() {
        classGraph = new HashMap<>();
        methodGraph = new HashMap<>();
        attrGraph = new HashMap<>();
        symbolTables = new HashMap<>();
    }

    public static void close() {
        try {
            if (reader != null) {
                reader.close();
                reader = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedReader reader() {
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }
        return reader;
    }

    public static void error(String key, String... params) {
        throw new RuntimeException(errorMsg(key, params));
    }

    public static String errorMsg(String key, String... params) {
        return MessageFormat.format(messages.getProperty(key), params);
    }

    public static String errorPos(TreeNode node) {
        return errorPos(node.starPos, node.endPos);
    }

    public static String errorPos(Token token) {
        return errorPos(token.startPos, token.endPos);
    }

    public static String errorPos(Pos startPos, Pos endPos) {
        return " at " + startPos + " to " + endPos;
    }

    public static String constructMethod(String id, List<String> params) {
        return id + Utils.mkString(params, Optional.of("("), ",", Optional.of(")"));
    }

    public static String constructMethod(MethodDeclaration methodDeclaration) {
        return constructMethod(methodDeclaration.methodName, methodDeclaration.paramTypes);
    }

    public static CoolObject newDef(Type type) {
        CoolObject object = ObjectFactory.coolObject();
        object.type = type;

        Stack<String> inheritsLinks = new Stack<>();
        String temp = type.className();
        while (temp != null) {
            inheritsLinks.push(temp);
            temp = Utils.classGraph.get(temp);
        }

        Env env1 = new Env();
        env1.so = object;
        env1.env = new SymbolTable();
        env1.env.enterScope();

        while (!inheritsLinks.isEmpty()) {
            String parentClassName = inheritsLinks.pop();
            Map<String, AttrDeclaration> attrs = attrGraph.get(parentClassName);
            for (Map.Entry<String, AttrDeclaration> attr : attrs.entrySet()) {
                if (isStringType(attr.getValue().type)) {
                    env1.env.addId(attr.getKey(), ObjectFactory.coolStringDefault());
                } else if (isBoolType(attr.getValue().type)) {
                    env1.env.addId(attr.getKey(), ObjectFactory.coolBoolDefault());
                } else if (isIntType(attr.getValue().type)) {
                    env1.env.addId(attr.getKey(), ObjectFactory.coolIntDefault());
                } else {
                    env1.env.addId(attr.getKey(), ObjectFactory.coolVoid());
                }
            }
        }
        object.env = env1;
        initializer(env1);
        return object;
    }

    public static Type lub(List<Type> types) {
        return types.stream().reduce(TypeFactory.noType(), (type1, type2) -> {
            if (type1.type() == TypeEnum.NO_TYPE) {
                return type2;
            } else if (type2.type() == TypeEnum.NO_TYPE) {
                return type1;
            } else if (type1.type() == TypeEnum.SELF_TYPE && type2.type() == TypeEnum.SELF_TYPE) {
                return TypeFactory.selfType(lub(type1.replace(), type2.replace()).className());
            } else {
                return lub(type1.replace(), type2.replace());
            }
        });
    }

    private static Type lub(Type type1, Type type2) {
        List<String> list1 = new ArrayList<>();
        list1.add(type1.className());
        String temp = type1.className();
        while (temp != null) {
            temp = classGraph.get(temp);
            list1.add(temp);
        }
        List<String> list2 = new ArrayList<>();
        list2.add(type2.className());
        temp = type2.className();
        while (temp != null) {
            temp = classGraph.get(temp);
            list2.add(temp);
        }
        return TypeFactory.objectType(list1.stream().filter(list2::contains).findFirst().get());
    }

    private static void initializer(Env env) {
        env.env.addId(Constant.SELF, env.so);
        Stack<String> inheritsLinks = new Stack<>();
        String temp = env.so.type.className();
        while (temp != null) {
            inheritsLinks.push(temp);
            temp = Utils.classGraph.get(temp);
        }
        while (!inheritsLinks.isEmpty()) {
            String parentClassName = inheritsLinks.pop();
            Map<String, AttrDeclaration> attrs = attrGraph.get(parentClassName);
            attrs.entrySet().forEach(attr -> {
                if (attr.getValue().expr.isPresent()) {
                    env.env.addId(attr.getKey(), attr.getValue().expr.get().eval(env));
                }
            });
        }
    }

    private static Optional<MethodDeclaration> minimumMethodDeclaration(List<MethodDeclaration> list, String className, List<Type> typeInfo) {
        MethodDeclaration min = new MethodDeclaration();
        min.paramTypes = typeInfo.stream().map(e -> Constant.OBJECT).collect(Collectors.toList());
        label:
        for (MethodDeclaration e : list) {
            for (int i = 0; i < e.paramTypes.size(); i++) {
                if (!Utils.isParent(TypeFactory.objectType(e.paramTypes.get(i), className), TypeFactory.objectType(min.paramTypes.get(i), className))) {
                    continue label;
                }
            }
            min = e;
        }
        for (MethodDeclaration e : list) {
            for (int i = 0; i < e.paramTypes.size(); i++) {
                if (!Utils.isParent(TypeFactory.objectType(min.paramTypes.get(i), className), TypeFactory.objectType(e.paramTypes.get(i), className))) {
                    Utils.error("global.error.overload", className, mkString(list.stream().map(Utils::constructMethod).collect(Collectors.toList()), Optional.of("["), ",", Optional.of("]")));
                    return Optional.empty();
                }
            }
        }
        return Optional.of(min);
    }


    private static void checkCircleInherits(Map<String, String> classGraph) {
        Set<String> keys = classGraph.keySet();
        for (String key : keys) {
            String parent = classGraph.get(key);
            while (parent != null && !parent.equals(key)) {
                parent = classGraph.get(parent);
            }
            if (parent != null && parent.equals(key)) {
                Utils.error("global.error.class.circle", key);
            }
        }
    }

    private static boolean checkType(List<String> paramTypes, List<Type> typeInfos, String className) {
        if (paramTypes.size() != typeInfos.size()) {
            return false;
        } else {
            for (int i = 0; i < typeInfos.size(); i++) {
                if (!Utils.isParent(typeInfos.get(i), TypeFactory.objectType(paramTypes.get(i), className))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static <T> String mkString(List<T> tks, String split) {
        return mkString(tks, Optional.<String>empty(), split, Optional.<String>empty());
    }

    public static <T> String mkString(List<T> tks, Optional<String> beforeOpt, String split, Optional<String> endOpt) {
        Iterator<T> it = tks.iterator();
        StringBuilder sb = new StringBuilder();
        if (beforeOpt.isPresent()) {
            sb.append(beforeOpt.get());
        }
        while (it.hasNext()) {
            sb.append(it.next());
            if (!it.hasNext()) {
                break;
            }
            sb.append(split);
        }
        if (endOpt.isPresent()) {
            sb.append(endOpt.get());
        }
        return sb.toString();
    }
}
