package com.leon.cool.lang.support;

import com.leon.cool.lang.Constant;
import com.leon.cool.lang.factory.ObjectFactory;
import com.leon.cool.lang.factory.TypeFactory;
import com.leon.cool.lang.object.CoolInt;
import com.leon.cool.lang.object.CoolObject;
import com.leon.cool.lang.object.CoolString;
import com.leon.cool.lang.support.declaration.AttrDeclaration;
import com.leon.cool.lang.support.declaration.MethodDeclaration;
import com.leon.cool.lang.support.infrastructure.ConstantPool;
import com.leon.cool.lang.support.infrastructure.Context;
import com.leon.cool.lang.support.infrastructure.Heap;
import com.leon.cool.lang.support.infrastructure.SymbolTable;
import com.leon.cool.lang.tree.EvalTreeVisitor;
import com.leon.cool.lang.type.Type;
import com.leon.cool.lang.type.TypeEnum;
import com.leon.cool.lang.util.Stack;
import com.leon.cool.lang.util.StringUtil;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static com.leon.cool.lang.support.TypeSupport.*;

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
 * @author leon on 15-10-15
 */
public class ScannerSupport implements Closeable {

    public Map<String, String> classGraph = new HashMap<>();
    public Map<String, SymbolTable<String>> symbolTables = new HashMap<>();
    public Map<String, Set<MethodDeclaration>> methodGraph = new HashMap<>();
    public Map<String, Map<String, AttrDeclaration>> attrGraph = new HashMap<>();
    public BufferedReader reader;

    public void createSymbolTable(String className) {
        if (!symbolTables.containsKey(className)) {
            symbolTables.put(className, new SymbolTable<>());
        }
    }

    public void createAttrGraph(String className) {
        if (!attrGraph.containsKey(className)) {
            attrGraph.put(className, new LinkedHashMap<>());
        }
    }

    public void createMethodGraph(String className) {
        if (!methodGraph.containsKey(className)) {
            methodGraph.put(className, new LinkedHashSet<>());
        }
    }

    public void putToClassGraph(String type, Optional<String> parentType) {
        if (classGraph.containsKey(type)) {
            ErrorSupport.error("global.error.class.duplicated", type);
        } else {
            if (parentType.isPresent()) {
                classGraph.put(type, parentType.get());
                checkCircleInherits(classGraph);
            } else {
                if (isObjectType(type)) {
                    classGraph.put(type, null);
                } else {
                    ErrorSupport.error("global.error.inherits.object", type);
                }
            }
        }
    }

    public void putToMethodGraph(String className, MethodDeclaration methodDeclaration) {
        Set<MethodDeclaration> methodDeclarations = methodGraph.get(className);
        if (methodDeclarations.contains(methodDeclaration)) {
            ErrorSupport.error("global.error.method.duplicated", className, StringUtil.constructMethod(methodDeclaration));
        } else {
            methodDeclarations.add(methodDeclaration);
            methodGraph.put(className, methodDeclarations);
        }
    }

    public void putToAttrGraph(String className, String id, AttrDeclaration attrDeclaration) {
        attrGraph.get(className).put(id, attrDeclaration);
    }

    public void mergeMethodGraph(String className) {
        String parentClassName = classGraph.get(className);
        while (parentClassName != null) {
            Set<MethodDeclaration> methodDeclarations = methodGraph.get(className);
            Set<MethodDeclaration> parentDeclarations = methodGraph.get(parentClassName);
            for (MethodDeclaration declaration : methodDeclarations) {
                parentDeclarations.forEach(e -> {
                    //仅返回类型不同的话override错误
                    if (declaration.equals(e) && !declaration.returnType.equals(e.returnType)) {
                        ErrorSupport.error("global.error.override", className, StringUtil.constructMethod(declaration));
                    }
                });
            }
            parentDeclarations.stream().forEach(e -> {
                if (!methodDeclarations.contains(e)) {
                    methodDeclarations.add(e);
                }
            });
            methodGraph.put(className, methodDeclarations);
            parentClassName = classGraph.get(parentClassName);
        }
    }

    public void mergeAttrGraph(String className) {
        Stack<String> inheritsLinks = new Stack<>();
        String temp = className;
        while (temp != null) {
            inheritsLinks.push(temp);
            temp = classGraph.get(temp);
        }
        while (!inheritsLinks.isEmpty()) {
            String parentClassName = inheritsLinks.pop();
            Map<String, AttrDeclaration> attrs = attrGraph.get(parentClassName);
            for (Map.Entry<String, AttrDeclaration> attr : attrs.entrySet()) {
                //参数重定义
                if (lookupSymbolTable(className).lookup(attr.getKey()).isPresent()) {
                    ErrorSupport.error("global.error.attr.redefined", className, attr.getKey(), parentClassName);
                } else {
                    lookupSymbolTable(className).addId(attr.getKey(), attr.getValue().type);
                }
            }
        }
        lookupSymbolTable(className).addId(Constant.SELF, Constant.SELF_TYPE);
    }

