package org.netbeans.modules.couchbase.connection;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(
        category = "Connection",
        id = "org.netbeans.modules.couchbase.connection.ShowDocumentTotalsAction"
)
@ActionRegistration(
        displayName = "#CTL_ShowDocumentTotalsAction"
)
@Messages("CTL_ShowDocumentTotalsAction=Include Document Totals in Display Names")
public final class ShowDocumentTotalsAction extends AbstractAction {

    private final ConnectionNode contextNode;

    public ShowDocumentTotalsAction() {
        this(Utilities.actionsGlobalContext());
    }

    public ShowDocumentTotalsAction(Lookup lookup) {
        super(Bundle.CTL_ShowDocumentTotalsAction());
        this.contextNode = lookup.lookup(ConnectionNode.class);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        Children bucketKids = contextNode.getChildren();
        for (int i = 0; i < bucketKids.getNodesCount(); i++) {
            Node bucketNode = bucketKids.getNodeAt(i);
            String name = bucketNode.getDisplayName();
            bucketNode.setDisplayName(name + " (*)");
        }
    }

}
