package org.netbeans.modules.couchbase.bucket;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import java.beans.IntrospectionException;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.modules.couchbase.CouchbaseRootNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

public class BucketChildFactory extends ChildFactory.Detachable<Bucket> {

    private final Cluster cluster;

    private ChangeListener listener;

    public BucketChildFactory(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    protected void addNotify() {
        RefreshBucketListTrigger.addChangeListener(listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ev) {
                refresh(true);
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
        String login = NbPreferences.forModule(CouchbaseRootNode.class).get("clusterLogin", "error!");
        String password = NbPreferences.forModule(CouchbaseRootNode.class).get("clusterPassword", "error!");
        ClusterManager cmgr = cluster.clusterManager(login, password);
        for (BucketSettings bs : cmgr.getBuckets()) {
            Bucket bucket = cluster.openBucket(bs.name());
            //“select count(*)  from system:indexes where keyspace_id = `” + bucket.name() + “`“
            //bucket.query(N1qlQuery.simple(String.format("create primary index on `%s`", bucket.name())));
           
            
//            String bucketName = bs.name();
//            if (!bucketName.equals("default")) {
                list.add(bucket);
//            }
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