    public Optional<MethodDeclaration> lookupMethodDeclaration(String className, String methodName, List<Type> typeInfo) {
        if (methodGraph.get(className) == null) {
            // Type check for NoType.
            return Optional.empty();
        }
        List<MethodDeclaration> list = methodGraph.get(className).stream().filter(e -> e.methodName.equals(methodName) && checkType(e.paramTypes, typeInfo, className)).collect(Collectors.toList());
        if (list.isEmpty()) {
            return Optional.empty();
        } else if (list.size() > 1) {
            //包含多个方法（重载方法），则在重载方法中进一步选择
            return minimumMethodDeclaration(list, className, typeInfo);
        } else {
            return Optional.of(list.get(0));
        }
    }

    private Optional<MethodDeclaration> minimumMethodDeclaration(List<MethodDeclaration> list, String className, List<Type> typeInfo) {
        MethodDeclaration min = new MethodDeclaration();
        min.paramTypes = typeInfo.stream().map(e -> Constant.OBJECT).collect(Collectors.toList());
        label:
        for (MethodDeclaration declaration : list) {
            for (int i = 0; i < declaration.paramTypes.size(); i++) {
                if (!isParent(classGraph, TypeFactory.objectType(declaration.paramTypes.get(i), className), TypeFactory.objectType(min.paramTypes.get(i), className))) {
                    continue label;
                }
            }
            min = declaration;
        }
        for (MethodDeclaration declaration : list) {
            for (int i = 0; i < declaration.paramTypes.size(); i++) {
                if (!isParent(classGraph, TypeFactory.objectType(min.paramTypes.get(i), className), TypeFactory.objectType(declaration.paramTypes.get(i), className))) {
                    ErrorSupport.error("global.error.overload", className, StringUtil.mkString(list.stream().map(StringUtil::constructMethod).collect(Collectors.toList()), Optional.of("["), ",", Optional.of("]")));
                    return Optional.empty();
                }
            }
        }
        return Optional.of(min);
    }

    public Optional<MethodDeclaration> lookupMethodDeclaration(String className, String methodName) {
        return methodGraph.get(className).stream().filter(e -> e.methodName.equals(methodName)).findFirst();
    }

    public SymbolTable<String> lookupSymbolTable(String className) {
        return symbolTables.get(className);
    }

    public void checkUndefinedClass() {
        Set<String> keys = classGraph.keySet();
        keys.forEach(key -> {
            if (!isObjectType(key)) {
                if (!keys.contains(classGraph.get(key))) {
                    ErrorSupport.error("global.error.class.undefined", classGraph.get(key));
                }
            }
        });
    }

