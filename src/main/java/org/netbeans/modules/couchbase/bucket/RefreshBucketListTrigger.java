package org.netbeans.modules.couchbase.bucket;

import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;

public class RefreshBucketListTrigger {

    private static final ChangeSupport cs = new ChangeSupport(RefreshBucketListTrigger.class);

    public static void addChangeListener(ChangeListener listener) {
        cs.addChangeListener(listener);
    }

    public static void removeChangeListener(ChangeListener listener) {
        cs.removeChangeListener(listener);
    }

    public static void trigger() {
        cs.fireChange();
    }

}
