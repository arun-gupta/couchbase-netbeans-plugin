package org.netbeans.modules.couchbase;

import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.db.explorer.node.NodeProvider;
import org.netbeans.api.db.explorer.node.NodeProviderFactory;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

public class CouchbaseNodeProvider extends NodeProvider {
    // lazy initialization holder class idiom for static fields is used
    // for retrieving the factory
    public static NodeProviderFactory getFactory() {
        return FactoryHolder.FACTORY;
    }

    private static class FactoryHolder {
        static final NodeProviderFactory FACTORY = new NodeProviderFactory() {
            @Override
            public CouchbaseNodeProvider createInstance(Lookup lookup) {
                CouchbaseNodeProvider provider = new CouchbaseNodeProvider(lookup);
                return provider;
            }
        };
    }

    private CouchbaseNodeProvider(Lookup lookup) {
        super(lookup);
    }

    @Override
    protected synchronized void initialize() {
        List<Node> newList = new ArrayList<Node>();
        newList.add(CouchbaseRootNode.getDefault());
        setNodes(newList);
    }
    
}
