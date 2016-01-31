package org.netbeans.modules.couchbase.value;

import java.util.concurrent.Callable;
import org.netbeans.modules.couchbase.model.CouchbaseValue;
import org.netbeans.modules.couchbase.model.CouchbaseValue.Type;
import org.netbeans.modules.couchbase.value.ArrayValueChildFactory;
import org.openide.nodes.Children;

public class LeafOrArrayCallable implements Callable<Children> {

    private final CouchbaseValue key;

    public LeafOrArrayCallable(CouchbaseValue key) {
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
