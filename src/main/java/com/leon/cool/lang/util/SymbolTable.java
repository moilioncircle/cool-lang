package com.leon.cool.lang.util;

import com.leon.cool.lang.util.Stack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class SymbolTable<T> {
    private final Stack<HashMap<String, T>> tbl;

    public SymbolTable() {
        tbl = new Stack<>();
    }

    public void enterScope() {
        tbl.push(new LinkedHashMap<>());
    }

    public void exitScope() {
        if (tbl.isEmpty()) {
            System.out.println("existScope: can't remove scope from an isEmpty symbol table.");
        }
        tbl.pop();
    }

    public void addId(String id, T info) {
        if (tbl.isEmpty()) {
            System.out.println("addId: can't add a symbol without a scope.");
        }
        tbl.top().put(id, info);
    }

    public Optional<T> lookup(String sym) {
        if (tbl.isEmpty()) {
            System.out.println("lookup: no scope in symbol table.");
        }
        for (int i = 0; i < tbl.size(); i++) {
            T info = tbl.elementAt(i).get(sym);
            if (info != null) return Optional.of(info);
        }
        return Optional.empty();
    }

    public void update(String sym, T obj) {
        if (tbl.isEmpty()) {
            System.out.println("lookup: no scope in symbol table.");
        }
        for (int i = 0; i < tbl.size(); i++) {
            if (tbl.elementAt(i).containsKey(sym)) {
                tbl.elementAt(i).put(sym, obj);
            }
        }
    }

    public HashMap<String, T> elementAt(int index) {
        return tbl.elementAt(index);
    }

    public int size() {
        return tbl.size();
    }

    public Optional<Map<String, T>> topStack() {
        if (tbl.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(tbl.top());
        }
    }

    public String toString() {
        String res = "";
        for (int i = tbl.size() - 1, j = 0; i >= 0; i--, j++) {
            res += "Scope " + j + ": " + tbl.elementAt(i) + "\n";
        }
        return res;
    }
}




