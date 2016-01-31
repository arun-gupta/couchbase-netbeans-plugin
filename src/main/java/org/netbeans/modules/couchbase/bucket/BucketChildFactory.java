package org.netbeans.modules.couchbase.bucket;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import java.beans.IntrospectionException;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.modules.couchbase.connection.ConnectionUtilities;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

public class BucketChildFactory extends ChildFactory.Detachable<Bucket> {

    private final Cluster cluster;

    private ChangeListener listener;

    public BucketChildFactory(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    protected void addNotify() {
        ConnectionUtilities.addChangeListener(listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ev) {
                refresh(true);
            }
        });
    }

    @Override
    protected void removeNotify() {
        if (listener != null) {
            ConnectionUtilities.removeChangeListener(listener);
            listener = null;
        }
    }

    @Override
    protected boolean createKeys(List<Bucket> list) {
        String login = "Administrator";
        String password = "adminadmin";
        ClusterManager cmgr = cluster.clusterManager(login, password);
        for (BucketSettings bs : cmgr.getBuckets()) {
            String bucketName = bs.name();
            if (!bucketName.equals("default")) {
                list.add(cluster.openBucket(bs.name()));
            }
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(Bucket key) {
        BucketNode node = null;
        try {
            node = new BucketNode(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return node;
    }

}
