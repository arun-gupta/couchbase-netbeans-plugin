package org.netbeans.modules.couchbase.connection;

import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;

public class ConnectionUtilities {

    private static final ChangeSupport cs = new ChangeSupport(ConnectionUtilities.class);

    public static void addChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }

    public static void removeChangeListener(ChangeListener listener) {
        cs.removeChangeListener(listener);
    }

    public static void changed() {
        cs.fireChange();
    }

}
