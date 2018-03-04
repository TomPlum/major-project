package university.robocode;

import org.bson.Document;
import university.twitter.TweetParser;
import university.twitter.TweetReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class RobotController  {
    //Robot Attributes
    private String USER;
    private double FIRE_POWER;
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

    public RobotController() {
        initialiseTweets();

    }

    public void randomiseValues() {
        //Parser takes the first Tweets from the Array.
        TweetParser parser = new TweetParser();
        updateCurrentTweet(); //Now we remove it, once.
        while(true) {
            if (currentTweet.get("text").toString().length() > 0) {
                if (allValuesValidated()) {
                    break;
                } else {
                    int fp1 = parser.getValue(getTweetChar());
                    int fp2 = parser.getValue(getTweetChar());
                    setFIRE_POWER(createDouble(fp1, fp2));
                }
            } else {
                updateCurrentTweet(); //Each time we exhaust a tweet, remove one, but store it as current.
                System.out.println("There are " + currentTweetArray.size() + "/" + allTweets.size() + " left.");
            }
        }
        invalidateAllValues();
    }

    private String getTweetChar() {
        String text = currentTweet.get("text").toString();
        String newText = text.substring(1, text.length() - 1);
        String ch = text.substring(0, 1);
        currentTweet.remove("text");
        currentTweet.put("key", newText);
        return ch;
    }

    /**
     * Concatenates two integers into String, converts to double
     * @param s1 ten unit
     * @param s2 first decimal
     * @return double in format x.y
     */
    private double createDouble(int s1, int s2) {
        return Double.parseDouble(s1 + "." + s2);
    }

    private void updateCurrentTweet() {
        if (currentTweetArray.size() > 0) {
            currentTweet = currentTweetArray.remove(0);
        } else {
            System.out.println("Current Tweet Array Is Empty!");
        }
    }

    private boolean allValuesValidated() {
        //FIRE_POWER
        if (FIRE_POWER < 0 || FIRE_POWER > 3) {
            return false;
        }
        /*
        //START_X
        if (START_X < 0 || START_X > BATTLEFIELD_W) {
            return false;
        }
        */
        return true;

    }

    private void invalidateAllValues() {
        FIRE_POWER = -1;
        START_X = -1;
        START_Y = -1;
        MOVE_UP = -1;
        MOVE_RIGHT = -1;
        MOVE_DOWN = -1;
        MOVE_LEFT = -1;
        ROTATE = -1;
        ROTATE_DIRECTION = -1;
    }

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
}
