package com.pat.starter.common.expression;

/**
 * ExpressionRootObject
 *
 * @author chouway
 * @date 2021.12.31
 */
public class ExpressionRootObject {

    private final Object object;

    private final Object[] args;

    public ExpressionRootObject(Object object, Object[] args) {
        this.object = object;
        this.args = args;
    }

    public Object getObject() {
        return object;
    }

    public Object[] getArgs() {
        return args;
    }
}
