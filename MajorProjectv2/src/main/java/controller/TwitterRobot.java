package controller;

import robocode.*;

import java.util.ArrayList;
import java.util.Scanner;

public class TwitterRobot extends AdvancedRobot {
    private RobotController rc = new RobotController();
    private GameConfigurer config = new GameConfigurer();

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

    public void run() {
        //rc.randomiseValues();

        while (true) {
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }

        //rc.invalidateAllValues();
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        fire(1);
    }
}
