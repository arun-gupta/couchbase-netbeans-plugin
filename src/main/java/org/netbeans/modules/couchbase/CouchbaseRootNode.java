package org.netbeans.modules.couchbase;

import org.netbeans.modules.couchbase.connection.ConnectionChildFactory;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.core.ide.ServicesTabNodeRegistration;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

@ServicesTabNodeRegistration(
        name = "Couchbase",
        displayName = "Couchbase",
        iconResource = "org/netbeans/modules/couchbase/couchbase-root-icon.png")
public class CouchbaseRootNode extends AbstractNode {

    @StaticResource
    private static final String ICON = "org/netbeans/modules/couchbase/couchbase-root-icon.png";

    public CouchbaseRootNode() {
        super(Children.create(new ConnectionChildFactory(), true));
        setDisplayName("Couchbase");
        setIconBaseWithExtension(ICON);
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{new AbstractAction("Connect to Couchbase") {
            @Override
            public void actionPerformed(ActionEvent e) {
//                CouchbaseConnect.main();
            }
        }};
    }

}
