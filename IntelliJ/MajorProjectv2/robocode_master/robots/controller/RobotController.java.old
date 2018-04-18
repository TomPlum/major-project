package controller;

import org.bson.Document;
import twitter.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class RobotController implements Runnable {
    private static String USER;
    private static Double robotX;
    private static Double robotY;
    private static Double FIRE_POWER = -1.0;
    private static Integer MOVE_UP = -1;
    private static Integer MOVE_RIGHT = -1;
    private static Integer MOVE_DOWN = -1;
    private static Integer MOVE_LEFT = -1;
    private static Integer ROTATE = 999;
    private static Integer ROTATE_DIRECTION = 99;
    private static Integer ROTATE_GUN = 999;
    private static Integer ROTATE_GUN_DIRECTION = 99;
    private Integer ROTATE_SCANNER = 999;
    private Integer ROTATE_SCANNER_DIRECTION = 99;
    private Integer SCAN_FREQUENCY = -1;

    private static TweetReader tr = new TweetReader();
    private static ArrayList<Document> allTweets;
    private static ArrayList<Document> currentTweetArray;
    private static ArrayList<String> newTweets;
    private static Document currentTweet;
    private static DecimalFormat dp1 = new DecimalFormat(".#");

    private static int CHARS_USED = 0;
    private static int TWEETS_USED = 0;

    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();

    public static void setNewTweets(ArrayList<String> tweets) {
        RobotController.newTweets = tweets;
    }

    public static ArrayList<String> getNewTweets() {
        return newTweets;
    }

    @Override
    public void run() {
        while (running){
            synchronized (pauseLock) {
                if (!running) {
                    break;
                }
                if (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        break;
                    }
                    if (!running) {
                        break;
                    }
                }
            }
            System.out.println("RobotController Running...");
            pause();
            System.out.println("Retrieving Users From MongoDB Cluster...");
            setRandomUser();
            System.out.println("Initialising " + getUSER() + "'s Tweets...");
            initialiseTweets();
            resume();
            stop();
        }
    }

    public void stop() {
        running = false;
        // you might also want to interrupt() the Thread that is
        // running this Runnable, too, or perhaps call:
        resume();
        // to unblock
    }

    public void pause() {
        // you may want to throw an IllegalStateException if !running
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Unblocks thread
        }
    }

    public RobotController() {
        //run();
        //setRandomUser();
        //initialiseTweets();
    }

    public void initialiseTweets() {
        if (USER == null) {
            System.out.println("RobotController User is not defined!");
        } else {
            allTweets = tr.getTweetsByUser(USER);
            currentTweetArray = new ArrayList<>(allTweets);
        }
    }

    public static void randomiseValues() {
        TweetParser parser = new TweetParser();
        updateCurrentTweet(); //Instantiate currentTweet.
        while(true) {
            if (currentTweetIsNotExhausted()) {
                if (allValuesValidated()) {
                    System.out.println("All RobotController Values Validated. Breaking!");
                    break;
                } else {
                    //Set FIRE_POWER
                    if (!FIRE_POWERisValid()) {
                        Integer fp1 = parser.getValue(getTweetChar());
                        Integer fp2 = parser.getValue(getTweetChar());
                        System.out.println("FIRE_POWER: fp1(" + fp1 + "), fp2(" + fp2 + ").");
                        setFIRE_POWER(createDouble(fp1, fp2));
                    }

                    //SET ROTATE_DIRECTION
                    if (!ROTATE_DIRECTIONisValid(ROTATE_DIRECTION)) {
                        Integer rd = parser.getValue(getTweetChar());
                        System.out.println("ROTATE DIRECTION: rd(" + rd + ").");
                        setROTATE_DIRECTION(createDirection(rd));
                    }

                    //Set ROTATE
                    if (!ROTATEisValid(ROTATE)) {
                        Integer r1 = parser.getValue(getTweetChar());
                        Integer r2 = parser.getValue(getTweetChar());
                        System.out.println("ROTATE: r1(" + r1 + "), r2(" + r2 + ").");
                        setROTATE(createAngle(r1, r2) * ROTATE_DIRECTION);
                    }

                    //Set MOVE_UP
                    if (!MOVEVEMENTisValid(MOVE_UP)) {
                        Integer mvup1 = parser.getValue(getTweetChar());
                        Integer mvup2 = parser.getValue(getTweetChar());
                        System.out.println("MOVE_UP: mvup1(" + mvup1 + "), mvup2(" + mvup2 + ").");
                        setMOVE_UP(createMovement(mvup1, mvup2));
                    }

                    //Set MOVE_RIGHT
                    if (!MOVEVEMENTisValid(MOVE_RIGHT)) {
                        Integer mvright1 = parser.getValue(getTweetChar());
                        Integer mvright2 = parser.getValue(getTweetChar());
                        System.out.println("MOVE_RIGHT: mvright1(" + mvright1 + "), mvright2(" + mvright2 + ").");
                        setMOVE_RIGHT(createMovement(mvright1, mvright2));
                    }

                    //Set MOVE_DOWN
                    if (!MOVEVEMENTisValid(MOVE_DOWN)) {
                        Integer mvdown1 = parser.getValue(getTweetChar());
                        Integer mvdown2 = parser.getValue(getTweetChar());
                        System.out.println("MOVE_DOWN: mvdown1(" + mvdown1 + "), mvdown2(" + mvdown2 + ").");
                        setMOVE_DOWN(createMovement(mvdown1, mvdown2));
                    }

                    //Set MOVE_LEFT
                    if (!MOVEVEMENTisValid(MOVE_LEFT)) {
                        Integer mvleft1 = parser.getValue(getTweetChar());
                        Integer mvleft2 = parser.getValue(getTweetChar());
                        System.out.println("MOVE_LEFT: mvleft1(" + mvleft1 + "), mvleft2(" + mvleft2 + ").");
                        setMOVE_LEFT(createMovement(mvleft1, mvleft2));
                    }

                    //Set ROTATE_GUN_DIRECTION
                    if (!ROTATE_DIRECTIONisValid(ROTATE_GUN_DIRECTION)) {
                        Integer rd = parser.getValue(getTweetChar());
                        System.out.println("ROTATE_GUN_DIRECTION: rd1(" + rd + ").");
                        setROTATE_GUN_DIRECTION(createDirection(rd));
                    }

                    //Set ROTATE_GUN
                    if (!ROTATEisValid(ROTATE_GUN)) {
                        Integer rg1 = parser.getValue(getTweetChar());
                        Integer rg2 = parser.getValue(getTweetChar());
                        System.out.println("ROTATE_GUN: rg1(" + rg1 + "), rg2(" + rg2 + ").");
                        setROTATE_GUN(createAngle(rg1, rg2));
                    }
                }
            } else {
                updateCurrentTweet(); //Each time we exhaust a tweet, remove one, but store it as current.
                System.out.println("There are " + currentTweetArray.size() + "/" + allTweets.size() + " left.");
            }
        }
    }

    private static boolean currentTweetIsNotExhausted() {
        return currentTweet.get("text").toString().length() > 0;
    }

    private static String getTweetChar() {
        try {
            String text = currentTweet.get("text").toString();
            String newText = text.substring(1, text.length());
            String ch = text.substring(0, 1);
            currentTweet.remove("text");
            currentTweet.put("text", newText);

            CHARS_USED++;

            return ch;
        } catch (NullPointerException e) {
            System.out.println("Current Tweet Is Null");
        }
        return null;
    }

    /**
     * Concatenates two integers into String, converts to double
     * @param s1 ten unit
     * @param s2 first decimal
     * @return double in format x.y
     */
    private static Double createDouble(Integer s1, Integer s2) {
        if (s1 > 10) {
            String num1 = s1.toString().substring(0, 1);
            String num2 = s1.toString().substring(1, 2);
            return Double.parseDouble(Integer.parseInt(num1) + "." + Integer.parseInt(num2));
        } else if (s2 > 10) {
            String num1 = s2.toString().substring(0, 1);
            String num2 = s2.toString().substring(1, 2);
            return Double.parseDouble(Integer.parseInt(num1) + "." + Integer.parseInt(num2));
        }

        //If Both < 10, just concat into Double
        return Double.parseDouble(s1 + "." + s2);
    }

    private static Integer createAngle(Integer i1, Integer i2) {
        String intString = i1.toString() + i2.toString();
        Integer concatInt = Integer.parseInt(intString);
        if (concatInt <= 360) {
            return concatInt;
        }

        //Max concat Int is if i1 and i2 were 25. So it would be 2525
        //2525 / 360 = 7.013888888888889. So we divide by that to make sure always < 360
        return  Math.round(concatInt / 7.013888888888889f);
    }

    private static Integer createDirection(Integer num) {
        if (num >= 0 || num <= 11) {
            return -1;
        }
        return 1;
    }

    private static Integer createMovement(Integer mv1, Integer mv2) {
        String moveString = mv1.toString() + mv2.toString();
        Integer concatInt = Integer.parseInt(moveString);

        if (concatInt < 1000) {
            return concatInt;
        }

        //Max concat Int is if mv1 and mv2 were 25. so it would be 2525
        //2525 / 1000 = 2.525. So we divide by that to make sure always < 1000
        return Math.round(concatInt / 2.525f);
    }

    private static void updateCurrentTweet() {
        if (currentTweetArray != null && currentTweetArray.size() > 0) {
            currentTweet = currentTweetArray.remove(0);

            TweetSanitiser ts = new TweetSanitiser(currentTweet.get("text").toString());
            currentTweet.put("text", ts.extractLetters());

            TWEETS_USED++;
        } else {
            System.out.println("Current Tweet Array Is Empty!");
        }
    }

    private static boolean allValuesValidated() {
        //FIRE_POWER
        return FIRE_POWERisValid() &&
                ROTATE_DIRECTIONisValid(ROTATE_DIRECTION) &&
                ROTATEisValid(ROTATE) &&
                MOVEVEMENTisValid(MOVE_UP) &&
                MOVEVEMENTisValid(MOVE_RIGHT) &&
                MOVEVEMENTisValid(MOVE_DOWN) &&
                MOVEVEMENTisValid(MOVE_LEFT) &&
                ROTATE_DIRECTIONisValid(ROTATE_GUN_DIRECTION) &&
                ROTATEisValid(ROTATE_GUN);
    }

    public static void invalidateAllValues() {
        FIRE_POWER = -1.0;
        ROTATE_DIRECTION = 99;
        ROTATE = 999;
        MOVE_UP = -1;
        MOVE_RIGHT = -1;
        MOVE_DOWN = -1;
        MOVE_LEFT = -1;
        ROTATE_GUN_DIRECTION = 99;
        ROTATE = 999;
    }

    private void setRandomUser() {
        /*
        GameConfigurer config = new GameConfigurer();
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
                setUSER(robotUser);
                correctUser = true;
            } else {
                System.out.println("User Does Not Exist!");
            }
        }
        */
        //Get all usernames from cluster
        ArrayList<String> users = tr.getAllUsernames();

        //Generate Random Number (From 0 - users.size())
        Random rand = new Random();
        Integer n = rand.nextInt(users.size());

        //Get random user string and set RobotController USER
        String user = users.get(n);
        setUSER(user);
        System.out.println("User set to " + user);
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }

    /**
     * Robot Firepower. Min 0.1, Max 3.0
     * @param FIRE_POWER Firepower (0.1 - 3.0)
     */
    public static void setFIRE_POWER(double FIRE_POWER) {
        RobotController.FIRE_POWER = Double.parseDouble(dp1.format(FIRE_POWER));
    }

    public static void setROTATE(Integer ROTATE) {
        RobotController.ROTATE = ROTATE;
    }

    public static Integer getMOVE_UP() {
        return MOVE_UP;
    }

    public static void setMOVE_UP(Integer MOVE_UP) {
        RobotController.MOVE_UP = MOVE_UP;
    }

    public Integer getMOVE_RIGHT() {
        return MOVE_RIGHT;
    }

    public static void setMOVE_RIGHT(Integer MOVE_RIGHT) {
        RobotController.MOVE_RIGHT = MOVE_RIGHT;
    }

    public static Integer getMOVE_DOWN() {
        return MOVE_DOWN;
    }

    public static void setMOVE_DOWN(Integer MOVE_DOWN) {
        RobotController.MOVE_DOWN = MOVE_DOWN;
    }

    public Integer getMOVE_LEFT() {
        return MOVE_LEFT;
    }

    public static void setMOVE_LEFT(Integer MOVE_LEFT) {
        RobotController.MOVE_LEFT = MOVE_LEFT;
    }

    public static Integer getROTATE_DIRECTION() {
        return ROTATE_DIRECTION;
    }

    public static void setROTATE_DIRECTION(Integer ROTATE_DIRECTION) {
        RobotController.ROTATE_DIRECTION = ROTATE_DIRECTION;
    }

    public static Integer getROTATE_GUN() {
        return ROTATE_GUN;
    }

    public static void setROTATE_GUN(Integer ROTATE_GUN) {
        RobotController.ROTATE_GUN = ROTATE_GUN;
    }

    public Integer getROTATE_GUN_DIRECTION() {
        return ROTATE_GUN_DIRECTION;
    }

    public static void setROTATE_GUN_DIRECTION(Integer ROTATE_GUN_DIRECTION) {
        RobotController.ROTATE_GUN_DIRECTION = ROTATE_GUN_DIRECTION;
    }

    public static double getFIRE_POWER() {
        return FIRE_POWER;
    }

    public ArrayList<Document> getAllTweets() {
        return allTweets;
    }

    public static String getUSER() {
        return USER;
    }

    public Integer getROTATE() {
        return ROTATE;
    }

    public int getCHARS_USED() {
        return CHARS_USED;
    }

    public int getTWEETS_USED() {
        return TWEETS_USED;
    }

    public static void setRobotX(Double robotX) {
        RobotController.robotX = robotX;
    }

    public static void setRobotY(Double robotY) {
        RobotController.robotY = robotY;
    }

    /*---------------------------------------------------
    /*--------------- VALIDATION METHODS ----------------
    /*-------------------------------------------------*/

    private static boolean FIRE_POWERisValid() {
        return FIRE_POWER >= 0.0 && FIRE_POWER <= 3.0;
    }

    private static boolean ROTATE_DIRECTIONisValid(Integer dir) {
        return dir == 1 || dir == -1;
    }

    private static boolean ROTATEisValid(Integer angle) {
        return angle >= -360 && angle <= 360;
    }

    private static boolean MOVEVEMENTisValid(Integer movement) {
        return movement >= 0 && movement < 1000;
    }


    public static void main(String[] args) {
        /*
        RobotController rc = new RobotController();
        TweetReader tr = new TweetReader();

        //Get all usernames from cluster
        ArrayList<String> users = tr.getAllUsernames();

        //Generate Random Number (From 0 - users.size())
        Random rand = new Random();
        Integer n = rand.nextInt(users.size());

        //Get random user string and set RobotController USER
        String user = users.get(n);
        rc.setUSER(user);
        System.out.println("RobotController Username set to " + rc.getUSER());

        //Initialise RobotController Tweets
        rc.initialiseTweets();

        ArrayList<Document> tweets = rc.getAllTweets();
        System.out.println("Getting Tweets from Twitter Account: " + rc.getUSER());

        //Check if they have been downloaded and set correctly
        System.out.println("The RobotController has " + tweets.size() + " Tweets.");

        rc.invalidateAllValues();
        rc.randomiseValues();
        System.out.println("FirePower: " + rc.getFIRE_POWER());
        if (rc.getROTATE_DIRECTION() == -1) {
            System.out.println("Rotate Direction: " + rc.getROTATE_DIRECTION() + " (Anti-Clockwise)");
        } else {
            System.out.println("Rotate Direction: " + rc.getROTATE_DIRECTION() + " (Clockwise)");
        }
        System.out.println("Rotate: " + rc.getROTATE());
        */
    }
}
