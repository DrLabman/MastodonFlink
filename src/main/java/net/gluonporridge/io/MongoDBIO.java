package net.gluonporridge.io;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.WriteModel;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.bson.Document;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Class contains a Flink Sink for storing data into MongoDB and a
 * Flink Source for reading data out of MongoDB
 */
public class MongoDBIO {

    public static class MongoOptions implements Serializable {

        private static final long serialVersionUID = 958052114998933956L;

        // Connection information
        private String URI;
        private String dbName;
        private String collection;
        // Use bulk insert/update
        private boolean bulk;

        public MongoOptions() {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("MongoDB: ");
            sb.append(URI);
            if (!URI.endsWith("/")) {
                sb.append("/");
            }
            sb.append(dbName);
            sb.append(":");
            sb.append(collection);

            return sb.toString();
        }

        public MongoOptions URI(String URI) {
            this.URI = URI;
            return this;
        }

        public MongoOptions dbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public MongoOptions collection(String collection) {
            this.collection = collection;
            return this;
        }

        public MongoOptions setBulk(boolean bulk) {
            this.bulk = bulk;
            return this;
        }

        public String getURI() {
            return this.URI;
        }

        public String getDb() {
            return this.dbName;
        }

        public String getCollection() {
            return this.collection;
        }

        public boolean isBulk() {
            return bulk;
        }
    }

    public static class MongoDbSink extends RichSinkFunction<Document> {
        private static final long serialVersionUID = 1L;

        private transient volatile boolean open;
        private transient MongoClient client;
        private transient MongoDatabase db;
        private transient MongoCollection<Document> collection;
        private MongoOptions options;
        private List<WriteModel<Document>> bulkInsertList;

        public MongoDbSink(MongoOptions options) {
            this.options = options;
        }

        @Override
        public void open(Configuration config) throws Exception {
            // Open the MongoDB connection
            client = new MongoClient(new MongoClientURI(options.getURI()));
            open = true;

            db = client.getDatabase(options.getDb());
            collection = db.getCollection(options.getCollection());

            if (options.isBulk()) {
                bulkInsertList = new LinkedList<>();
            }
        }

        @Override
        public void invoke(Document doc) {
            if (options.isBulk()) {
                bulkInsertList.add(new InsertOneModel<>(doc));

                if (bulkInsertList.size() >= 1000) {
                    collection.bulkWrite(bulkInsertList, new BulkWriteOptions().ordered(false));
                    bulkInsertList.clear();
                }
            } else {
                // Save the document into collection
                collection.insertOne(doc);
            }
        }

        @Override
        public void close() {
            if (open) {
                if (options.isBulk() && bulkInsertList.size() > 0) {
                    collection.bulkWrite(bulkInsertList, new BulkWriteOptions().ordered(false));
                    bulkInsertList.clear();
                }

                client.close();
                open = false;
            }
        }
    }
}
