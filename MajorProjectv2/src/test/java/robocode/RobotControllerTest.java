package robocode;

import java.util.ArrayList;
import java.util.Random;

import controller.RobotController;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import twitter.TweetReader;
import static org.junit.jupiter.api.Assertions.*;

class RobotControllerTest {

    private void initController(RobotController rc) {
        TweetReader tr = new TweetReader();

        //Get all usernames from cluster
        ArrayList<String> users = tr.getAllUsernames();

        //Generate Random Number (From 0 - users.size())
        Random rand = new Random();
        int n = rand.nextInt(users.size());

        //Get random user string and set RobotController USER
        String user = users.get(n);
        rc.setUSER(user);
        System.out.println("RobotController Username set to " + rc.getUSER());

        //Initialise RobotController Tweets
        rc.initialiseTweets();
    }

    @Test
    void downloadTweetsShouldReturn3200()  {
        RobotController rc = new RobotController();
        initController(rc);

        ArrayList<Document> tweets = rc.getAllTweets();
        System.out.println("Getting Tweets from Twitter Account: " + rc.getUSER());

        //Check if they have been downloaded and set correctly
        System.out.println("The RobotController has " + tweets.size() + " Tweets.");
        assertTrue(tweets.size() >= 3200);
    }

    @Test
    void firePowerShouldBeInRange() {
        RobotController rc = new RobotController();
        initController(rc);

        rc.randomiseValues();
        double fp = rc.getFIRE_POWER();
        System.out.println("FirePower: " + fp);
        assertTrue(fp >= 0.1 && fp <= 3.0);
    }
}