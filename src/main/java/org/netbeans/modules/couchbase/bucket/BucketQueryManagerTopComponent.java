package org.netbeans.modules.couchbase.bucket;

import com.couchbase.client.java.Bucket;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import javax.swing.AbstractAction;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;
import org.openide.util.Utilities;

@ConvertAsProperties(
        dtd = "-//org.netbeans.modules.couchbase.bucket//BucketQueryManager//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "BucketQueryManagerTopComponent",
        iconBase = "org/netbeans/modules/couchbase/database.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "navigator", openAtStartup = false)
@ActionID(category = "Window", id = "org.netbeans.modules.couchbase.bucket.BucketQueryManagerTopComponent")
@Messages({
    "CTL_BucketQueryManagerTopComponent=Bucket Query Manager"
})
public final class BucketQueryManagerTopComponent extends TopComponent implements LookupListener {

    private Bucket bucket;

    public BucketQueryManagerTopComponent() {
        initComponents();
        setName(Bundle.CTL_BucketQueryManagerTopComponent());

        noOfDisplayedDocuments.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NbPreferences.forModule(BucketNode.class).put(
                        bucket.name() + "-numberOfRows",
                        noOfDisplayedDocuments.getText());
            }
        });

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        noOfDisplayedDocuments = new javax.swing.JTextField();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(BucketQueryManagerTopComponent.class, "BucketQueryManagerTopComponent.jLabel1.text")); // NOI18N

        noOfDisplayedDocuments.setText(org.openide.util.NbBundle.getMessage(BucketQueryManagerTopComponent.class, "BucketQueryManagerTopComponent.noOfDisplayedDocuments.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noOfDisplayedDocuments, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(noOfDisplayedDocuments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(270, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField noOfDisplayedDocuments;
    // End of variables declaration//GEN-END:variables
    Lookup.Result<Bucket> bucketsInLookup;

    @Override
    public void resultChanged(LookupEvent le) {
        if (bucketsInLookup.allInstances().iterator().hasNext()) {
            Bucket bucket = bucketsInLookup.allInstances().iterator().next();
            this.bucket = bucket;
            setDisplayName("Manage " + bucket.name());
        }
    }

    @Override
    public void componentOpened() {
        bucketsInLookup = Utilities.actionsGlobalContext().lookupResult(Bucket.class);
        bucketsInLookup.addLookupListener(this);
    }

    @Override
    public void componentClosed() {
        bucketsInLookup.removeLookupListener(this);
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

}
