package org.netbeans.modules.couchbase.connection;

import org.netbeans.modules.couchbase.bucket.RefreshBucketListTrigger;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.modules.couchbase.CouchbaseRootNode;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.awt.StatusDisplayer;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;

@ActionID(
        category = "Connection",
        id = "org.netbeans.modules.couchbase.connection.CreateBucketAction"
)
@ActionRegistration(
        displayName = "#CTL_CreateBucketAction"
)
@Messages("CTL_CreateBucketAction=Create Bucket")
public final class CreateBucketAction implements ActionListener {

    private final Cluster cluster;

    public CreateBucketAction(Cluster cluster) {
        this.cluster = cluster;
    }

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
        String login = NbPreferences.forModule(CouchbaseRootNode.class).get("clusterLogin", "error!");
        String password = NbPreferences.forModule(CouchbaseRootNode.class).get("clusterPassword", "error!");
        ClusterManager cmgr = cluster.clusterManager(login, password);
        cmgr.insertBucket(settings);
        RefreshBucketListTrigger.trigger();
        StatusDisplayer.getDefault().setStatusText("New: " + bucketName);
    }

}
