package org.netbeans.modules.couchbase.model;

import com.couchbase.client.java.query.N1qlQueryRow;

public class CouchBaseRow {

    int i;
    N1qlQueryRow row;

    public CouchBaseRow(int i, N1qlQueryRow row) {
        this.i = i;
        this.row = row;
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
