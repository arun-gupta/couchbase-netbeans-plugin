package org.netbeans.modules.couchbase.value;

import org.netbeans.modules.couchbase.model.CouchbaseValue;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQueryRow;
import java.beans.IntrospectionException;
import java.util.List;
import java.util.Set;
import org.netbeans.modules.couchbase.model.CouchBaseRow;
import org.netbeans.modules.couchbase.model.CouchbaseValue.Type;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

public class LeafValueChildFactory extends ChildFactory<CouchbaseValue> {

    private final CouchBaseRow cbr;

    public LeafValueChildFactory(CouchBaseRow row) {
        this.cbr = row;
    }

    @Override
    protected boolean createKeys(List<CouchbaseValue> list) {
        JsonObject json = JsonObject.fromJson(cbr.getRow().toString());
        Set<String> topNames = json.getNames();
        parseNames(topNames, json, list);
        return true;
    }

    private void parseNames(Set<String> topNames, JsonObject json, List<CouchbaseValue> list) {
        for (String topName : topNames) {
            JsonObject firstLevel = json.getObject(topName);
            if (firstLevel != null) {
                Set<String> names = firstLevel.getNames();
                for (String name : names) {
                    Object value = firstLevel.get(name);
                    if (value != null) {
                        String simpleName = value.getClass().getSimpleName();
                        if (simpleName != null) {
                            if (simpleName.equals("JsonObject")) {
                                list.add(new CouchbaseValue(cbr, name, value, Type.ARRAY));
                            } else {
                                list.add(new CouchbaseValue(cbr, name, value, Type.LEAF));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected Node createNodeForKey(CouchbaseValue key) {
        LeafValueNode vn = null;
        try {
            vn = new LeafValueNode(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return vn;
    }
    
}
