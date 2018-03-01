package controller;

import robocode.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.LoggingPermission;

public class TwitterRobot extends AdvancedRobot {
    private RobotController rc = new RobotController();
    private double robotX;
    private double robotY;
        /*
    private void initialiseRobot() {
        System.out.println("Available Users: \n");
        ArrayList<String> users = config.getAvailableUsers();
        for (String user : users) {
            System.out.println(user);
        }
        boolean correctUser = false;
        String robotUser;
        while(!correctUser) {
            System.out.println("Please enter a username: ");
            Scanner sc = new Scanner(System.in);
            robotUser = sc.nextLine();
            if (config.userAvailable(robotUser)) {
                rc.setUSER(robotUser);
                correctUser = true;
            } else {
                System.out.println("User Does Not Exist!");
            }
        }
    }
    */

    public void onStatus(StatusEvent e) {
        RobotStatus robotStatus = e.getStatus();
        this.robotX = robotStatus.getX();
        this.robotY = robotStatus.getY();
    }

    public void run() {
        //GameConfigurer config = new GameConfigurer();

        while (true) {
            rc.randomiseValues();

            ahead(rc.getMOVE_UP());
            if (rc.getROTATE_DIRECTION() == 1) {
                turnGunRight(rc.getROTATE_GUN());
            } else {
                turnGunLeft(rc.getROTATE_GUN() * -1);
            }

            rc.invalidateAllValues();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        fire(rc.getFIRE_POWER());
    }

    public void onHitWall(HitWallEvent e) {
        back(rc.getMOVE_DOWN());
    }
}
