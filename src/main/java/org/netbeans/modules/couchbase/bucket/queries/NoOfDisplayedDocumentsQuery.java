package org.netbeans.modules.couchbase.bucket.queries;

import com.couchbase.client.java.Bucket;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import org.netbeans.modules.couchbase.bucket.BucketNode;
import org.openide.util.NbPreferences;

public class NoOfDisplayedDocumentsQuery extends JPanel {

    public NoOfDisplayedDocumentsQuery(final Bucket bucket) {
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
    }

}
