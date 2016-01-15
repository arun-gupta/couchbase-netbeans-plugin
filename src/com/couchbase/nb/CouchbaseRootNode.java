package com.couchbase.nb;

import org.netbeans.api.core.ide.ServicesTabNodeRegistration;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

@ServicesTabNodeRegistration(
        name="Couchbase", 
        displayName = "Couchbase",
        iconResource = "com/couchbase/nb/couchbase-icon.jpeg")
public class CouchbaseRootNode extends AbstractNode {
    
    public CouchbaseRootNode() {
        super(Children.LEAF);
        setDisplayName("Couchbase Root");
    }
    
}
