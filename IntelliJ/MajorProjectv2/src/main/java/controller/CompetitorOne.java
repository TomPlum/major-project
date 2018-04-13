package controller;

import view.RobotObserver;

/**
 * ---------------------------------------------------------------------------------------------------
 * This robot's actions are entirely dependent on the character values from Twitter Tweets stored in a
 * MongoDB Atlas Cluster. It is controller via the RobotController that parses Tweets from the
 * TweetParser. A Twitter user is chosen at random to represent it and therefore choose its actions.
 * ---------------------------------------------------------------------------------------------------
 * @author Thomas Plumpton
 * @version 2.0.0
 */
@SuppressWarnings("unused")
public class CompetitorOne extends TwitterRobot implements ITwitterRobot {
    private static final int ROBOT_ID = 1;

    /**
     * The robots main event loop. This method is called every turn and contains
     * the robots main functionality for moving, turning, scanning and firing.
     */
    public void run() {
        initialConfiguration(ROBOT_ID);
        doTurn();
    }

    /**
     * Updates the values in the RobotObserver GUI Swing/AWT Panel.
     */
    public synchronized void updateRobotObserver() {
        //RobotObserver.setNumberOfTweetsOne(rc.getCurrentTweetArray().size());
        //setNumberOfTweetsOne(rc.getCurrentTweetArray().size());
        //setTurnNumber(numberOfTurns);
        //setRoundNumber(getRoundNum());
        //setUserOne(rc.getUSER());
    }
}
