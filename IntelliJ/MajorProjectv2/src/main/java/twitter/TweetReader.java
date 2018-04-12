package twitter;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;

import static com.mongodb.client.model.Filters.eq;

public class TweetReader {
    private MongoConnection conn = new MongoConnection("twitter", "tweets");

    /**
     * Iterates over the database via MongoCursor and return all documents.
     * @return ArrayList of all Tweets from the database.
     */
    public ArrayList<Document> getAllTweets() {
        ArrayList<Document> list = new ArrayList<>();
        MongoCollection<Document> coll = conn.getMongoCollection();
        try (MongoCursor<Document> cursor = coll.find().iterator()) {
            while (cursor.hasNext()) {
                list.add(cursor.next());
            }
        }
        return list;
    }

    /**
     * Iterates over the database via MongoCursor and returns all documents from the given user.
     * @param username Twitter ScreenName to download.
     * @return ArrayList of all Tweets from the given user.
     */
    public ArrayList<Document> getTweetsByUser(String username) {
        if (username == null) {
            NullPointerException err = new NullPointerException("Username is NULL. Cannot get Tweets by user.");
            System.out.println(err.getMessage());
            throw err;
        }
        ArrayList<Document> list = new ArrayList<>();
        Block<Document> addBlock = document -> list.add(document);
        conn.getMongoCollection().find(eq("screenName", username)).forEach(addBlock);
        return list;
    }

    /**
     * Iterates over the database via MongoCursor and returns all Tweet text for the given user.
     * @param username Twitter ScreenName
     * @return ArrayList of Tweet Text values.
     */
    public ArrayList<String> getAllTweetTextByUser(String username) {
        ArrayList<String> list = new ArrayList<>();
        MongoCollection<Document> coll = conn.getMongoCollection();
        try (MongoCursor<Document> cursor = coll.find(eq("screenName", username)).iterator()) {
            while (cursor.hasNext()) {
                list.add(cursor.next().get("text").toString());
            }
        }
        return list;
    }

    /**
     * Iterates over the database via MongoCursor and returns all values from the given key.
     * @param key Document key to filter by
     * @return ArrayList of String values that match the provided key.
     */
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

    /**
     * Returns an ArrayList of all users who have Tweets in the database.
     * @return ArrayList of Strings of all username values.
     */
    public ArrayList<String> getAllUsernames() {
        ArrayList<String> list = new ArrayList<>();
        MongoCollection<Document> coll = conn.getMongoCollection();
        try (MongoCursor<Document> cursor = coll.find().iterator()) {
            int count = 0;
            while (cursor.hasNext()) {
                Document element = cursor.next();
                try {
                    //System.out.println(element.get("user") + ": " + count);
                    count++;
                    if (!list.contains(element.get("screenName"))) {
                        list.add(element.get("screenName").toString());
                    }
                } catch (NullPointerException e) {
                    System.out.println("Found Null User: " + element.get("tweet_id") + "(tweet_id)");
                } catch (NoSuchElementException e) {
                    System.out.println("Error: MongoCursor tried to get an element that doesn't exist.");
                }
            }
        }
        return list;
    }

    public ArrayList<String> getAllScreenames() {
        ArrayList<String> screenNames = new ArrayList<>();
        MongoConnection mc = new MongoConnection("twitter", "users");
        MongoCollection<Document> coll = mc.getMongoCollection();
        try (MongoCursor<Document> cursor = coll.find().iterator()) {
            while(cursor.hasNext()) {
                Document element = cursor.next();
                screenNames.add(element.get("name").toString());
            }
        }
        return screenNames;
    }

    /**
     * Returns all Tweets that were posted on the given date.
     * @param date Date Object representing the date a Tweet was posted.
     * @return ArrayList of Tweets that were posted on the given date.
     */
    public ArrayList<Document> getTweetsByDate(Date date) {
        ArrayList<Document> list = new ArrayList<>();
        Block<Document> addBlock = document -> list.add(document);
        conn.getMongoCollection().find(eq("created_at", date)).forEach(addBlock);
        return list;
    }

    /**
     * Returns a single Tweet that matches the provided, unique Tweet ID.
     * @param tweet_id Unique Tweet ID (Long)
     * @return Tweet that matches the given Tweet ID.
     */
    public Document getTweetByTweetId(long tweet_id) {
        return conn.getMongoCollection().find(eq("tweet_id", tweet_id)).first();
    }

    /**
     * Beautifies a Document and returns the formatted String.
     * @param doc Document to format
     * @return Formatted String
     */
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
}
