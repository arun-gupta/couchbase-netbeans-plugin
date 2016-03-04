package org.netbeans.modules.couchbase.attributes;

import org.netbeans.modules.couchbase.model.CouchbaseAttribute;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQueryRow;
import java.beans.IntrospectionException;
import java.util.List;
import java.util.Set;
import org.netbeans.modules.couchbase.model.CouchbaseDocument;
import org.netbeans.modules.couchbase.model.CouchbaseAttribute.Type;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

public class LeafValueChildFactory extends ChildFactory<CouchbaseAttribute> {

    private final CouchbaseDocument cbr;

    public LeafValueChildFactory(CouchbaseDocument row) {
        this.cbr = row;
    }

    @Override
    protected boolean createKeys(List<CouchbaseAttribute> list) {
        JsonObject json = JsonObject.fromJson(cbr.getRow().toString());
        Set<String> topNames = json.getNames();
        parseNames(topNames, json, list);
        return true;
    }

    private void parseNames(Set<String> topNames, JsonObject json, List<CouchbaseAttribute> list) {
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
                                list.add(new CouchbaseAttribute(cbr, name, value, Type.ARRAY));
                            } else {
                                list.add(new CouchbaseAttribute(cbr, name, value, Type.LEAF));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected Node createNodeForKey(CouchbaseAttribute key) {
        LeafValueNode vn = null;
        try {
            vn = new LeafValueNode(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return vn;
    }
    
}
