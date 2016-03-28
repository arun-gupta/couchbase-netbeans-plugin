package org.netbeans.modules.couchbase.connection;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.JOptionPane;
import org.netbeans.modules.couchbase.CouchbaseRootNode;
import org.openide.awt.StatusDisplayer;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.NbPreferences;
import org.openide.util.RequestProcessor;

public class ConnectionChildFactory extends ChildFactory.Detachable<Cluster> implements PreferenceChangeListener {

    private final List<Cluster> clusters;
    private String clusterAddress;
    private String clusterName;
    private ClusterManager clusterManager;

    public ConnectionChildFactory() {
        this.clusters = new ArrayList<Cluster>();
    }

    @Override
    protected boolean createKeys(List<Cluster> list) {
        list.addAll(clusters);
        return true;
    }

    @Override
    protected Node createNodeForKey(Cluster cluster) {
        return new ConnectionNode(cluster, clusterManager, clusterName);
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent evt) {
        if (evt.getKey().equals("clusterName")) {
            clusterName = evt.getNewValue();
            try {
                clusterAddress = NbPreferences.forModule(CouchbaseRootNode.class).get("clusterAddress", "localhost");
                String login = NbPreferences.forModule(CouchbaseRootNode.class).get("clusterLogin", "error!");
                String password = NbPreferences.forModule(CouchbaseRootNode.class).get("clusterPassword", "error!");
                CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                        .queryEnabled(true)
                        .build();
                Cluster cluster = CouchbaseCluster.create(env, clusterAddress);
                clusterManager = cluster.clusterManager(login, password);
                clusters.add(cluster);
                refresh(true);
                StatusDisplayer.getDefault().setStatusText("New cluster.");
            } catch (com.couchbase.client.java.error.InvalidPasswordException e) {
                RequestProcessor.getDefault().post(new Runnable() {
                    @Override
                    public void run() {
                        String msg = "Invalid login credentials";
                        JOptionPane.showMessageDialog(null, msg);
                        StatusDisplayer.getDefault().setStatusText(msg);
                    }
                });
            }
        }
    }

    @Override
    protected void addNotify() {
        NbPreferences.forModule(CouchbaseRootNode.class).addPreferenceChangeListener(this);
    }

    @Override
    protected void removeNotify() {
        NbPreferences.forModule(CouchbaseRootNode.class).removePreferenceChangeListener(this);
    }

}
