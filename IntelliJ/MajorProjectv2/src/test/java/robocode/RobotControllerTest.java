package robocode;

import java.io.IOException;
import java.util.ArrayList;
import controller.RobotController;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import twitter.TweetSerialiser;
import view.RobotObserver;
import static org.junit.jupiter.api.Assertions.*;

class RobotControllerTest {
    private static RobotController rc = new RobotController();
    private static TweetSerialiser serialiser = new TweetSerialiser();
    private static RobotObserver observer =new RobotObserver();
    private static ArrayList<Document> tweets;
    private static int roundNumber = 1;

    @BeforeAll
    @SuppressWarnings("unused")
    private static void init() {
        System.out.println("Initialising RobotController Test Suite...");
        try {
            serialiser.serialiseTweets(1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tweets = serialiser.readTweets(1);

        rc.setUSER(tweets.get(0).get("screenName").toString());

        //Initialise RobotController Tweets
        rc.initialiseTweets(tweets);
        observer.startObserving();
        System.out.println("Successfully Read " + rc.getAllTweets().size() + " Tweets.");
        System.out.println("Running Tests...");
    }

    private static void updateObserver() {
        observer.setNumberOfTweetsOne(tweets.size());
        observer.setRoundNumber(roundNumber++);
        observer.setXOrdinateOne(1);
        observer.setYOrdinateOne(1);
        observer.setUserOne(rc.getUSER());
        observer.setFirePowerOne(rc.getFIRE_POWER());
        observer.setMoveUpOne(rc.getMOVE_UP());
        observer.setMoveRightOne(rc.getMOVE_RIGHT());
        observer.setMoveDownOne(rc.getMOVE_DOWN());
        observer.setMoveLeftOne(rc.getMOVE_LEFT());
        observer.setRotateOne(rc.getROTATE());
        observer.setRotateGunOne(rc.getROTATE_GUN());
        observer.setRotateDirectionOne(rc.getROTATE_DIRECTION());
        observer.setRotateGunDirectionOne(rc.getROTATE_GUN_DIRECTION());
        observer.setCurrentTweetOne(rc.getCurrentTweet().get("text").toString());
    }

    @Test
    void testFunctionality() {
        for(int i = 0; i < 50; i++) {
            try {
                Thread.sleep(1000);
                rc.randomiseValues();
                updateObserver();
                rc.invalidateAllValues();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterAll
    @SuppressWarnings("unused")
    private static void finish() {
        System.out.println("Tests Finished.\n");
        System.out.println("RobotController Values:");
        System.out.println("Fire Power: " + rc.getFIRE_POWER());
        System.out.println("Robot Rotation: " + rc.getROTATE() + "deg");
        if (rc.getROTATE_DIRECTION() == 1) {
            System.out.println("Robot Rotation Direction: Clockwise");
        } else if (rc.getROTATE_DIRECTION() == -1) {
            System.out.println("Robot Rotation Direction: Anti-Clockwise");
        } else {
            System.out.println("Robot Rotation Direction: Error, it is " + rc.getROTATE_DIRECTION());
        }
        System.out.println("Move Up: " + rc.getMOVE_UP());
        System.out.println("Move Right: " + rc.getMOVE_RIGHT());
        System.out.println("Move Down: " + rc.getMOVE_DOWN());
        System.out.println("Move Left: " + rc.getMOVE_LEFT());
        System.out.println("Gun Rotation: " + rc.getROTATE_GUN() + "deg.");
        if (rc.getROTATE_GUN_DIRECTION() == 1) {
            System.out.println("Gun Rotation Direction: Clockwise");
        } else if (rc.getROTATE_GUN_DIRECTION() == -1) {
            System.out.println("Gun Rotation Direction: Anti-Clockwise");
        } else {
            System.out.println("Gun Rotation Direction: Error, it is " + rc.getROTATE_GUN_DIRECTION());
        }

        System.out.println("\nTweets Used: " + rc.getTWEETS_USED());
        System.out.println("Characters Used: " + rc.getCHARS_USED());
    }

    @Test
    void downloadTweetsShouldReturn3200()  {
        ArrayList<Document> tweets = rc.getAllTweets();
        System.out.println("Getting Tweets from Twitter Account: " + rc.getUSER());

        //Check if they have been downloaded and set correctly
        System.out.println("The RobotController has " + tweets.size() + " Tweets.");
        assertTrue(tweets.size() >= 3200);
    }

    @Test
    void firePowerShouldBeInRange() {
        //rc.randomiseValues();
        double fp = rc.getFIRE_POWER();
        assertTrue(fp >= 0.0 && fp <= 3.0);
    }

    @Test
    void rotateShouldBeInRange() {
        //rc.randomiseValues();
        Integer angle = rc.getROTATE();
        assertTrue(angle >= -360 && angle <= 360);
    }

    @Test
    void rotateDirectionShouldBeOne() {
        //rc.randomiseValues();
        Integer dir = rc.getROTATE_DIRECTION();
        assertTrue(dir == -1 || dir == 1);
    }

    @Test
    void moveUpShouldBeInRange() {
        Integer up = rc.getMOVE_UP();
        assertTrue(up >= 0 && up < 1000);
    }

    @Test
    void moveRightShouldBeInRange() {
        Integer right = rc.getMOVE_RIGHT();
        assertTrue(right >= 0 && right < 1000);
    }

    @Test
    void moveDownShouldBeInRange() {
        Integer down = rc.getMOVE_DOWN();
        assertTrue(down >= 0 && down < 1000);
    }

    @Test
    void moveLeftShouldBeInRange() {
        Integer left = rc.getMOVE_LEFT();
        assertTrue(left >= 0 && left < 1000);
    }

    @Test
    void gunRotationDirectionShouldBeOne() {
        Integer gunDir = rc.getROTATE_GUN_DIRECTION();
        assertTrue(gunDir == 1 || gunDir == -1);
    }

    @Test
    void gunRotationAngleShouldBeInRange() {
        Integer angle = rc.getROTATE_GUN();
        assertTrue(angle >= -360 && angle <= 360);
    }
}