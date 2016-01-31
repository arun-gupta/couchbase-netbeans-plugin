package org.netbeans.modules.couchbase.bucket;

import org.netbeans.modules.couchbase.row.RowChildFactory;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public class BucketNode extends BeanNode<Bucket> {

    @StaticResource
    private static final String ICON = "org/netbeans/modules/couchbase/database.png";
    private final Bucket bucket;

    public BucketNode(final Bucket bean) throws IntrospectionException {
        super(bean, Children.create(new RowChildFactory(bean), true), Lookups.singleton(bean));
        this.bucket = bean;
        N1qlQuery all = N1qlQuery.simple(select("*").from(i(bucket.name())));
        N1qlQueryResult result = bucket.query(all);
        setDisplayName(bean.name() + " (" + result.allRows().size() + ")");
        setIconBaseWithExtension(ICON);
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{new AbstractAction("Customize Query...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopComponent tc = WindowManager.getDefault().findTopComponent("BucketQueryManagerTopComponent");
                tc.open();
                tc.requestActive();
//                NotifyDescriptor.InputLine nd = new NotifyDescriptor.InputLine(
//                        "No. of rows:",
//                        "Customize Query");
//                DialogDisplayer.getDefault().notify(nd);
//                String enteredNumber = nd.getInputText();
//                NbPreferences.forModule(BucketNode.class).put(
//                        bucket.name() + "-numberOfRows",
//                        enteredNumber);
            }
        }};
    }

    @Override
    public Action getPreferredAction() {
        return null;
    }

}
