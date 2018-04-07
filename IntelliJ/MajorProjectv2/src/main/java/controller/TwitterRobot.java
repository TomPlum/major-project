package controller;

import robocode.*;

/**
 * ---------------------------------------------------------------------------------------------------
 * This robot's actions are entirely dependent on the character values from Twitter Tweets stored in a
 * MongoDB Atlas Cluster. It is controller via the RobotController that parses Tweets from the
 * TweetParser. A Twitter user is chosen at random to represent it and therefore choose its actions.
 * ---------------------------------------------------------------------------------------------------
 * @author Thomas Plumpton
 * @version 1.0.0
 */
public class TwitterRobot extends AdvancedRobot {
    private int turns = 0;

    /**
     * Called whenever the Robot's status changes.
     * @param e Robocode StatusEvent
     */
    public void onStatus(StatusEvent e) {
        //Update Robot's Current Coordinates
        RobotStatus robotStatus = e.getStatus();
        RobotController.setRobotX(robotStatus.getX());
        RobotController.setRobotY(robotStatus.getY());
    }

    /**
     * The robots main event loop. This method is called every turn and contains
     * the robots main functionality for moving, turning, scanning and firing.
     */
    public void run() {
        while (true) {
            turns++;
            RobotController.randomiseValues();

            ahead(RobotController.getMOVE_UP());
            if (RobotController.getROTATE_DIRECTION() == 1) {
                turnGunRight(RobotController.getROTATE_GUN());
            } else {
                turnGunLeft(RobotController.getROTATE_GUN() * -1);
            }

            RobotController.invalidateAllValues();
            BattleObserver.numberOfTurns++;
        }
    }

    /**
     * Called whenever the robots scanner finds another robot.
     * @param e Robocode ScannedRobotEvent
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        fire(RobotController.getFIRE_POWER());
    }

    /**
     * Called whenever the robot collides with one of the arena walls.
     * @param e Robocode HitWallEvent
     */
    public void onHitWall(HitWallEvent e) {
        back(RobotController.getMOVE_DOWN());
    }

    /**
     * Called whenever the robot dies.
     * @param e Robocode DeathEvent
     */
    public void onDeath(DeathEvent e) {
        //Count time up until, then record time taken to die. Save in DB.
    }
}
