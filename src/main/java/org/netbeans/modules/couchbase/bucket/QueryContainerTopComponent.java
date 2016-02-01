package org.netbeans.modules.couchbase.bucket;

import com.couchbase.client.java.Bucket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
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
        setLayout(new MigLayout(""));
        noOfDisplayedDocumentsQuery(bucket);
        rangeDisplayedDocumentsQuery(bucket);
        associateLookup(Lookups.singleton(bucket));
    }

    private void noOfDisplayedDocumentsQuery(final Bucket bucket1) {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Basic Query"));
        JLabel label = new JLabel("No. of displayed documents:");
        final JTextField noOfDisplayedDocuments = new JTextField(50);
        noOfDisplayedDocuments.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NbPreferences.forModule(BucketNode.class).put(bucket1.name() + "-numberOfRows", noOfDisplayedDocuments.getText());
            }
        });
        panel.add(label);
        panel.add(noOfDisplayedDocuments);
        add(panel, "wrap");
    }
    
    private void rangeDisplayedDocumentsQuery(final Bucket bucket) {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder("Complex Query"));
        JLabel fromLabel = new JLabel("Where:");
        final JTextField whereField = new JTextField(20);
        JLabel toLabel = new JLabel("Equals:");
        final JTextField equalsField = new JTextField(20);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NbPreferences.forModule(BucketNode.class).put(bucket.name() + "-where", whereField.getText());
                NbPreferences.forModule(BucketNode.class).put(bucket.name() + "-equals", equalsField.getText());
            }
        });
        panel.add(fromLabel);
        panel.add(whereField);
        panel.add(toLabel);
        panel.add(equalsField);
        panel.add(okButton);
        add(panel, "wrap");
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
