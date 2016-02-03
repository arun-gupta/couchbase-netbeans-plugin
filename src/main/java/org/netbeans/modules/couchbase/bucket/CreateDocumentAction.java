package org.netbeans.modules.couchbase.bucket;

import com.couchbase.client.java.Bucket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.awt.StatusDisplayer;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Bucket",
        id = "org.netbeans.modules.couchbase.bucket.CreateDocumentAction"
)
@ActionRegistration(
        displayName = "#CTL_CreateDocumentAction"
)
@Messages("CTL_CreateDocumentAction=Create Document")
public final class CreateDocumentAction implements ActionListener {

    private final Bucket context;

    public CreateDocumentAction(Bucket context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        StatusDisplayer.getDefault().setStatusText("Create document...");
    }

}
