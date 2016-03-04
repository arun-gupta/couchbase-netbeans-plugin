package org.netbeans.modules.couchbase.model;

public class CouchbaseAttribute {

    private CouchbaseDocument cbr;
    private String name;
    private Object value;
    private Type type;

    public enum Type {
        LEAF, ARRAY
    };

    public CouchbaseAttribute(CouchbaseDocument cbr, String name, Object value, Type type) {
        this.cbr = cbr;
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public CouchbaseDocument getCbr() {
        return cbr;
    }

    public void setCbr(CouchbaseDocument cbr) {
        this.cbr = cbr;
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
