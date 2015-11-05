package com.leon.cool.lang.type;

/**
 * Created by leon on 15-10-14.
 */
public class ObjectType implements Type {
    public String type;

    public ObjectType(String type) {
        this.type = type;
    }

    @Override
    public TypeEnum type() {
        return TypeEnum.OBJECT;
    }

    @Override
    public Type replace() {
        return this;
    }

    @Override
    public String className() {
        return toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectType)) return false;

        ObjectType that = (ObjectType) o;

        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    @Override
    public String toString() {
        return type;
    }
}
