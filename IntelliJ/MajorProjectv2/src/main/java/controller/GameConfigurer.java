package controller;

import robocode.control.*;

public class GameConfigurer {
    private static final int BATTLEFIELD_W = 1000;
    private static final int BATTLEFIELD_H = 1000;
    private static final int NO_OF_ROUNDS = 5;

    public static void startBattle() {
        //Robocode Messages & Errors
        RobocodeEngine.setLogMessagesEnabled(true);
        RobocodeEngine.setLogErrorsEnabled(true);

        // Create the RobocodeEngine
        RobocodeEngine engine = new RobocodeEngine(new java.io.File("C:/Users/thoma/Dropbox (University)/Year 3/CPU6001 - Major Project (Amanda & Louise)/IntelliJ/MajorProjectv2/robocode_master"));
        System.out.println("Running Robocode Version: " + engine.getVersion());

        //Add BattleObserver TO Engine
        engine.addBattleListener(new BattleObserver());

        //Show Robocode Battle View (GUI Window)
        engine.setVisible(true);

        System.out.println("Working Dir: " + RobocodeEngine.getCurrentWorkingDir());
        System.out.println("Robots Dir: " + RobocodeEngine.getRobotsDir());

        //Setup Battle Specification
        BattlefieldSpecification battlefield = new BattlefieldSpecification(BATTLEFIELD_W, BATTLEFIELD_H);
        RobotSpecification[] selectedRobots = engine.getLocalRepository("sample.Crazy, sample.RobotJDK6");

        BattleSpecification battleSpec = new BattleSpecification(NO_OF_ROUNDS, battlefield, selectedRobots);

        // Run our specified battle and let it run till it is over
        engine.runBattle(battleSpec, true); // waits till the battle finishes

        //Clean Up Our RobocodeEngine
        engine.close();

        //Ensure JVM Is Shut-Down Properly
        System.exit(0);
    }
}
