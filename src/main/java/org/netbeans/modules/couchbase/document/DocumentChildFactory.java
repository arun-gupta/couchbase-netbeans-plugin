package org.netbeans.modules.couchbase.document;

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
import org.netbeans.modules.couchbase.model.CouchbaseDocument;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;
import static com.couchbase.client.java.query.Select.select;
import org.netbeans.modules.couchbase.connection.ConnectionNode;
import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.Select.select;

public class DocumentChildFactory extends ChildFactory<CouchbaseDocument> implements PreferenceChangeListener {

    private final Bucket bucket;
    private final String bucketName;
    private final Preferences connectionNodePref = NbPreferences.forModule(ConnectionNode.class);
    private final Preferences bucketNodePref = NbPreferences.forModule(BucketNode.class);
    private N1qlQuery query;

    public DocumentChildFactory(Bucket bean) {
        this.bucket = bean;
        this.bucketName = bucket.name();
        this.connectionNodePref.addPreferenceChangeListener(this);
        this.bucketNodePref.addPreferenceChangeListener(this);
        this.query = N1qlQuery
                    .simple(select("*")
                            .from(i(bucket.name()))
                            .limit(3));
    }

    //http://developer.couchbase.com/documentation/server/4.0/getting-started/first-n1ql-query.html
    @Override
    protected boolean createKeys(List<CouchbaseDocument> list) {
        //Force indexing:
//        bucket.query(N1qlQuery.simple(String.format("create primary index on `%s`", bucket.name())));
        //Run query:
        N1qlQueryResult resultOfFindingLimitedDocumentsInBucket = bucket.query(query);
        List<N1qlQueryRow> rowsOfFindingLimitedDocumentsInBucket = resultOfFindingLimitedDocumentsInBucket.allRows();
        for (int i = 0; i < rowsOfFindingLimitedDocumentsInBucket.size(); i++) {
            N1qlQueryRow row = rowsOfFindingLimitedDocumentsInBucket.get(i);
            list.add(new CouchbaseDocument(bucketName, i + 1, row));
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(CouchbaseDocument key) {
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
        String clusterName = connectionNodePref.get("clusterName", "error!");
        String clusterDefaultNumber = connectionNodePref.get(clusterName + "-defaultNumber", "3");
        int numberOfRows = Integer.parseInt(bucketNodePref.get(bucket.name() + "-numberOfRows", clusterDefaultNumber));
        if (evt.getKey().startsWith(bucket.name() + "-numberOfRows") || evt.getKey().startsWith(clusterName)) {
            query = N1qlQuery
                    .simple(select("*")
                            .from(i(bucket.name()))
                            .limit(numberOfRows));
            refresh(true);
        } else if (evt.getKey().startsWith(bucket.name() + "-where") || evt.getKey().startsWith(bucket.name() + "-equals")) {
            String where = bucketNodePref.get(bucket.name() + "-where", "-");
            String equals = bucketNodePref.get(bucket.name() + "-equals", "-");
            query = N1qlQuery
                    .simple(select("*")
                            .from(i(bucket.name()))
                            .where(where + " =\"" + equals + "\"")
                            .limit(numberOfRows));
            refresh(true);
        }
    }

}
