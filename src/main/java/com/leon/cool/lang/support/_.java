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
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static com.leon.cool.lang.tokenizer.TokenKind.ID;
import static com.leon.cool.lang.tokenizer.TokenKind.TYPE;

/**
 * Created by leon on 15-10-15.
 */
public class _ {
    public static Map<String, String> classGraph = new HashMap<>();
    public static Map<String, Set<MethodDeclaration>> methodGraph = new HashMap<>();
    public static Map<String, Map<String, AttrDeclaration>> attrGraph = new HashMap<>();
    public static Map<String, SymbolTable> symbolTables = new HashMap<>();
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
            _.error("class:" + type + " Duplicated class defined.");
        } else {
            if (parentType.isPresent()) {
                classGraph.put(type, parentType.get());
                checkCircleInherits(classGraph);
            } else {
                if (_.isObjectType(type)) {
                    classGraph.put(type, null);
                } else {
                    _.error("class:" + type + " Must inherits Object.");
                }
            }
        }
    }

    public static void putToMethodGraph(String className, MethodDeclaration methodDeclaration) {
        Set<MethodDeclaration> methodDeclarations = methodGraph.get(className);
        if (methodDeclarations.contains(methodDeclaration)) {
            _.error("class:" + className + " Duplicated method declaration " + methodDeclaration.methodName);
        } else {
            methodDeclarations.add(methodDeclaration);
            methodGraph.put(className, methodDeclarations);
        }
    }

    public static void putToAttrGraph(String className, String id, AttrDeclaration attrDeclaration) {
        attrGraph.get(className).put(id, attrDeclaration);
    }

    public static void mergeMethodGraph(String className) {
        String parentClassName = _.classGraph.get(className);
        while (parentClassName != null) {
            if (parentClassName != null) {
                Set<MethodDeclaration> methodDeclarations = methodGraph.get(className);
                Set<MethodDeclaration> parentDeclarations = methodGraph.get(parentClassName);
                for (MethodDeclaration declaration : methodDeclarations) {
                    parentDeclarations.forEach(e -> {
                        if (declaration.equals(e) && !declaration.returnType.equals(e.returnType)) {
                            _.error("class :" + className + " Override error. Difference return type. method :" + declaration);
                        }
                    });
                }
                parentDeclarations.stream().forEach(e -> {
                    if (!methodDeclarations.contains(e)) {
                        methodDeclarations.add(e);
                    }
                });
                methodGraph.put(className, methodDeclarations);
            }
            parentClassName = _.classGraph.get(parentClassName);
        }
    }

    public static void mergeAttrGraph(String className) {
        Stack<String> inheritsLinks = new Stack<>();
        String temp = className;
        while (temp != null) {
            inheritsLinks.push(temp);
            temp = _.classGraph.get(temp);
        }
        while (!inheritsLinks.isEmpty()) {
            String parentClassName = inheritsLinks.pop();
            Map<String, AttrDeclaration> attrs = attrGraph.get(parentClassName);
            for (Map.Entry<String, AttrDeclaration> attr : attrs.entrySet()) {
                if (_.lookupSymbolTable(className).lookup(attr.getKey()).isPresent()) {
                    _.error("class:" + className + " Type check error. can not redefined attr " + attr.getKey() + " from parent class:" + parentClassName);
                } else {
                    _.lookupSymbolTable(className).addId(attr.getKey(), attr.getValue().type);
                }
            }
        }
        _.lookupSymbolTable(className).addId(Constant.SELF, Constant.SELF_TYPE);
    }


    public static Optional<MethodDeclaration> lookupMethodDeclaration(String className, String methodName, List<Type> typeInfo) {
        List<MethodDeclaration> list = methodGraph.get(className).stream().filter(e -> e.methodName.equals(methodName) && checkType(e.paramTypes, typeInfo, className)).collect(Collectors.toList());
        if (list.isEmpty()) {
            return Optional.empty();
        } else if (list.size() > 1) {
            return _.minimumMethodDeclaration(list, className, typeInfo);
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
                    error("class:" + classGraph.get(key) + " Undefined.");
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
        return isSelfType(token) || classGraph.containsKey(token.name);
    }

    public static boolean isTypeDefined(String name) {
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

    public static void error(String errMsg) {
        throw new RuntimeException(errMsg);
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

    public static CoolObject newDef(Type type) {
        CoolObject object = ObjectFactory.coolObject();
        object.type = type;

        Stack<String> inheritsLinks = new Stack<>();
        String temp = type.className();
        while (temp != null) {
            inheritsLinks.push(temp);
            temp = _.classGraph.get(temp);
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
        return TypeFactory.objectType(list1.stream().filter(e -> list2.contains(e)).findFirst().get());
    }

    private static void initializer(Env env) {
        env.env.addId(Constant.SELF, env.so);
        Stack<String> inheritsLinks = new Stack<>();
        String temp = env.so.type.className();
        while (temp != null) {
            inheritsLinks.push(temp);
            temp = _.classGraph.get(temp);
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
                if (!_.isParent(TypeFactory.objectType(e.paramTypes.get(i), className), TypeFactory.objectType(min.paramTypes.get(i), className))) {
                    continue label;
                }
            }
            min = e;
        }
        for (MethodDeclaration e : list) {
            for (int i = 0; i < e.paramTypes.size(); i++) {
                if (!_.isParent(TypeFactory.objectType(min.paramTypes.get(i), className), TypeFactory.objectType(e.paramTypes.get(i), className))) {
                    _.error("class:" + className + " Can't decide which method need to choose. method name " + list);
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
                _.error("class:" + key + " Circle inherits.");
            }
        }
    }

    private static boolean checkType(List<String> paramTypes, List<Type> typeInfos, String className) {
        if (paramTypes.size() != typeInfos.size()) {
            return false;
        } else {
            for (int i = 0; i < typeInfos.size(); i++) {
                if (!_.isParent(typeInfos.get(i), TypeFactory.objectType(paramTypes.get(i), className))) {
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
