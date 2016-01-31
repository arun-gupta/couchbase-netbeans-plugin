package org.netbeans.modules.couchbase.connection;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.modules.couchbase.bucket.BucketChildFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.StatusDisplayer;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

public class ConnectionNode extends AbstractNode {

    @StaticResource
    private static final String ICON = "org/netbeans/modules/couchbase/connection.png";
    private final Cluster cluster;

    public ConnectionNode(Cluster cluster, String name) {
        super(Children.create(new BucketChildFactory(cluster), true));
        this.cluster = cluster;
        setDisplayName(name);
        setIconBaseWithExtension(ICON);
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{new AbstractAction("Create Bucket...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                NotifyDescriptor.InputLine nd = new NotifyDescriptor.InputLine(
                        "Bucket name:",
                        "Create bucket");
                DialogDisplayer.getDefault().notify(nd);
                String bucketName = nd.getInputText();
                BucketSettings settings = DefaultBucketSettings
                        .builder()
                        .name(bucketName)
                        .quota(100)
                        .build();
                ClusterManager cmgr = cluster.clusterManager("Administrator", "adminadmin");
                cmgr.insertBucket(settings);
                ConnectionUtilities.changed();
                StatusDisplayer.getDefault().setStatusText("New: " + bucketName);
            }
        }};
    }

    @Override
    public Action getPreferredAction() {
        return null;
    }

}
