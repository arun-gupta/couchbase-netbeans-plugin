package org.netbeans.modules.couchbase.row;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import java.beans.IntrospectionException;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import org.netbeans.modules.couchbase.bucket.BucketNode;
import org.netbeans.modules.couchbase.model.CouchBaseRow;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;
import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.Select.select;

public class DocumentChildFactory extends ChildFactory<CouchBaseRow> implements PreferenceChangeListener {

    private final Bucket bucket;
    private final String bucketName;
    private final Preferences pref = NbPreferences.forModule(BucketNode.class);

    public DocumentChildFactory(Bucket bean) {
        this.bucket = bean;
        this.bucketName = bucket.name();
        this.pref.addPreferenceChangeListener(this);
    }

    @Override
    protected boolean createKeys(List<CouchBaseRow> list) {
        int numberOfRows = Integer.parseInt(pref.get(bucket.name() + "-numberOfRows", "2"));
        bucket.query(N1qlQuery.simple(String.format("create primary index on `%s`", bucket.name())));
        N1qlQuery query = N1qlQuery
                .simple(select("*")
                        .from(i(bucket.name()))
                        .limit(numberOfRows));
        N1qlQueryResult result = bucket.query(query);
        List<N1qlQueryRow> rows = result.allRows();
        for (int i = 0; i < rows.size(); i++) {
            N1qlQueryRow row = rows.get(i);
            list.add(new CouchBaseRow(bucketName, i, row));
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(CouchBaseRow key) {
        DocumentNode rn = null;
        try {
            rn = new DocumentNode(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return rn;
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent evt) {
        if (evt.getKey().equals(bucket.name() + "-numberOfRows")) {
            refresh(true);
        }
    }

}
