package org.netbeans.modules.couchbase.connection;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.modules.couchbase.CouchbaseRootNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.NbPreferences;

public class ConnectionChildFactory extends ChildFactory.Detachable<Cluster> {

    private final List<Cluster> clusters;

    private ChangeListener listener;

    public ConnectionChildFactory() {
        this.clusters = new ArrayList<Cluster>();
    }

    @Override
    protected void addNotify() {
        RefreshConnectionListTrigger.addChangeListener(listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ev) {
                String clusterName = NbPreferences.forModule(CouchbaseRootNode.class).get("clusterName", "error!");
                Cluster cluster = CouchbaseCluster.create(clusterName);
                clusters.add(cluster);
                refresh(true);
            }
        });
    }

    @Override
    protected void removeNotify() {
        if (listener != null) {
            RefreshConnectionListTrigger.removeChangeListener(listener);
            listener = null;
        }
    }

    @Override
    protected boolean createKeys(List<Cluster> list) {
        list.addAll(clusters);
        return true;
    }

    @Override
    protected Node createNodeForKey(Cluster key) {
        return new ConnectionNode(key, "localhost");
    }

}
