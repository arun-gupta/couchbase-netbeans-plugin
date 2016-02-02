package org.netbeans.modules.couchbase.bucket.queries;

import com.couchbase.client.java.Bucket;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;
import org.netbeans.modules.couchbase.bucket.BucketNode;
import org.openide.util.NbPreferences;

public class WhereDisplayedDocumentsQuery extends JPanel {

    public WhereDisplayedDocumentsQuery(final Bucket bucket) {
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
    }

}
