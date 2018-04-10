package controller;

import robocode.control.*;
import twitter.TweetSerialiser;
import view.RobotObserver;

/**
 * ----------------------------------------------------------------------------------------------
 * This class establishes the configuration options for the RobocodeEngine and launches the game.
 * ----------------------------------------------------------------------------------------------
 * @author Thomas Plumpton
 * @version 1.0.0
 */
public class GameConfigurer {
    private static final int BATTLEFIELD_W = 1000;
    private static final int BATTLEFIELD_H = 1000;
    private static final int NO_OF_ROUNDS = 5;

    /**
     * Starts a Robocode Battle with the specified configuration options.
     */
    public static void startBattle() {
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
        RobotSpecification[] selectedRobots = engine.getLocalRepository("controller.TwitterRobot2, controller.TwitterRobot2");

        BattleSpecification battleSpec = new BattleSpecification(NO_OF_ROUNDS, 2000, 0.1, false, battlefield, selectedRobots);

        //Open RobotObserver GUI
        RobotObserver.startObserving();

        //Get Tweets & Serialise
        TweetSerialiser tweetSerialiser = new TweetSerialiser();
        tweetSerialiser.serialiseTweets();

        //Start Battle
        engine.runBattle(battleSpec, true); //Wait Until Battle Finished

        //Clean Up Our RobocodeEngine
        engine.close();
    }

    /**
     * Closes the JVM (Java Virtual Machine)
     */
    public static void closeJVM() {
        //Ensure JVM Is Shut-Down Properly
        System.exit(0);
    }
}
