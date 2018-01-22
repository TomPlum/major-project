package twitter;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import connection.MongoConnection;
import java.util.ArrayList;
import java.util.Date;
import static com.mongodb.client.model.Filters.eq;

public class TweetReader {
    private MongoConnection conn = new MongoConnection("twitter", "tweets");

    private ArrayList<Document> getAllTweets() {
        ArrayList<Document> list = new ArrayList<>();
        MongoCollection<Document> coll = conn.getMongoCollection();
        try (MongoCursor<Document> cursor = coll.find().iterator()) {
            while (cursor.hasNext()) {
                list.add(cursor.next());
            }
        }
        return list;
    }

    private ArrayList<Document> getTweetsByUser(String username) {
        ArrayList<Document> list = new ArrayList<>();
        Block<Document> addBlock = document -> list.add(document);
        conn.getMongoCollection().find(eq("user", username)).forEach(addBlock);
        return list;
    }

    public ArrayList<String> getAllValuesByKey(String key) {
        ArrayList<String> list = new ArrayList<>();
        MongoCollection<Document> coll = conn.getMongoCollection();
        try (MongoCursor<Document> cursor = coll.find().iterator()) {
            while (cursor.hasNext()) {
                list.add(cursor.next().get(key).toString());
            }
        }
        return list;
    }

    public ArrayList<String> getAllUsernames() {
        ArrayList<String> list = new ArrayList<>();
        MongoCollection<Document> coll = conn.getMongoCollection();
        try (MongoCursor<Document> cursor = coll.find().iterator()) {
            while (cursor.hasNext()) {
                if (!list.contains(cursor.next().get("user"))) {
                    list.add(cursor.next().get("user").toString());
                }
            }
        }
        return list;
    }

    public ArrayList<Document> getTweetsByDate(Date date) {
        ArrayList<Document> list = new ArrayList<>();
        Block<Document> addBlock = document -> list.add(document);
        conn.getMongoCollection().find(eq("created_at", date)).forEach(addBlock);
        return list;
    }

    public Document getTweetByTweetId(long tweet_id) {
        return conn.getMongoCollection().find(eq("tweet_id", tweet_id)).first();
    }

    public String getTweetTextById(long tweet_id) {
        Document doc = conn.getMongoCollection().find(eq("tweet_id", tweet_id)).first();
        return "" + doc.get("text");
    }

    private String documentToString(Document doc) {
        String s = "";
        try {
            s += "_id: " + doc.get("_id") + "\n";
            s += "tweet_id: " + doc.get("tweet_id") + "\n";
            s += "created_at: " + doc.get("created_at") + "\n";
            s += "text: " + doc.get("text") + "\n";
            s += "user: " + doc.get("user") + "\n";
            s += "language: " + doc.get("language") + "\n";
        } catch (NullPointerException e) {
            System.out.println("Document Is Null");
        }
        return s;
    }

    public static void main(String[] args) {
        TweetReader tr = new TweetReader();
        System.out.println(tr.getAllUsernames());
    }
}
