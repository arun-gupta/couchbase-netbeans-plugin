package org.netbeans.modules.couchbase.bucket;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.awt.DynamicMenuContent;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

@ActionID(
        category = "Bucket",
        id = "org.netbeans.modules.couchbase.bucket.CreatePrimaryIndexAction"
)
@ActionRegistration(
        lazy = false,
        asynchronous = true,
        displayName = "#CTL_CreatePrimaryIndexAction"
)
@Messages("CTL_CreatePrimaryIndexAction=Set Primary Index")
public final class CreatePrimaryIndexAction extends AbstractAction implements ContextAwareAction {

    private final Bucket bucket;

    public CreatePrimaryIndexAction() {
        this(Utilities.actionsGlobalContext());
    }

    public CreatePrimaryIndexAction(Lookup context) {
        super(Bundle.CTL_CreatePrimaryIndexAction());
        this.bucket = context.lookup(Bucket.class);
        N1qlQueryResult query = bucket.query(N1qlQuery.simple(String.format("select * from system:indexes where keyspace_id = '" + bucket.name() + "'", bucket.name())));
        int size = query.allRows().size();
        if (size > 0) {
            setEnabled(false);
        } else {
            setEnabled(true);
        }
        putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String bucketName = bucket.name();
        bucket.query(N1qlQuery.simple(String.format("create primary index on `" + bucketName + "`", bucketName)));
        RefreshBucketListTrigger.trigger();
        System.out.println("New primary index on: " + bucketName);
    }

    @Override
    public Action createContextAwareInstance(Lookup context) {
        return new CreatePrimaryIndexAction(context);
    }

}
