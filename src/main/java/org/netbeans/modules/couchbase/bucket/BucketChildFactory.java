package org.netbeans.modules.couchbase.bucket;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.modules.couchbase.CouchbaseRootNode;
import org.netbeans.modules.couchbase.connection.ConnectionNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

public class BucketChildFactory extends ChildFactory.Detachable<Bucket> implements NodeListener {

    private final Cluster cluster;

    private ChangeListener listener;

    private ArrayList<Bucket> bucketList;

    private ClusterManager cmgr;

    public BucketChildFactory(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    protected void addNotify() {
        this.bucketList = new ArrayList<Bucket>();
        String login = NbPreferences.forModule(CouchbaseRootNode.class).get("clusterLogin", "error!");
        String password = NbPreferences.forModule(CouchbaseRootNode.class).get("clusterPassword", "error!");
        cmgr = cluster.clusterManager(login, password);
        for (BucketSettings bs : cmgr.getBuckets()) {
            Bucket bucket = cluster.openBucket(bs.name());
            bucketList.add(bucket);
        }
        RefreshBucketListTrigger.addChangeListener(listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ev) {
                final String bucketName = NbPreferences.forModule(ConnectionNode.class).get("bucketName", "error!");
                if (!bucketName.equals("error!")) {
                    try {
                        final Bucket newBucket = cluster.openBucket(bucketName);
                        if (!bucketList.contains(newBucket)) {
                            bucketList.add(newBucket);
                        }
                        refresh(true);
                    } catch (java.lang.RuntimeException e) {
                        JOptionPane.showMessageDialog(null, "Time out, please try again.");
                    }
                }
            }
        });
    }

    @Override
    protected void removeNotify() {
        if (listener != null) {
            RefreshBucketListTrigger.removeChangeListener(listener);
            listener = null;
        }
    }

    @Override
    protected boolean createKeys(List<Bucket> list) {
        Collections.sort(bucketList, new BucketNameComparator());
        list.addAll(bucketList);
        return true;
    }

    public class BucketNameComparator implements Comparator<Bucket> {
        public int compare(Bucket bucket1, Bucket bucket2) {
            return bucket1.name().compareTo(bucket2.name());
        }
    }

    @Override
    protected Node createNodeForKey(Bucket key) {
        BucketNode node = null;
        try {
            node = new BucketNode(key);
            node.addNodeListener(this);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return node;
    }

    @Override
    public void nodeDestroyed(NodeEvent ne) {
        Bucket removedBucket = ne.getNode().getLookup().lookup(Bucket.class);
        final String name = removedBucket.name();
        ProgressHandle handle = ProgressHandleFactory.createSystemHandle("Removing bucket " + name + "...");
        handle.start();
        cmgr.removeBucket(name);
        bucketList.remove(removedBucket);
        NbPreferences.forModule(ConnectionNode.class).remove("bucketName");
        refresh(true);
        handle.finish();
    }

    @Override
    public void childrenAdded(NodeMemberEvent ne) {
    }

    @Override
    public void childrenRemoved(NodeMemberEvent nme) {
    }

    @Override
    public void childrenReordered(NodeReorderEvent nre) {
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

}
