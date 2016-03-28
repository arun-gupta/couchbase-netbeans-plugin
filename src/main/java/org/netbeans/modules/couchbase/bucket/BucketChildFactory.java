package org.netbeans.modules.couchbase.bucket;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.progress.BaseProgressUtils;
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
import org.openide.util.RequestProcessor;
import rx.Observable;
import rx.functions.Func1;

public class BucketChildFactory extends ChildFactory.Detachable<Bucket> implements NodeListener {

    private final Cluster cluster;

    private ChangeListener listener;

    private ArrayList<Bucket> bucketList;

    private ClusterManager cmgr;

    public BucketChildFactory(Cluster cluster, ClusterManager cmgr) {
        this.cluster = cluster;
        this.cmgr = cmgr;
        this.bucketList = new ArrayList<Bucket>();
        for (BucketSettings bs : cmgr.getBuckets()) {
            if (!bs.name().equals("default")) {
                Bucket bucket = cluster.openBucket(bs.name());
                bucketList.add(bucket);
            }
        }
    }

    @Override
    protected void addNotify() {
        RefreshBucketListTrigger.addChangeListener(listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ev) {
                RequestProcessor.getDefault().post(new Runnable() {
                    @Override
                    public void run() {
                        BaseProgressUtils.showProgressDialogAndRun(new Runnable() {
                            @Override
                            public void run() {
                                final String bucketName = NbPreferences.forModule(ConnectionNode.class
                                ).get("bucketName", "error!");
                                if (!bucketName.equals("error!")) {
                                    if (cmgr.hasBucket(bucketName)) {
                                        bucketList.add(cluster.openBucket(bucketName, 1000, TimeUnit.SECONDS));
                                        refresh(true);
                                    }
                                }
                            }
                        }, "Refreshing buckets...");
                    }
                });
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
        final Bucket removedBucket = ne.getNode().getLookup().lookup(Bucket.class);
        final String bucketName = removedBucket.name();
        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                BaseProgressUtils.showProgressDialogAndRun(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (cmgr.hasBucket(bucketName)) {
                                cmgr.removeBucket(bucketName);
                                bucketList.remove(removedBucket);
                                refresh(true);
                            }
                        } catch (com.couchbase.client.core.endpoint.kv.AuthenticationException e) {
                            JOptionPane.showMessageDialog(null, "error");
                        }
                    }
                }, "Removing bucket '" + bucketName + "'...");
            }
        });
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
