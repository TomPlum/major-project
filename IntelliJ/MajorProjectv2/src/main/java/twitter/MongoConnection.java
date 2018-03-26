package twitter;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.mongodb.client.model.Filters.eq;

/**
 * --------------------------------------------------------------------------------------------------------
 * This class establishes a connection to a MongoDB Atlas Cluster and provides basic functionality such as
 * searching for documents via key, inserting documents and deleting documents.
 * --------------------------------------------------------------------------------------------------------
 * @author Thomas Plumpton
 * @version 1.0.0
 */
public class MongoConnection {
    private final String uri = "mongodb+srv://TomPlum:i7ljjmXIi19PK1CU@twitter-yu9se.mongodb.net/";
    private String database;
    private String collection;
    private MongoClient mongoClient;

    /**
     * MongoConnection Constructor. Instantiates a new MongoConnection Object.
     * @param database Name of the database to connect to
     * @param collection Collection in the database to access
     */
    public MongoConnection(String database, String collection) {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        setDatabase(database);
        setCollection(collection);
        initConn();
    }

    /**
     * Initialises the connection to the MongoDB Atlas Cluster.
     * @exception MongoSocketReadException occurs if IP not whitelisted
     */
    private void initConn() {
        try {
            mongoClient = new MongoClient(new MongoClientURI(uri));
        } catch (MongoSocketReadException e) {
            System.out.println("MongoSocketReadException! Perhaps your IP needs whitelisting.");
        }
        System.out.println("MongoDBAtlas Cluster Connection Made @ " + uri);
    }

    /**
     * Returns the MongoCollection of the specified database.
     * @return MongoCollection<Document>
     */
    public MongoCollection<Document> getMongoCollection() {
        try {
            MongoDatabase db = mongoClient.getDatabase(getDatabase());
            return db.getCollection(getCollection());
        } catch (MongoSocketReadException e) {
            System.out.println("MongoSocketReadException! Perhaps your IP needs whitelisting.");
            return null;
        }
    }

    /**
     * Inserts a single document into the MongoCollection
     * @param object Document To Insert
     */
    public void insertDocument(Document object) {
        getMongoCollection().insertOne(object);
    }

    private void updateUserDetails(String username, String screenName) {
        TweetReader tr = new TweetReader();
        UpdateOptions upsert = new UpdateOptions().upsert(true);
        ArrayList<Document> tweets = tr.getTweetsByUser(username);
        int count = 0;
        for (Document status : tweets) {
            count++;
            Bson idFilter = Filters.eq("_id", status.getObjectId("_id"));
            status.append("screenName", screenName);
            System.out.println("Adding ScreenName '" + screenName + "' for '" + username + "' [" + count + "/" + tweets.size() + "]");
            getMongoCollection().replaceOne(idFilter, status, upsert);
        }
    }

    public void removeDocumentsByKey(String key, String val) {
        DeleteResult delete = getMongoCollection().deleteMany(eq(key, val));
        System.out.println("Successfully Deleted " + delete.getDeletedCount() + " Tweets.");
        System.out.println("Where " + key + " matches '" + val + "'.");
    }

    /**
     * Disconnects from the MongoDB Atlas Database.
     * This method should be called when finished using the database to free up resources.
     */
    public void disconnect() {
        mongoClient.close();
        System.out.println("Successfully Disconnected From " + uri);
    }

    /**
     * Get the database name of the MongoConnection.
     * @return Database Name
     */
    private String getDatabase() {
        return database;
    }

    /**
     * Get the collection name of the MongoConnection.
     * @return Collection Name
     */
    public String getCollection() {
        return collection;
    }

    /**
     * Set the database name of the MongoConnection
     * @param database MongoDB Atlas Database Name (Case Sensitive)
     */
    private void setDatabase(String database) {
        this.database = database;
    }

    /**
     * Set the collection name of the MongoConnection
     * @param collection MongoDB Atlas Collection Name (Case Sensitive)
     */
    private void setCollection(String collection) {
        this.collection = collection;
    }
}
