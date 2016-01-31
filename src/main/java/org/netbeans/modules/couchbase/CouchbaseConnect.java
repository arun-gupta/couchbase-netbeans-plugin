package org.netbeans.modules.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import org.openide.awt.StatusDisplayer;

public class CouchbaseConnect {

    public static void main() {
// assume that you're running Couchbase locally,
// with the login "Administrator" and password "password"
        String address = "localhost";
        String login = "Administrator";
        String password = "adminadmin";
// create a connection to that server
        Cluster cluster = CouchbaseCluster.create(address);

        System.out.printf("Got cluster: %s\n", cluster.toString());
        
        StatusDisplayer.getDefault().setStatusText("Cluster: " + cluster.toString());

// get a ClusterManager object
        ClusterManager cmgr = cluster.clusterManager(login, password);
        System.out.printf("Got cluster manager: %s\n", cmgr.toString());

// write out info about our buckets
        for (BucketSettings b : cmgr.getBuckets()) {
// Open the bucket
Bucket bucket = cluster.openBucket(b.name());

System.out.printf("Looking for doccount for bucket: %s\n", b.name());
// Run a N1QL query to find out the number of documents
N1qlQueryResult res = bucket.query(N1qlQuery.simple(String.format("select count(*) as doccount from `%s`", b.name())));
for (JsonObject error : res.errors()) {
    System.out.printf("  got error: %s\n", error.toString());
}
for (N1qlQueryRow row : res.allRows()) {
    System.out.printf("  got doccount: %d\n", row.value().get("doccount"));
}
        }
    }
}