    public void close() {
        try {
            if (reader != null) {
                reader.close();
                reader = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Heap.clear();
        ConstantPool.getInstance().clear();
    }

    private BufferedReader reader() {
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }
        return reader;
    }

    /**
     * @param visitor
     * @param type
     * @param context
     * @return
     */
    public CoolObject newDef(EvalTreeVisitor visitor, Type type, Context context) {
        CoolObject object = ObjectFactory.coolObject();
        object.type = type;

        object.variables.enterScope();

        Stack<String> inheritsLinks = new Stack<>();
        String temp = type.className();
        while (temp != null) {
            inheritsLinks.push(temp);
            temp = classGraph.get(temp);
        }
        /**
         * 遍历继承树，找到父类中的属性。
         * 如果父类中的属性是String,Bool,Int类型，则对属性赋默认值.
         * 如果不是上述类型，则赋值void
         * String  = ""
         * Bool = false
         * Int = 0
         * Object = void
         */
        while (!inheritsLinks.isEmpty()) {
            String parentClassName = inheritsLinks.pop();
            Map<String, AttrDeclaration> attrs = attrGraph.getOrDefault(parentClassName, Collections.emptyMap());
            for (Map.Entry<String, AttrDeclaration> attr : attrs.entrySet()) {
                if (isStringType(attr.getValue().type)) {
                    object.variables.addId(attr.getKey(), ObjectFactory.coolStringDefault());
                } else if (isBoolType(attr.getValue().type)) {
                    object.variables.addId(attr.getKey(), ObjectFactory.coolBoolDefault());
                } else if (isIntType(attr.getValue().type)) {
                    object.variables.addId(attr.getKey(), ObjectFactory.coolIntDefault());
                } else {
                    object.variables.addId(attr.getKey(), ObjectFactory.coolVoid());
                }
            }
        }
        initializer(visitor, object);
        //垃圾回收
        gc(context);
        Heap.add(object);
        return object;
    }

    /**
     * Mark-Sweep GC
     *
     * @param context
     */
    public void gc(Context context) {
        if (Heap.size() < Constant.GC_HEAP_SIZE) return;
        SymbolTable<CoolObject> environment = context.environment;

        //Mark
        List<CoolObject> rootObjects = new ArrayList<>();
        for (int i = 0; i < environment.size(); i++) {
            rootObjects.addAll(environment.elementAt(i).values().stream().filter(e -> isObjectType(e.type)).collect(Collectors.toList()));
        }

        while (!rootObjects.isEmpty()) {
            CoolObject obj = rootObjects.remove(0);
            Heap.canReach(obj);
            SymbolTable<CoolObject> variables = obj.variables;
            if (variables == null) continue;
            for (int i = 0; i < variables.size(); i++) {
                Collection<CoolObject> values = variables.elementAt(i).values();
                for (CoolObject variable : values) {
                    //防止循环引用
                    if (isObjectType(variable.type) && !Heap.isReach(variable)) {
                        rootObjects.add(variable);
                    }
                }
            }
        }
        //Sweep
        Heap.clearUnreachable();
    }

    /**
     * @param visitor
     * @param object
     * @see this.newDef(Type)
     * <p>
     * 对有表达式的属性求值，并更新对象变量表，没有表达式的属性会在newDef中赋初值。
     */
    private void initializer(EvalTreeVisitor visitor, CoolObject object) {
        Stack<String> inheritsLinks = new Stack<>();
        String temp = object.type.className();
        while (temp != null) {
            inheritsLinks.push(temp);
            temp = classGraph.get(temp);
        }
        Context context = new Context(object, object.variables);
        while (!inheritsLinks.isEmpty()) {
            String parentClassName = inheritsLinks.pop();
            Map<String, AttrDeclaration> attrs = attrGraph.getOrDefault(parentClassName, Collections.emptyMap());
            attrs.entrySet().forEach(attr -> {
                if (attr.getValue().expr.isPresent()) {
                    object.variables.addId(attr.getKey(), attr.getValue().expr.get().accept(visitor, context));
                }
            });
        }
    }

    /**
     * build-in方法求值
     *
     * @param paramObjects
     * @param obj
     * @param methodDeclaration
     * @param pos
     * @return CoolObject
     */
    public CoolObject buildIn(List<CoolObject> paramObjects, CoolObject obj, MethodDeclaration methodDeclaration, String pos) {
        switch (methodDeclaration.belongs) {
            case "Object":
                if (methodDeclaration.methodName.equals("type_name")) {
                    return ObjectFactory.coolString(obj.type.className());
                } else if (methodDeclaration.methodName.equals("copy")) {
                    return obj.copy();
                } else if (methodDeclaration.methodName.equals("abort")) {
                    return ObjectFactory.coolObject().abort();
                }
                break;
            case "IO":
                if (methodDeclaration.methodName.equals("out_string")) {
                    System.out.print(((CoolString) paramObjects.get(0)).str);
                    return obj;
                } else if (methodDeclaration.methodName.equals("out_int")) {
                    System.out.print(((CoolInt) paramObjects.get(0)).val);
                    return obj;
                } else if (methodDeclaration.methodName.equals("in_string")) {
                    try {
                        String str = reader().readLine();
                        return ObjectFactory.coolString(str);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ErrorSupport.error("unexpected.error");
                    }
                    return ObjectFactory.coolStringDefault();
                } else if (methodDeclaration.methodName.equals("in_int")) {
                    try {
                        String str = reader().readLine();
                        return ObjectFactory.coolInt(Integer.parseInt(str));
                    } catch (Exception e) {
                        ErrorSupport.error("unexpected.error");
                    }
                    return ObjectFactory.coolIntDefault();
                }
                break;
            case "String":
                if (methodDeclaration.methodName.equals("length")) {
                    return ((CoolString) obj).length();
                } else if (methodDeclaration.methodName.equals("concat")) {
                    return ((CoolString) obj).concat((CoolString) paramObjects.get(0));
                } else if (methodDeclaration.methodName.equals("substr")) {
                    return ((CoolString) obj).substr((CoolInt) paramObjects.get(0), (CoolInt) paramObjects.get(1), pos);
                }
                break;
        }
        return null;
    }

    /**
     * 求多个类型的最小公共父类型
     *
     * @param types
     * @return 最小公共父类型
     * @see com.leon.cool.lang.tree.TypeCheckTreeScanner
     * @see com.leon.cool.lang.ast.CaseDef
     * @see com.leon.cool.lang.ast.Cond
     */
    public Type lub(List<Type> types) {
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

    private Type lub(Type type1, Type type2) {
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

    private void checkCircleInherits(Map<String, String> classGraph) {
        Set<String> keys = classGraph.keySet();
        for (String key : keys) {
            String parent = classGraph.get(key);
            while (parent != null && !parent.equals(key)) {
                parent = classGraph.get(parent);
            }
            if (parent != null && parent.equals(key)) {
                ErrorSupport.error("global.error.class.circle", key);
            }
        }
    }

    private boolean checkType(List<String> paramTypes, List<Type> typeInfos, String className) {
        if (paramTypes.size() != typeInfos.size()) {
            return false;
        } else {
            for (int i = 0; i < typeInfos.size(); i++) {
                if (!isParent(classGraph, typeInfos.get(i), TypeFactory.objectType(paramTypes.get(i), className))) {
                    return false;
                }
            }
        }
        return true;
    }

}
