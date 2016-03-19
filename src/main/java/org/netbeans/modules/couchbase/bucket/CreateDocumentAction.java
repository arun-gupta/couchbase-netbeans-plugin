package org.netbeans.modules.couchbase.bucket;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;
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

    private final Bucket bucket;

    private static AtomicInteger _integer = new AtomicInteger(0);

    public CreateDocumentAction(Bucket context) {
        this.bucket = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        int count = getNextCount();
        JsonObject content = JsonObject.empty().put("name", "Michael");
        JsonDocument doc = JsonDocument.create(String.valueOf(count), content);
        bucket.insert(doc);
        RefreshBucketListTrigger.trigger();
        StatusDisplayer.getDefault().setStatusText("New document: " + count);
    }

    private static int getNextCount() {
        return _integer.incrementAndGet();
    }

}
