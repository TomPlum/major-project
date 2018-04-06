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
    private RobotController rc = new RobotController();

    /**
     * Called whenever the Robot's status changes.
     * @param e Robocode StatusEvent
     */
    public void onStatus(StatusEvent e) {
        //Update Robot's Current Coordinates
        RobotStatus robotStatus = e.getStatus();
        rc.setRobotX(robotStatus.getX());
        rc.setRobotY(robotStatus.getY());
    }

    /**
     * The robots main event loop. This method is called every turn and contains
     * the robots main functionality for moving, turning, scanning and firing.
     */
    public void run() {
        while (true) {
            rc.randomiseValues();

            ahead(rc.getMOVE_UP());
            if (rc.getROTATE_DIRECTION() == 1) {
                turnGunRight(rc.getROTATE_GUN());
            } else {
                turnGunLeft(rc.getROTATE_GUN() * -1);
            }

            rc.invalidateAllValues();
            BattleObserver.numberOfTurns++;
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
}
