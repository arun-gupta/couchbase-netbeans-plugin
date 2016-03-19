package org.netbeans.modules.couchbase.connection;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.modules.couchbase.CouchbaseRootNode;
import org.netbeans.modules.couchbase.bucket.BucketChildFactory;
import org.netbeans.modules.couchbase.bucket.RefreshBucketListTrigger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.actions.NewAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.NewType;
import org.openide.util.lookup.Lookups;

public class ConnectionNode extends AbstractNode {

    @StaticResource
    private static final String ICON = "org/netbeans/modules/couchbase/connection.png";
    private final Cluster cluster;

    public ConnectionNode(Cluster cluster, String name) {
        super(Children.create(new BucketChildFactory(cluster), false), Lookups.singleton(cluster));
        this.cluster = cluster;
        setDisplayName(name);
        setIconBaseWithExtension(ICON);
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{
            SystemAction.get(NewAction.class),
            null,
            Utilities.actionsForPath("Actions/Connection").get(0)
        };
    }

    @NbBundle.Messages({
        "LBL_Title=Bucket...",
        "LBL_Text=Enter Bucket Name:"})
    @Override
    public NewType[] getNewTypes() {
        return new NewType[]{
            new NewType() {
                @Override
                public String getName() {
                    return "Bucket...";
                }

                @Override
                public void create() throws IOException {
                    NotifyDescriptor.InputLine nd = new NotifyDescriptor.InputLine(
                            "Bucket name:",
                            "Create bucket");
                    DialogDisplayer.getDefault().notify(nd);
                    final String bucketName = nd.getInputText();
                    final BucketSettings settings = DefaultBucketSettings
                            .builder()
                            .name(bucketName)
                            .quota(100)
                            .build();
                    String login = NbPreferences.forModule(CouchbaseRootNode.class).get("clusterLogin", "error!");
                    String password = NbPreferences.forModule(CouchbaseRootNode.class).get("clusterPassword", "error!");
                    final ClusterManager cmgr = cluster.clusterManager(login, password);
                    try {
                        System.out.println("status: one");
                        cmgr.insertBucket(settings);
                        System.out.println("status: two");
                        NbPreferences.forModule(ConnectionNode.class).put("bucketName", bucketName);
                        System.out.println("status: three");
                        RefreshBucketListTrigger.trigger();
                        System.out.println("status: four");
                    } catch (com.couchbase.client.core.CouchbaseException f) {
                        JOptionPane.showMessageDialog(null, f.getMessage());
                    }
                }
            }
        };
    }

    @Override
    public Action getPreferredAction() {
        return null;
    }

}
