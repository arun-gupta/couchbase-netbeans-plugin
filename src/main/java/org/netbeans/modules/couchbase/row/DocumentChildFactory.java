package org.netbeans.modules.couchbase.row;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import static com.couchbase.client.java.query.dsl.Expression.i;
import java.beans.IntrospectionException;
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

public class DocumentChildFactory extends ChildFactory<CouchBaseRow> implements PreferenceChangeListener {

    private final Bucket bucket;
    private final String bucketName;
    private final Preferences pref = NbPreferences.forModule(BucketNode.class);
    private N1qlQuery query;

    public DocumentChildFactory(Bucket bean) {
        this.bucket = bean;
        this.bucketName = bucket.name();
        this.pref.addPreferenceChangeListener(this);
        this.query = N1qlQuery
                    .simple(select("*")
                            .from(i(bucket.name()))
                            .limit(3));
    }

    //http://developer.couchbase.com/documentation/server/4.0/getting-started/first-n1ql-query.html
    @Override
    protected boolean createKeys(List<CouchBaseRow> list) {
        //Force indexing:
        bucket.query(N1qlQuery.simple(String.format("create primary index on `%s`", bucket.name())));
        //Run query:
        N1qlQueryResult resultOfFindingLimitedDocumentsInBucket = bucket.query(query);
        List<N1qlQueryRow> rowsOfFindingLimitedDocumentsInBucket = resultOfFindingLimitedDocumentsInBucket.allRows();
        for (int i = 0; i < rowsOfFindingLimitedDocumentsInBucket.size(); i++) {
            N1qlQueryRow row = rowsOfFindingLimitedDocumentsInBucket.get(i);
            list.add(new CouchBaseRow(bucketName, i + 1, row));
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
        int numberOfRows = Integer.parseInt(pref.get(bucket.name() + "-numberOfRows", "3"));
        if (evt.getKey().startsWith(bucket.name() + "-numberOfRows")) {
            query = N1qlQuery
                    .simple(select("*")
                            .from(i(bucket.name()))
                            .limit(numberOfRows));
            refresh(true);
        } else if (evt.getKey().startsWith(bucket.name() + "-where") || evt.getKey().startsWith(bucket.name() + "-equals")) {
            String where = pref.get(bucket.name() + "-where", "-");
            String equals = pref.get(bucket.name() + "-equals", "-");
            query = N1qlQuery
                    .simple(select("*")
                            .from(i(bucket.name()))
                            .where(where + " =\"" + equals + "\"")
                            .limit(numberOfRows));
            refresh(true);
        }
    }

}
