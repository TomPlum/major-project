package twitter;

import org.bson.Document;
import java.util.ArrayList;

public class Test {
    public static void main (String[] args) {
        /*
        TweetReader tr = new TweetReader();
        ArrayList<Document> tweets;
        tweets = tr.getTweetsByUser("BBC");
        System.out.println(tweets.toString());
        */

        MongoConnection mc = new MongoConnection("twitter", "tweets");
        TweetReader tr = new TweetReader();
        System.out.println(tr.getAllUsernames());
    }
}
