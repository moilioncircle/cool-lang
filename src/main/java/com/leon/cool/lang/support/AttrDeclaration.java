package com.leon.cool.lang.support;

import com.leon.cool.lang.ast.Expression;

import java.util.Optional;

/**
 * Created by leon on 15-10-28.
 */
public class AttrDeclaration {
    public String id;
    public String type;
    public Optional<Expression> expr;
}
