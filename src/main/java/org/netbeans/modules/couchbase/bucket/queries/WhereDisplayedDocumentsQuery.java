package org.netbeans.modules.couchbase.bucket.queries;

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
import org.netbeans.modules.couchbase.bucket.BucketNode;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.Lookups;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public class WhereDisplayedDocumentsQuery extends TopComponent implements ActionListener {

    private final Bucket bucket;

    public WhereDisplayedDocumentsQuery(final Bucket bucket) {
        this.bucket = bucket;
        setLayout(new MigLayout());
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
        add(fromLabel);
        add(whereField);
        add(toLabel);
        add(equalsField);
        add(okButton);
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
            if (tc.getLookup().lookup(Bucket.class) == bucket && tc.getClass().getSimpleName().equals("WhereDisplayedDocumentsQuery")) {
                return tc;
            }
        }
        return null;
    }

}
