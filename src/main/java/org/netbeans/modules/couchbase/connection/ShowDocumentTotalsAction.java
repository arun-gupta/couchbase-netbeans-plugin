package org.netbeans.modules.couchbase.connection;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;

@ActionID(
        category = "Connection",
        id = "org.netbeans.modules.couchbase.connection.ShowDocumentTotalsAction"
)
@ActionRegistration(
        displayName = "#CTL_ShowDocumentTotalsAction"
)
@Messages("CTL_ShowDocumentTotalsAction=Include Document Totals in Display Names")
public final class ShowDocumentTotalsAction extends AbstractAction implements Presenter.Popup, ItemListener {

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
        // not used
    }

    @Override
    public JMenuItem getPopupPresenter() {
        final JCheckBoxMenuItem item
                = new JCheckBoxMenuItem(Bundle.CTL_ShowDocumentTotalsAction(), false);
        item.addItemListener(this);
        return item;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        AbstractButton button = (AbstractButton) e.getItem();
        if (button.isSelected()) {
            StatusDisplayer.getDefault().setStatusText("selected");
//            Children bucketKids = contextNode.getChildren();
//            for (int i = 0; i < bucketKids.getNodesCount(); i++) {
//                Node bucketNode = bucketKids.getNodeAt(i);
//                String name = bucketNode.getDisplayName();
////                bucketNode.setDisplayName(name + " (*)");
//            }
        } else {
            StatusDisplayer.getDefault().setStatusText("not selected");
//            Children bucketKids = contextNode.getChildren();
//            for (int i = 0; i < bucketKids.getNodesCount(); i++) {
//                Node bucketNode = bucketKids.getNodeAt(i);
//                String name = bucketNode.getDisplayName();
////                bucketNode.setDisplayName(name.replace(" (*)", ""));
//            }
        }
    }

}
