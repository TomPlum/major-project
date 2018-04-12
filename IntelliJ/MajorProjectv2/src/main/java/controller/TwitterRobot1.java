package controller;

import org.bson.Document;
import robocode.*;
import twitter.TweetSerialiser;
import view.RobotObserver;
import java.awt.Color;
import java.util.ArrayList;
import static controller.BattleObserver.numberOfTurns;
import static view.RobotObserver.*;

/**
 * ---------------------------------------------------------------------------------------------------
 * This robot's actions are entirely dependent on the character values from Twitter Tweets stored in a
 * MongoDB Atlas Cluster. It is controller via the RobotController that parses Tweets from the
 * TweetParser. A Twitter user is chosen at random to represent it and therefore choose its actions.
 * ---------------------------------------------------------------------------------------------------
 * @author Thomas Plumpton
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class TwitterRobot1 extends AdvancedRobot {
    private static int skippedTurns = 0;
    private static RobotController2 rc = new RobotController2();
    private static TweetSerialiser serialiser = new TweetSerialiser();
    private static ArrayList<Document> tweets;
    private static boolean isWaiting = true;
    /**
     * Called whenever the Robot's status changes.
     * @param e Robocode StatusEvent
     */
    public void onStatus(StatusEvent e) {
        //Update Robot's Current Coordinates
        RobotStatus robotStatus = e.getStatus();
        rc.setRobotX(robotStatus.getX());
        rc.setRobotY(robotStatus.getY());
        setXOrdinateOne(robotStatus.getX());
    }

    /**
     * The robots main event loop. This method is called every turn and contains
     * the robots main functionality for moving, turning, scanning and firing.
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        /*
        while (isWaiting) {
            doNothing();
            try {
                Thread.sleep(3000);
                //System.out.println("Loading Tweets. Waiting 3 Seconds...");
                tweets = serialiser.readTweets();
                isWaiting = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //System.out.println(tweets);
        RobotObserver.setNumberOfTweetsOne(tweets.size());


        */

        //Increment Number of Turns
        numberOfTurns++;

        //De-Serialise Tweets & Pass To RobotController
        tweets = serialiser.readTweets(1);
        rc.initialiseTweets(tweets);
        rc.setUSER(tweets.get(0).get("screenName").toString());

        //Set Twitter Palette Colours
        setBodyColor(new Color(29, 202, 255));
        setGunColor(new Color(0, 172, 237));
        setRadarColor(new Color(0, 132, 180));
        setBulletColor(new Color(29, 202, 255));
        setScanColor(new Color(192, 222, 237));

        //Loop Indefinitely
        while (true) {
            //Update Robot Observer GUI
            updateRobotObserver();

            //Randomise Robot Values
            rc.randomiseValues();

            //Move Ahead
            ahead(rc.getMOVE_UP());

            //Rotate Robot Body
            if (rc.getROTATE_DIRECTION() == 1) {
                turnGunRight(rc.getROTATE_GUN());
            } else {
                turnGunLeft(rc.getROTATE_GUN() * -1);
            }

            //Invalidate All rc Values
            rc.invalidateAllValues();
        }
    }

    /**
     * Called whenever the robots scanner finds another robot.
     * @param e Robocode ScannedRobotEvent
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        fire(rc.getFIRE_POWER());
    }

    /**
     * Called whenever the robot collides with one of the arena walls.
     * @param e Robocode HitWallEvent
     */
    public void onHitWall(HitWallEvent e) {
        back(rc.getMOVE_DOWN());
    }

    /**
     * Called whenever the robot dies.
     * @param e Robocode DeathEvent
     */
    public void onDeath(DeathEvent e) {
        //Count time up until, then record time taken to die. Save in DB.
    }

    /**
     * Called whenever the robot performs no action and skips the turn/
     * @param e Robocode SkippedTurnEvent
     */
    public void onSkippedTurn(SkippedTurnEvent e) {
        System.out.println("Doing Nothing. Skipping Turn.");
        skippedTurns++;
    }

    private void updateRobotObserver() {
        setNumberOfTweetsOne(rc.getCurrentTweetArray().size());
        setTurnNumber(numberOfTurns);
        setRoundNumber(getRoundNum());
        setUserOne(rc.getUSER());
    }
}
