package org.netbeans.modules.couchbase.value;

import org.netbeans.modules.couchbase.model.CouchbaseValue;
import java.beans.IntrospectionException;
import java.util.List;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.modules.couchbase.model.CouchbaseValue.Type;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

public class LeafValueNode extends BeanNode<CouchbaseValue> {

    @StaticResource
    private static final String ICON = "org/netbeans/modules/couchbase/key.png";

    private final CouchbaseValue cd;

    public LeafValueNode(CouchbaseValue cd) throws IntrospectionException {
        super(cd, Children.createLazy(new LeafOrArrayCallable(cd)), Lookups.singleton(cd));
        this.cd = cd;
        if (cd.getType() == Type.ARRAY) {
            setDisplayName(cd.getName());
        } else {
            setDisplayName(cd.getName() + ":" + cd.getValue().toString());
        }
        setIconBaseWithExtension(ICON);
    }

    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> bucketActions = Utilities.actionsForPath("Actions/Value");
        return bucketActions.toArray(new Action[bucketActions.size()]);
    }

    @Override
    public Action getPreferredAction() {
        return Utilities.actionsForPath("Actions/Value").get(0);
    }

}
