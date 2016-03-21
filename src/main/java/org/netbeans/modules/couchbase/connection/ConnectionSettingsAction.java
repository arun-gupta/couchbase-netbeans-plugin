package org.netbeans.modules.couchbase.connection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;
import org.openide.util.actions.Presenter;
import org.openide.util.actions.SystemAction;

@ActionID(
        category = "Connection",
        id = "org.netbeans.modules.couchbase.connection.PropertiesAction"
)
@ActionRegistration(
        lazy = false,
        displayName = "not-used"
)
public final class ConnectionSettingsAction extends AbstractAction implements Presenter.Popup {

    @Override
    public void actionPerformed(ActionEvent ev) {
    }

    @Override
    public JMenuItem getPopupPresenter() {
        JMenu menu = new JMenu("Settings");
        menu.add(new NoOfDocumentsDisplayedAction());
        menu.add(new AbstractAction("Include Document Totals in Display Names") {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        return menu;
    }

}
