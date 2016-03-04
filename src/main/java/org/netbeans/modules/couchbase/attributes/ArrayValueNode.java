package org.netbeans.modules.couchbase.attributes;

import java.beans.IntrospectionException;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;

public class ArrayValueNode extends BeanNode<String> {

    @StaticResource
    private static final String ICON = "org/netbeans/modules/couchbase/bullet_blue.png";

    public ArrayValueNode(String label) throws IntrospectionException {
        super(label, Children.LEAF);
        setDisplayName("array-"+label);
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
