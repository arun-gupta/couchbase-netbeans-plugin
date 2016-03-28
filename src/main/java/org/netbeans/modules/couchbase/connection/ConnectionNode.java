package org.netbeans.modules.couchbase.connection;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import com.couchbase.client.java.query.N1qlQuery;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.progress.BaseProgressUtils;
import org.netbeans.modules.couchbase.bucket.BucketChildFactory;
import org.netbeans.modules.couchbase.bucket.RefreshBucketListTrigger;
import org.netbeans.modules.couchbase.connection.rename.RenameContainerAction;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.actions.NewAction;
import org.openide.actions.OpenLocalExplorerAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.NewType;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class ConnectionNode extends AbstractNode {

    @StaticResource
    private static final String ICON = "org/netbeans/modules/couchbase/connection.png";

    private ClusterManager cmgr;
    private final Cluster cluster;

    public ConnectionNode(Cluster cluster, ClusterManager cmgr, String name) {
        this(cluster, cmgr, name, new InstanceContent());
    }

    public ConnectionNode(
            Cluster cluster,
            ClusterManager cmgr,
            String name,
            InstanceContent content) {
        super(Children.create(
                new BucketChildFactory(cluster, cmgr), true),
                new AbstractLookup(content));
        this.cmgr = cmgr;
        this.cluster = cluster;
        content.add(this);
        content.add(cluster);
        setDisplayName(name);
        setIconBaseWithExtension(ICON);
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{
            SystemAction.get(NewAction.class),
            SystemAction.get(RenameContainerAction.class),
            SystemAction.get(OpenLocalExplorerAction.class),
            null,
            Utilities.actionsForPath("Actions/Connection").get(0),
            Utilities.actionsForPath("Actions/Connection").get(1)
        };
    }

    @Override
    public boolean canRename() {
        return true;
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
                    final CreateBucketForm cbf = new CreateBucketForm();
                    NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(
                            cbf,
                            "Create bucket");
                    JButton ok = new JButton();
                    ok.setText("OK");
                    JButton cancel = new JButton();
                    cancel.setText("Cancel");
                    nd.setOptions(new Object[]{ok, cancel});
                    ok.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            final Boolean indexable = cbf.getIndexable();
                            final String bucketName = cbf.getBucketName();
                            final BucketSettings settings = DefaultBucketSettings
                                    .builder()
                                    .name(bucketName)
                                    .quota(100)
                                    .build();
                            try {
                                RequestProcessor.getDefault().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        BaseProgressUtils.showProgressDialogAndRun(new Runnable() {
                                            @Override
                                            public void run() {
                                                cmgr.insertBucket(settings);
                                                NbPreferences.forModule(ConnectionNode.class).put("bucketName", bucketName);
                                                Bucket bucket = cluster.openBucket(bucketName);
                                                if (indexable) {
                                                    bucket.query(N1qlQuery.simple(String.format("create primary index on `" + bucketName + "`", bucketName)));
                                                }
                                                RefreshBucketListTrigger.trigger();
                                            }
                                        }, "Creating bucket '" + bucketName + "'...");
                                    }
                                });
                            } catch (com.couchbase.client.core.CouchbaseException f) {
                                JOptionPane.showMessageDialog(null, f.getMessage());
                            }
                        }
                    });
                    DialogDisplayer.getDefault().notify(nd);
                }
            }
        };
    }

    @Override
    public Action getPreferredAction() {
        return null;
    }

}
