package controller;

import robocode.Robot;
import robocode.control.*;
import twitter.TweetSerialiser;
import view.RobotObserver;
import java.io.IOException;

/**
 * ----------------------------------------------------------------------------------------------
 * This class establishes the configuration options for the RobocodeEngine and launches the game.
 * ----------------------------------------------------------------------------------------------
 * @author Thomas Plumpton
 * @version 1.0.0
 */
class GameConfigurer {
    private static final int BATTLEFIELD_W = 1000; //Pixels
    private static final int BATTLEFIELD_H = 1000; //Pixels
    private static final int NO_OF_ROUNDS = 5; //Default: 10
    private static final int INACTIVITY_TIME = 2000; //Default: 450 (Turns)
    private static final double GUN_COOLING_RATE = 0.1; //Default: 0.1
    private static final boolean HIDE_ENEMY_NAMES = false; //Default: false

    /**
     * Starts a Robocode Battle with the specified configuration options.
     */
    static void startBattle() {
        //Robocode Messages & Errors
        RobocodeEngine.setLogMessagesEnabled(true);
        RobocodeEngine.setLogErrorsEnabled(true);

        // Create the RobocodeEngine
        RobocodeEngine engine = new RobocodeEngine(new java.io.File("C:/Users/thoma/Dropbox (University)/Year 3/CPU6001 - Major Project (Amanda & Louise)/IntelliJ/MajorProjectv2/robocode_master"));
        System.out.println("Running Robocode Version: " + engine.getVersion());

        //Add BattleObserver To Engine
        engine.addBattleListener(new BattleObserver());

        //Show Robocode Battle View (GUI Window)
        engine.setVisible(true);

        System.out.println("Working Dir: " + RobocodeEngine.getCurrentWorkingDir());
        System.out.println("Robots Dir: " + RobocodeEngine.getRobotsDir());

        //Setup Battle Specification
        BattlefieldSpecification battlefield = new BattlefieldSpecification(BATTLEFIELD_W, BATTLEFIELD_H);
        RobotSpecification[] selectedRobots = engine.getLocalRepository("controller.CompetitorOne, controller.CompetitorTwo");

        BattleSpecification battleSpec = new BattleSpecification(NO_OF_ROUNDS, INACTIVITY_TIME, GUN_COOLING_RATE, HIDE_ENEMY_NAMES, battlefield, selectedRobots);

        //Open RobotObserver GUI
        //RobotObserver.startObserving();

        //Get Tweets & Serialise
        TweetSerialiser tweetSerialiser = new TweetSerialiser();
        try {
            tweetSerialiser.serialiseTweets(1);
            tweetSerialiser.serialiseTweets(2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Close Connection To MongoDB Atlas Cluster
        tweetSerialiser.getTweetReader().getConn().disconnect();

        //Start Battle
        engine.runBattle(battleSpec, true); //Wait Until Battle Finished

        //Clean Up Our RobocodeEngine
        engine.close();
    }

    /**
     * Closes the JVM (Java Virtual Machine)
     */
    static void closeJVM() {
        //Ensure JVM Is Shut-Down Properly
        System.exit(0);
    }
}
