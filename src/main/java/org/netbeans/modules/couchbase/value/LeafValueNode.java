package org.netbeans.modules.couchbase.value;

import org.netbeans.modules.couchbase.model.CouchbaseValue;
import java.beans.IntrospectionException;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.modules.couchbase.model.CouchbaseValue.Type;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;

public class LeafValueNode extends BeanNode<CouchbaseValue> {

    @StaticResource
    private static final String ICON = "org/netbeans/modules/couchbase/key.png";

    public LeafValueNode(CouchbaseValue cd) throws IntrospectionException {
        super(cd, Children.createLazy(new LeafOrArrayCallable(cd)));
        if (cd.getType() == Type.ARRAY) {
            setDisplayName(cd.getName());
        } else {
            setDisplayName(cd.getName() + ":" + cd.getValue().toString());
        }
        setIconBaseWithExtension(ICON);
    }
    
    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{};
    }
    
    @Override
    public Action getPreferredAction() {
        return null;
    }

}
