package org.netbeans.modules.couchbase.connection;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
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
                String clusterName = NbPreferences.forModule(CouchbaseRootNode.class).get("clusterAddress", "error!");
                CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                        .connectTimeout(60000) //default is 5s
                        .build();
                System.out.println("Create connection");
                //use the env during cluster creation to apply
                Cluster cluster = CouchbaseCluster.create(env, clusterName);
                clusters.add(cluster);
                refresh(false);
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
        String clusterName = NbPreferences.forModule(CouchbaseRootNode.class).get("clusterName", "error!");
        return new ConnectionNode(key, clusterName);
    }

}
