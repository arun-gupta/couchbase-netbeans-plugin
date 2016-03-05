package org.netbeans.modules.couchbase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.modules.couchbase.connection.ConnectionChildFactory;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JButton;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.modules.couchbase.connection.RefreshConnectionListTrigger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.actions.NewAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.NewType;

@NbBundle.Messages({"LBL_Connections=Couchbase"})
public class CouchbaseRootNode extends AbstractNode {

    private String serverName;
    private String address;
    private String login;
    private String password;

    private static final CouchbaseRootNode DEFAULT = new CouchbaseRootNode();
    
    @StaticResource
    private static final String ICON = "org/netbeans/modules/couchbase/couchbase-root-icon.png";

     public static CouchbaseRootNode getDefault() {
        return DEFAULT;
    }
    
    public CouchbaseRootNode() {
        super(Children.create(new ConnectionChildFactory(), true));
        setDisplayName(Bundle.LBL_Connections());
        setIconBaseWithExtension(ICON);
    }

    @Override
    public Action getPreferredAction() {
        return SystemAction.get(NewAction.class);
    }
    
    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{SystemAction.get(NewAction.class)};
    }

    @NbBundle.Messages({
        "LBL_Title=Couchbase Node...",
        "LBL_Text=Enter Cluster Name:"})
    @Override
    public NewType[] getNewTypes() {
        return new NewType[]{
            new NewType() {
                @Override
                public String getName() {
                    return Bundle.LBL_Title();
                }
                @Override
                public void create() throws IOException {
                    final LoginForm form = new LoginForm();
                    NotifyDescriptor.Confirmation nd = new NotifyDescriptor.Confirmation(
                            form,
                            Bundle.LBL_Title());
                    JButton ok = new JButton();
                    ok.setText("OK");
                    JButton cancel = new JButton();
                    cancel.setText("Cancel");
                    nd.setOptions(new Object[]{ok, cancel});
                    ok.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            serverName = form.getServerName();
                            address = form.getAddress();
                            login = form.getLogin();
                            password = form.getPassword();
                            NbPreferences.forModule(CouchbaseRootNode.class).put("clusterName", serverName);
                            NbPreferences.forModule(CouchbaseRootNode.class).put("clusterAddress", address);
                            NbPreferences.forModule(CouchbaseRootNode.class).put("clusterLogin", login);
                            NbPreferences.forModule(CouchbaseRootNode.class).put("clusterPassword", password);
                            RefreshConnectionListTrigger.trigger();
                        }
                    });
                    DialogDisplayer.getDefault().notify(nd);
                }
            }
        };
    }

}
