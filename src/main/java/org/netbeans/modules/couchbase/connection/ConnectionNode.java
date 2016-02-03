package org.netbeans.modules.couchbase.connection;

import com.couchbase.client.java.Cluster;
import java.util.List;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.modules.couchbase.bucket.BucketChildFactory;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

public class ConnectionNode extends AbstractNode {

    @StaticResource
    private static final String ICON = "org/netbeans/modules/couchbase/connection.png";

    public ConnectionNode(Cluster cluster, String name) {
        super(Children.create(new BucketChildFactory(cluster), true), Lookups.singleton(cluster));
        setDisplayName(name);
        setIconBaseWithExtension(ICON);
    }

    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> connectionActions = Utilities.actionsForPath("Actions/Connection");
        return connectionActions.toArray(new Action[connectionActions.size()]);
    }

    @Override
    public Action getPreferredAction() {
        return null;
    }

}
