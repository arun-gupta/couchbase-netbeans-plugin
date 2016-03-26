package org.netbeans.modules.couchbase.document;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import org.openide.awt.StatusDisplayer;

public class DocumentSavePanel extends javax.swing.JPanel implements ItemListener {

    public DocumentSavePanel() {
        initComponents();
        replaceButton.addItemListener(this);
        saveButton.addItemListener(this);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        replaceButton = new javax.swing.JRadioButton();
        saveButton = new javax.swing.JRadioButton();

        buttonGroup1.add(replaceButton);
        org.openide.awt.Mnemonics.setLocalizedText(replaceButton, org.openide.util.NbBundle.getMessage(DocumentSavePanel.class, "DocumentSavePanel.replaceButton.text")); // NOI18N

        buttonGroup1.add(saveButton);
        org.openide.awt.Mnemonics.setLocalizedText(saveButton, org.openide.util.NbBundle.getMessage(DocumentSavePanel.class, "DocumentSavePanel.saveButton.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(replaceButton)
                    .addComponent(saveButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(replaceButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton replaceButton;
    private javax.swing.JRadioButton saveButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();
        if (source == replaceButton) {
            StatusDisplayer.getDefault().setStatusText("Replace...");
        } else if (source == saveButton) {
            StatusDisplayer.getDefault().setStatusText("Save...");
        }
    }
    
}
