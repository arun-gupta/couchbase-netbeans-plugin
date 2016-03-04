package org.netbeans.modules.couchbase.connection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;
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
        displayName = "#CTL_ServerDefaultsAction"
)
@Messages("CTL_ServerDefaultsAction=Server Defaults")
public final class ServerDefaultsAction implements ActionListener {

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
