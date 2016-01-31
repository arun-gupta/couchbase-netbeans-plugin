package org.netbeans.modules.couchbase.row;

import java.beans.IntrospectionException;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.modules.couchbase.model.CouchBaseRow;
import org.netbeans.modules.couchbase.value.LeafValueChildFactory;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;

public class RowNode extends BeanNode<CouchBaseRow> {

    @StaticResource
    private static final String ICON = "org/netbeans/modules/couchbase/collection.png";
    
    public RowNode(CouchBaseRow row) throws IntrospectionException {
        super(row, Children.create(new LeafValueChildFactory(row.getRow()), true));
        setDisplayName("Row " + row.getI());
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
