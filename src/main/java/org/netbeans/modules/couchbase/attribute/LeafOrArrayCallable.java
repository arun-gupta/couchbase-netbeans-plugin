package org.netbeans.modules.couchbase.attribute;

import java.util.concurrent.Callable;
import org.netbeans.modules.couchbase.model.CouchbaseAttribute;
import org.netbeans.modules.couchbase.model.CouchbaseAttribute.Type;
import org.netbeans.modules.couchbase.attribute.ArrayValueChildFactory;
import org.openide.nodes.Children;

public class LeafOrArrayCallable implements Callable<Children> {

    private final CouchbaseAttribute key;

    public LeafOrArrayCallable(CouchbaseAttribute key) {
        this.key = key;
    }

    @Override
    public Children call() throws Exception {
        if (key.getType() == Type.LEAF) {
            return Children.LEAF;
        } else {
            return Children.create(new ArrayValueChildFactory(key), true);
        }
    }

}
