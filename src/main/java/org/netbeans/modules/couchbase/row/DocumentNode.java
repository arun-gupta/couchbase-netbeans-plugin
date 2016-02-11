package org.netbeans.modules.couchbase.row;

import java.beans.IntrospectionException;
import java.util.List;
import javax.swing.Action;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.modules.couchbase.model.CouchBaseRow;
import org.netbeans.modules.couchbase.value.LeafValueChildFactory;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

public class DocumentNode extends BeanNode<CouchBaseRow> {

    @StaticResource
    private static final String ICON = "org/netbeans/modules/couchbase/collection.png";

    public DocumentNode(CouchBaseRow cbr) throws IntrospectionException {
        super(cbr, Children.create(new LeafValueChildFactory(cbr), true), Lookups.singleton(cbr));
        setDisplayName(String.valueOf(cbr.getI()));
        setIconBaseWithExtension(ICON);
    }

    @Override
    public Action[] getActions(boolean context) {
        List<? extends Action> documentActions = Utilities.actionsForPath("Actions/Document");
        return documentActions.toArray(new Action[documentActions.size()]);
    }

    @Override
    public Action getPreferredAction() {
        return null;
    }

}
