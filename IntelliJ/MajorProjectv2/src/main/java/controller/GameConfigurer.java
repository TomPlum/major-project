package controller;

import robocode.Robot;
import robocode.control.*;

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
    public static RobotController controller = new RobotController();

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
        RobotSpecification[] selectedRobots = engine.getLocalRepository("controller.TestRobot, controller.TwitterRobot");

        BattleSpecification battleSpec = new BattleSpecification(NO_OF_ROUNDS, battlefield, selectedRobots);

        //Before We Start The Battle - Initialise Tweets
        //RobotController controller = new RobotController();
        //RobotController2 controller2 = new RobotController2();
        controller.run();
        //controller2.run();

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
