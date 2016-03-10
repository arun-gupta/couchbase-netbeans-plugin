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
import java.awt.Image;
import java.util.List;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class BucketNode extends BeanNode<Bucket> {

    @StaticResource
    private static final String ICON = "org/netbeans/modules/couchbase/database.png";
    @StaticResource
    private static final String INDEXED = "org/netbeans/modules/couchbase/bucket/index_icon.png";
    @StaticResource
    private static final String UNINDEXED = "org/netbeans/modules/couchbase/bucket/unindexed_icon.png";
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
        N1qlQueryResult query = bucket.query(N1qlQuery.simple(String.format("select * from system:indexes where keyspace_id = '" + bucket.name() + "'", bucket.name())));
        int size = query.allRows().size();
        if (size > 0) {
            setShortDescription("Indexed!");
        } else {
            setShortDescription("Not Indexed Yet!");
        }
    }

    @Override
    public Image getIcon(int type) {
        Image original = ImageUtilities.loadImage(ICON);
        N1qlQueryResult query = bucket.query(N1qlQuery.simple(String.format("select * from system:indexes where keyspace_id = '" + bucket.name() + "'", bucket.name())));
        int size = query.allRows().size();
        if (size > 0) {
            return ImageUtilities.mergeImages(original, ImageUtilities.loadImage(INDEXED), 7, 7);
        } else {
            return ImageUtilities.mergeImages(original, ImageUtilities.loadImage(UNINDEXED), 7, 7);
        }
    }

    @Override
    public Image getOpenedIcon(int type) {
        Image original = ImageUtilities.loadImage(ICON);
        N1qlQueryResult query = bucket.query(N1qlQuery.simple(String.format("select * from system:indexes where keyspace_id = '" + bucket.name() + "'", bucket.name())));
        int size = query.allRows().size();
        if (size > 0) {
            return ImageUtilities.mergeImages(original, ImageUtilities.loadImage(INDEXED), 7, 7);
        } else {
            return ImageUtilities.mergeImages(original, ImageUtilities.loadImage(UNINDEXED), 7, 7);
        }
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
