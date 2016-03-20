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
import java.io.IOException;
import org.netbeans.modules.couchbase.connection.rename.RenameContainerAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.OpenLocalExplorerAction;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class BucketNode extends BeanNode<Bucket> {

    private static final String BASE_ICON_PATH = "org/netbeans/modules/couchbase/";
    @StaticResource
    private static final String ICON = BASE_ICON_PATH + "database.png";
    @StaticResource
    private static final String INDEXED = BASE_ICON_PATH + "bucket/index_icon.png";
    @StaticResource
    private static final String UNINDEXED = BASE_ICON_PATH + "bucket/unindexed_icon.png";
    private final Bucket bucket;

    public BucketNode(Bucket bean) throws IntrospectionException {
        this(bean, new InstanceContent());
    }

    private BucketNode(final Bucket bean, InstanceContent ic) throws IntrospectionException {
        super(bean, Children.create(new DocumentChildFactory(bean), true), new AbstractLookup(ic));
        ic.add(bean);
        ic.add(this);
        this.bucket = bean;
        setDisplayName(bean.name());
    }

//    private String getCurrentName() {
//        N1qlQuery all = N1qlQuery.simple(select("*").from(i(bucket.name())));
//        N1qlQueryResult result = bucket.query(all);
//        return bucket.name() + " (" + result.allRows().size() + ")";
//    }
//
//    @Override
//    public String getDisplayName() {
//        return getCurrentName();
//    }
//
//    @Override
//    public String getShortDescription() {
//        N1qlQueryResult query = bucket.query(N1qlQuery.simple(String.format("select * from system:indexes where keyspace_id = '" + bucket.name() + "'", bucket.name())));
//        int size = query.allRows().size();
//        if (size > 0) {
//            return "Indexed!";
//        } else {
//            return "Not Indexed Yet!";
//        }
//    }
//
    @Override
    public Image getIcon(int type) {
        return getCurrentImage();
    }

    private Image getCurrentImage() {
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
        return getCurrentImage();
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{
            Utilities.actionsForPath("Actions/Bucket").get(1),
            null,
            Utilities.actionsForPath("Actions/Bucket").get(0),
            SystemAction.get(RenameContainerAction.class),
            SystemAction.get(OpenLocalExplorerAction.class),
            null,
            SystemAction.get(DeleteAction.class)
        };
    }

    @Override
    public boolean canDestroy() {
        return true;
    }
    
    @Override
    public boolean canRename() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        fireNodeDestroyed();
    }
    
    public void refresh() {
        fireIconChange();
        fireOpenedIconChange();
    }

    @Override
    public Action getPreferredAction() {
        return null;
    }

}
