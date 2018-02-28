package controller;

import org.bson.Document;
import twitter.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class RobotController  {

    /*
     * MAKE ALL MEMBERS INTEGER (WRAPPER CLASS) AND INSTANTIATE TO NULL
     */
    //Robot Attributes
    private String USER;
    private Double FIRE_POWER;
    private int BATTLEFIELD_W;
    private int BATTLEFIELD_H;
    private int START_X;
    private int START_Y;
    private int MOVE_UP;
    private int MOVE_RIGHT;
    private int MOVE_DOWN;
    private int MOVE_LEFT;
    private int ROTATE;
    private int ROTATE_DIRECTION;
    private int ROTATE_GUN;
    private int ROTATE_GUN_DIRECTION;
    private int ROTATE_SCANNER;
    private int ROTATE_SCANNER_DIRECTION;
    private int SCAN_FREQUENCY;
    private int NO_OF_ROUNDS;
    private int GUN_COOLING_RATE;
    private int INACTIVITY_TIME;
    private int SENTRY_BORDER_SIZE;

    private TweetReader tr = new TweetReader();
    private ArrayList<Document> allTweets;
    private ArrayList<Document> currentTweetArray;
    private Document currentTweet;
    private DecimalFormat dp1 = new DecimalFormat(".#");

    public void initialiseTweets() {
        try {
            allTweets = tr.getTweetsByUser(USER);
            currentTweetArray = new ArrayList<>(allTweets);
        } catch (NullPointerException e) {
            if (USER == null) {
                System.out.println("RobotController User is not defined!");
            }
        }

    }

    public void randomiseValues() {
        TweetParser parser = new TweetParser();
        updateCurrentTweet(); //Instantiate currentTweet.
        while(true) {
            if (currentTweetIsNotExhausted()) {
                if (allValuesValidated()) {
                    System.out.println("All RobotController Values Validated. Breaking!");
                    break;
                } else {
                    //Set FIRE_POWER
                    int fp1 = parser.getValue(getTweetChar());
                    int fp2 = parser.getValue(getTweetChar());
                    System.out.println("FIRE_POWER: fp1(" + fp1 + "), fp2(" + fp2 + ").");
                    setFIRE_POWER(createDouble(fp1, fp2));
                }
            } else {
                updateCurrentTweet(); //Each time we exhaust a tweet, remove one, but store it as current.
                System.out.println("There are " + currentTweetArray.size() + "/" + allTweets.size() + " left.");
            }
        }
    }

    private boolean currentTweetIsNotExhausted() {
        return currentTweet.get("text").toString().length() > 0;
    }

    private String getTweetChar() {
        try {
            String text = currentTweet.get("text").toString();
            String newText = text.substring(1, text.length());
            String ch = text.substring(0, 1);
            currentTweet.remove("text");
            currentTweet.put("text", newText);
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
    private Double createDouble(int s1, int s2) {
        return Double.parseDouble(s1 + "." + s2);
    }

    private void updateCurrentTweet() {
        if (currentTweetArray != null && currentTweetArray.size() > 0) {
            currentTweet = currentTweetArray.remove(0);

            TweetSanitiser ts = new TweetSanitiser(currentTweet.get("text").toString());
            currentTweet.put("text", ts.extractLetters());
        } else {
            System.out.println("Current Tweet Array Is Empty!");
        }
    }

    private boolean allValuesValidated() {
        //FIRE_POWER
        if (FIRE_POWER < 0 || FIRE_POWER > 3 || FIRE_POWER == null) {
            return false;
        }

        return true;
    }

    public void invalidateAllValues() {
        FIRE_POWER = -1.0;
        START_X = -1;
        START_Y = -1;
        MOVE_UP = -1;
        MOVE_RIGHT = -1;
        MOVE_DOWN = -1;
        MOVE_LEFT = -1;
        ROTATE = -1;
        ROTATE_DIRECTION = -1;
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }

    /**
     * Robot Firepower. Min 0.1, Max 3.0
     * @param FIRE_POWER Firepower (0.1 - 3.0)
     */
    public void setFIRE_POWER(double FIRE_POWER) {
        this.FIRE_POWER = Double.parseDouble(dp1.format(FIRE_POWER));
    }

    public double getFIRE_POWER() {
        return FIRE_POWER;
    }

    public ArrayList<Document> getAllTweets() {
        return allTweets;
    }

    public String getUSER() {
        return USER;
    }

    public ArrayList<Document> getCurrentTweetArray() {
        return currentTweetArray;
    }

    public Document getCurrentTweet() {
        return currentTweet;
    }

    public static void main(String[] args) {
        RobotController rc = new RobotController();
        TweetReader tr = new TweetReader();

        //Get all usernames from cluster
        ArrayList<String> users = tr.getAllUsernames();

        //Generate Random Number (From 0 - users.size())
        Random rand = new Random();
        int n = rand.nextInt(users.size());

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
    }
}
