package org.netbeans.modules.couchbase.connection;

import com.couchbase.client.java.Cluster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Connection",
        id = "org.netbeans.modules.couchbase.connection.PropertiesAction"
)
@ActionRegistration(
        displayName = "#CTL_PropertiesAction"
)
@Messages("CTL_PropertiesAction=Properties")
public final class PropertiesAction implements ActionListener {

    private final Cluster context;

    public PropertiesAction(Cluster context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        NotifyDescriptor.InputLine line = 
                new NotifyDescriptor.InputLine(
                        "Default Number of Documents:",
                        "Server-Level Setting"
                );
        DialogDisplayer.getDefault().notify(line);
    }
    
}
