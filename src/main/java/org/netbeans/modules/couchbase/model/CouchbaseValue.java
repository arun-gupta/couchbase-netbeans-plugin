package org.netbeans.modules.couchbase.model;

public class CouchbaseValue {

    private String name;
    private Object value;
    private Type type;

    public enum Type {
        LEAF, ARRAY
    };

    public CouchbaseValue(String name, Object value, Type type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
