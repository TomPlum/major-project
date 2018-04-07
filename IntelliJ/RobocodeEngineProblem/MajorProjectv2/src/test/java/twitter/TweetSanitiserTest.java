package twitter;

import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.vdurmont.emoji.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TweetSanitiserTest {
    private static ArrayList<Document> tweets = new ArrayList<>();

    @BeforeAll
    private static void downloadTweets() {
        TweetReader tr = new TweetReader();

        System.out.println("Initialising TweetSanitiser Test Suite...");
        //Get All Usernames From MongoDB Cluster
        ArrayList<String> users = tr.getAllUsernames();

        //Generate Random Number (From 0 - users.size())
        Random rand = new Random();
        int n = rand.nextInt(users.size());

        //Get random user string and set RobotController USER
        String user = users.get(n);
        System.out.println("User Set To: " + user);
        tweets = tr.getTweetsByUser(user);
    }

    @Test
    void stringsShouldOnlyContainLetters() {
        Iterator it = tweets.iterator();
        int successfulDocuments = 0;
        while(it.hasNext()) {
            Document doc = (Document) it.next();
            String tweet = (String) doc.get("text");
            boolean tweetSuccessful = true;

            //Extract Letters Test
            TweetSanitiser ts = new TweetSanitiser(tweet);
            tweet = ts.extractLetters();
            for (int i = 0; i < tweet.length(); i++) {
                if (!Character.isLetter(tweet.charAt(i))) {
                    tweetSuccessful = false;
                    System.out.println("Failed Tweet:");
                    System.out.println("_id: " + doc.get("_id"));
                    System.out.println("tweet_id: " + doc.get("tweet_id"));
                    System.out.println("text: " + tweet + "\n");
                }
            }

            if (tweetSuccessful) {
                successfulDocuments++;
            }
        }

        assertEquals(tweets.size(), successfulDocuments);
    }

    @Test
    void stringsShouldNotContainAnyEmoji() {
        Iterator it = tweets.iterator();
        int successfulDocuments = 0;
        while(it.hasNext()) {
            Document doc = (Document) it.next();
            String tweet = (String) doc.get("text");

            //Remove Emoji Test
            TweetSanitiser ts = new TweetSanitiser(tweet);
            ts.removeEmoji();
            tweet = ts.getTweet();

            if (EmojiParser.extractEmojis(tweet).size() == 0) {
                successfulDocuments++;
            } else {
                //System.out.println("Failed Tweet:");
                //System.out.println("_id: " + doc.get("_id"));
                //System.out.println("tweet_id: " + doc.get("tweet_id"));
                //System.out.println("text: " + tweet + "\n");
            }
        }

        System.out.println(successfulDocuments + "/" + tweets.size() + " Passed. " + (tweets.size() - successfulDocuments) + "/" + tweets.size() + " Failed.");

        assertEquals(tweets.size(), successfulDocuments);
    }

    @Test
    void stringsShouldNotContainAnyWhiteSpaces() {
        Iterator it = tweets.iterator();
        int successfulDocuments = 0;
        while(it.hasNext()) {
            Document doc = (Document) it.next();
            String tweet = (String) doc.get("text");

            //Remove WhiteSpaces Test
            TweetSanitiser ts = new TweetSanitiser(tweet);
            ts.removeWhiteSpaces();
            tweet = ts.getTweet();

            if (tweet.contains("\\s")) {
                successfulDocuments++;
            } else {
                System.out.println("Failed Tweet:");
                System.out.println("_id: " + doc.get("_id"));
                System.out.println("tweet_id: " + doc.get("tweet_id"));
                System.out.println("text: " + tweet + "\n");
            }
        }

        assertEquals(tweets.size(), successfulDocuments);
    }

    @Test
    void stringsShouldNotContainAnyLineBreaks() {
        Iterator it = tweets.iterator();
        int successfulDocuments = 0;
        while(it.hasNext()) {
            Document doc = (Document) it.next();
            String tweet = (String) doc.get("text");

            //Remove WhiteSpaces Test
            TweetSanitiser ts = new TweetSanitiser(tweet);
            ts.removeLineBreaks();
            tweet = ts.getTweet();

            if (tweet.contains("\\r|\\n")) {
                successfulDocuments++;
            } else {
                System.out.println("Failed Tweet:");
                System.out.println("_id: " + doc.get("_id"));
                System.out.println("tweet_id: " + doc.get("tweet_id"));
                System.out.println("text: " + tweet + "\n");
            }
        }

        assertEquals(tweets.size(), successfulDocuments);
    }
}