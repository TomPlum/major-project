package university.robocode;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import university.twitter.TweetReader;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import static org.junit.jupiter.api.Assertions.*;

class RobotControllerTest {
    //Test Init
    private RobotController rc = new RobotController();

    @Test
    void downloadTweetsShouldReturn3200() throws ExecutionException, InterruptedException {
        rc.initialiseTweets();
        TweetReader tr = new TweetReader();
        ArrayList<String> users = tr.getAllUsernames();
        Random rand = new Random();
        int n = rand.nextInt(users.size());
        String user = users.get(n);
        rc.setUSER(user);
        System.out.println("Getting Tweets from Twitter Account: " + rc.getUSER());
        for (int i = 10; i >= 0; i--) {
            Thread.sleep(1000);
            System.out.println("Waiting " + i + " seconds...");
        }
        assertEquals(3200, rc.getAllTweets().size());

        /*
        CompletableFuture.supplyAsync(() -> {
            ArrayList<Document> tweets;
            try {
                tweets = rc.getAllTweets();
            } catch (NullPointerException e) {
                System.out.println("AllTweets ArrayList is NULL!");
                e.printStackTrace();
                tweets = null;
            }
            return tweets;
        }).thenApply((ArrayList<Document> tweets) -> {
            System.out.println("The RobotController has " + tweets.size() + " tweets.");
            assertEquals(3200, tweets.size());
            return null;
        });
        */
    }

    @Test
    void firePowerShouldBeInRange() {
        rc.randomiseValues();
        double fp = rc.getFIRE_POWER();
        System.out.println("FirePower: " + fp);
        assertTrue(fp >= 0 && fp <= 3);
    }
}