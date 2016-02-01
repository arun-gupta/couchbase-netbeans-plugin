package org.netbeans.modules.couchbase.bucket;

import com.couchbase.client.java.Bucket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.Lookups;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@TopComponent.Description(
        preferredID = "QueryContainerTopComponent",
        iconBase = "org/netbeans/modules/couchbase/database.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "navigator", openAtStartup = false)
public class QueryContainerTopComponent extends ObjectTopComponent implements ActionListener {

    private Bucket bucket;

    public QueryContainerTopComponent() {
    }

    public QueryContainerTopComponent(final Bucket bucket) {
        this.bucket = bucket;
        setLayout(new MigLayout());
        JLabel label = new JLabel("No. of displayed documents:");
        final JTextField noOfDisplayedDocuments = new JTextField(50);
        noOfDisplayedDocuments.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NbPreferences.forModule(BucketNode.class).put(bucket.name() + "-numberOfRows", noOfDisplayedDocuments.getText());
            }
        });
        add(label);
        add(noOfDisplayedDocuments);
        associateLookup(Lookups.singleton(bucket));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setDisplayName(bucket.name());
        Mode navigatorMode = WindowManager.getDefault().findMode("navigator");
        navigatorMode.dockInto(this);
        TopComponent tc = findTopComponent(bucket);
        if (tc == null) {
            this.open();
            this.requestActive();
        } else {
            tc.requestActive();
        }
    }

    private TopComponent findTopComponent(Bucket bucket) {
        Set<TopComponent> openTopComponents = WindowManager.getDefault().getRegistry().getOpened();
        for (TopComponent tc : openTopComponents) {
            if (tc.getLookup().lookup(Bucket.class) == bucket && tc.getLookup().lookup(BucketNode.class) == null) {
                return tc;
            }
        }
        return null;
    }

}
