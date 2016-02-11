package org.netbeans.modules.couchbase.connection;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.cluster.ClusterManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;
import org.netbeans.modules.couchbase.CouchbaseRootNode;
import org.netbeans.modules.couchbase.bucket.BucketNode;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;

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
        Preferences pref = NbPreferences.forModule(ConnectionNode.class);
        String clusterName = pref.get("clusterName", "error!");
        String clusterDefaultNumber = pref.get(clusterName + "-defaultNumber", "3");
        NotifyDescriptor.InputLine line
                = new NotifyDescriptor.InputLine(
                        "Default Number of Documents:",
                        "Server-Level Setting"
                );
        line.setInputText(clusterDefaultNumber);
        Object result = DialogDisplayer.getDefault().notify(line);
        if (NotifyDescriptor.YES_OPTION.equals(result)) {
            String inputText = line.getInputText();
            pref.putInt(clusterName + "-defaultNumber", Integer.parseInt(inputText));
        }
    }

}
