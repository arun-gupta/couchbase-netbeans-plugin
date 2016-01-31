package org.netbeans.modules.couchbase.connection;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

public class ConnectionChildFactory extends ChildFactory<Cluster> {

    @Override
    protected boolean createKeys(List<Cluster> list) {
        list.add(CouchbaseCluster.create("localhost"));
        return true;
    }

    @Override
    protected Node createNodeForKey(Cluster key) {
        return new ConnectionNode(key, "localhost");
    }
    
}
