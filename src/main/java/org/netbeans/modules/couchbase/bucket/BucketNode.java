package org.netbeans.modules.couchbase.bucket;

import com.couchbase.client.java.Bucket;
import org.netbeans.modules.couchbase.document.DocumentChildFactory;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import static com.couchbase.client.java.query.dsl.Expression.i;
import java.beans.IntrospectionException;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import static com.couchbase.client.java.query.Select.select;
import java.util.List;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class BucketNode extends BeanNode<Bucket> {

    @StaticResource
    private static final String ICON = "org/netbeans/modules/couchbase/database.png";
    private final Bucket bucket;

    public BucketNode(Bucket bean) throws IntrospectionException {
        this(bean, new InstanceContent());
    }

    private BucketNode(final Bucket bean, InstanceContent ic) throws IntrospectionException {
        super(bean, Children.create(new DocumentChildFactory(bean), true), new AbstractLookup(ic));
        ic.add(bean);
        ic.add(this);
        this.bucket = bean;
        N1qlQuery all = N1qlQuery.simple(select("*").from(i(bucket.name())));
        N1qlQueryResult result = bucket.query(all);
        setDisplayName(bean.name() + " (" + result.allRows().size() + ")");
        setIconBaseWithExtension(ICON);
    }

    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> bucketActions = Utilities.actionsForPath("Actions/Bucket");
        return bucketActions.toArray(new Action[bucketActions.size()]);
    }

    @Override
    public Action getPreferredAction() {
        return null;
    }

}
