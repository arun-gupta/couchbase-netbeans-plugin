package org.netbeans.modules.couchbase.model;

import com.couchbase.client.java.query.N1qlQueryRow;

public class CouchbaseDocument {

    String bucketName;
    int i;
    N1qlQueryRow row;

    public CouchbaseDocument(String bucketName, int i, N1qlQueryRow row) {
        this.bucketName = bucketName;
        this.i = i;
        this.row = row;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
    
    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public N1qlQueryRow getRow() {
        return row;
    }

    public void setRow(N1qlQueryRow row) {
        this.row = row;
    }
    
}
