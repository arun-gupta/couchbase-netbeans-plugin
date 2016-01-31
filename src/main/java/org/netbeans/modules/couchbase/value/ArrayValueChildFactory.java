package org.netbeans.modules.couchbase.value;

import org.netbeans.modules.couchbase.model.CouchbaseValue;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.transcoder.JsonTranscoder;
import java.beans.IntrospectionException;
import java.util.List;
import java.util.Set;
import org.netbeans.modules.couchbase.model.CouchbaseValue.Type;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

public class ArrayValueChildFactory extends ChildFactory<String> {

    private final CouchbaseValue cd;

    public ArrayValueChildFactory(CouchbaseValue cd) {
        this.cd = cd;
    }

    @Override
    protected boolean createKeys(List<String> list) {
        if(cd.getType() == Type.ARRAY){
            JsonTranscoder trans = new JsonTranscoder();
            JsonObject arrayContent;
            try {
                arrayContent = trans.stringToJsonObject(cd.getValue().toString());
                Set<String> arrayNames = arrayContent.getNames();
                for (String arrayName : arrayNames) {
                    String value = arrayContent.get(arrayName).toString();
                    list.add(arrayName+":"+value);
                }
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(String key) {
        ArrayValueNode vn = null;
        try {
            vn = new ArrayValueNode(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return vn;
    }
    
}
