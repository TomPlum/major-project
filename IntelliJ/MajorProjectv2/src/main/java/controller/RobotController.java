package controller;

import org.bson.Document;
import twitter.TweetParser;
import twitter.TweetReader;
import twitter.TweetSanitiser;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class RobotController {
    //Robot Attribute Values
    private String USER;
    private Double robotX;
    private Double robotY;
    private Double FIRE_POWER = -1.0;
    private Integer MOVE_UP = -1;
    private Integer MOVE_RIGHT = -1;
    private Integer MOVE_DOWN = -1;
    private Integer MOVE_LEFT = -1;
    private Integer ROTATE = 999;
    private Integer ROTATE_DIRECTION = 99;
    private Integer ROTATE_GUN = 999;
    private Integer ROTATE_GUN_DIRECTION = 99;
    private Integer ROTATE_SCANNER = 999;
    private Integer ROTATE_SCANNER_DIRECTION = 99;
    //private Integer SCAN_FREQUENCY = -1;

    //Components
    private TweetReader tr = new TweetReader();
    private TweetParser parser = new TweetParser();
    private ArrayList<Document> allTweets;
    private ArrayList<Document> currentTweetArray;
    private Document currentTweet;
    private DecimalFormat dp1 = new DecimalFormat(".#");

    //Data Analysis
    private int CHARS_USED = 0;
    private int TWEETS_USED = 0;

    /**
     * Sets the RobotControllers Tweets from the selected user.
     * Also copies this ArrayList into the current array for manipulation.
     * @param allTweetsByUser ArrayList of Twitter4J Status Objects (Documents)
     */
    public void initialiseTweets(ArrayList<Document> allTweetsByUser) {
        allTweets = allTweetsByUser;
        currentTweetArray = allTweetsByUser;
        updateCurrentTweet(); //Set Current Tweet so randomiseValues() has a starting value
        System.out.println("Tweets Initialised.");
    }

    /**
     * Sets the TwitterRobot's movements and attacking values from the characters in the Tweets.
     */
    public void randomiseValues() {
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
                        //System.out.println("FIRE_POWER: fp1(" + fp1 + "), fp2(" + fp2 + ").");
                        setFIRE_POWER(createDouble(fp1, fp2));
                    }

                    //SET ROTATE_DIRECTION
                    if (!ROTATE_DIRECTIONisValid(ROTATE_DIRECTION)) {
                        Integer rd = parser.getValue(getTweetChar());
                        //System.out.println("ROTATE DIRECTION: rd(" + rd + ").");
                        setROTATE_DIRECTION(createDirection(rd));
                    }

                    //Set ROTATE
                    if (!ROTATEisValid(ROTATE)) {
                        Integer r1 = parser.getValue(getTweetChar());
                        Integer r2 = parser.getValue(getTweetChar());
                        //System.out.println("ROTATE: r1(" + r1 + "), r2(" + r2 + ").");
                        setROTATE(createAngle(r1, r2) * ROTATE_DIRECTION);
                    }

                    //Set MOVE_UP
                    if (!MOVEVEMENTisValid(MOVE_UP)) {
                        Integer mvup1 = parser.getValue(getTweetChar());
                        Integer mvup2 = parser.getValue(getTweetChar());
                        //System.out.println("MOVE_UP: mvup1(" + mvup1 + "), mvup2(" + mvup2 + ").");
                        setMOVE_UP(createMovement(mvup1, mvup2));
                    }

                    //Set MOVE_RIGHT
                    if (!MOVEVEMENTisValid(MOVE_RIGHT)) {
                        Integer mvright1 = parser.getValue(getTweetChar());
                        Integer mvright2 = parser.getValue(getTweetChar());
                        //System.out.println("MOVE_RIGHT: mvright1(" + mvright1 + "), mvright2(" + mvright2 + ").");
                        setMOVE_RIGHT(createMovement(mvright1, mvright2));
                    }

                    //Set MOVE_DOWN
                    if (!MOVEVEMENTisValid(MOVE_DOWN)) {
                        Integer mvdown1 = parser.getValue(getTweetChar());
                        Integer mvdown2 = parser.getValue(getTweetChar());
                        //System.out.println("MOVE_DOWN: mvdown1(" + mvdown1 + "), mvdown2(" + mvdown2 + ").");
                        setMOVE_DOWN(createMovement(mvdown1, mvdown2));
                    }

                    //Set MOVE_LEFT
                    if (!MOVEVEMENTisValid(MOVE_LEFT)) {
                        Integer mvleft1 = parser.getValue(getTweetChar());
                        Integer mvleft2 = parser.getValue(getTweetChar());
                        //System.out.println("MOVE_LEFT: mvleft1(" + mvleft1 + "), mvleft2(" + mvleft2 + ").");
                        setMOVE_LEFT(createMovement(mvleft1, mvleft2));
                    }

                    //Set ROTATE_GUN_DIRECTION
                    if (!ROTATE_DIRECTIONisValid(ROTATE_GUN_DIRECTION)) {
                        Integer rd = parser.getValue(getTweetChar());
                        //System.out.println("ROTATE_GUN_DIRECTION: rd1(" + rd + ").");
                        setROTATE_GUN_DIRECTION(createDirection(rd));
                    }

                    //Set ROTATE_GUN
                    if (!ROTATEisValid(ROTATE_GUN)) {
                        Integer rg1 = parser.getValue(getTweetChar());
                        Integer rg2 = parser.getValue(getTweetChar());
                        //System.out.println("ROTATE_GUN: rg1(" + rg1 + "), rg2(" + rg2 + ").");
                        setROTATE_GUN(createAngle(rg1, rg2));
                    }

                    //Set ROTATE_SCANNER
                    if (!ROTATEisValid(ROTATE_SCANNER)) {
                        Integer rs1 = parser.getValue(getTweetChar());
                        Integer rs2 = parser.getValue(getTweetChar());
                        //System.out.println("ROTATE_SCANNER: rs1(" + rs1 + "), rs2(" + rs2 + ").");
                        setROTATE_SCANNER(createAngle(rs1, rs2));
                    }

                    //Set ROTATE_SCANNER_DIRECTION
                    if (!ROTATE_DIRECTIONisValid(ROTATE_SCANNER_DIRECTION)) {
                        Integer rsd1 = parser.getValue(getTweetChar());
                        //System.out.println("ROTATE_SCANNER_DIRECTION: rsd1(" + rsd1 + ")");
                        setROTATE_SCANNER_DIRECTION(createDirection(rsd1));
                    }
                }
            } else {
                updateCurrentTweet(); //Each time we exhaust a tweet, remove one, but store it as current.
                System.out.println("There are " + currentTweetArray.size() + "/" + allTweets.size() + " left.");
            }
        }
    }

    /**
     * Checks if the current Tweet has been used by the TweetParser
     * @return If Tweet is exhausted
     */
    private boolean currentTweetIsNotExhausted() {
        try {
            return currentTweet.get("text").toString().length() > 0;
        } catch (NullPointerException e) {
            System.out.println("Current Tweet Is Null!");
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Gets the first character from the current Tweet, trims the string and replaces
     * the current Tweet. Increments characters used.
     * @return First character from current Tweet
     */
    private String getTweetChar() {
        try {
            //Get The Text Element From Current Tweet (Status)
            String text = currentTweet.get("text").toString();
            System.out.println(text);

            //Increment Characters used for data analysis
            CHARS_USED++;

            //If Tweet length is 1, we skip this statement and just return
            //the first character. It will catch as exhausted on the next loop.
            if (currentTweet.get("text").toString().length() > 1) {
                //Since we're going to remove the first character for parsing,
                //take from the 2nd char to the end and store in newText.
                String newText = text.substring(1, text.length());
                currentTweet.remove("text"); //Remove text element from current status
                currentTweet.put("text", newText); //Replace with the newText

                //Return the first character from the current tweets for parsing
                return text.substring(0, 1);
            } else {
                //If we're on the last char of the Tweet (length = 1) then we
                //need to set the currentTweet text element to nothing
                currentTweet.remove("text");
                currentTweet.put("text", ""); //String of length 0

                //Only one character left, return it.
                return text;
            }
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
    private Double createDouble(Integer s1, Integer s2) {
        //Sanitise Integers
        s1 = Math.abs(s1);
        s2 = Math.abs(s2);

        try {
            if (s1 > 10 && s2 > 10) {
                //If both integers are 2 digits, take first digit from each and concat into double.
                String num1 = s1.toString().substring(0, 1);
                String num2 = s2.toString().substring(0, 1);
                return Double.parseDouble(Integer.parseInt(num1) + "." + Integer.parseInt(num2));
            } else if (s1 > 10) {
                //If s1 is 2 digits, split them and parse as double. I.e 13 would become 1.3
                String num1 = s1.toString().substring(0, 1);
                String num2 = s1.toString().substring(1, 2);
                return Double.parseDouble(Integer.parseInt(num1) + "." + Integer.parseInt(num2));
            } else if (s2 > 10) {
                //Same here but for s2
                String num1 = s2.toString().substring(0, 1);
                String num2 = s2.toString().substring(1, 2);
                return Double.parseDouble(Integer.parseInt(num1) + "." + Integer.parseInt(num2));
            }
        } catch (NumberFormatException e) {
            System.out.println("createDouble() parsing error. Integer 1: " + s1 + ", Integer 2: " + s2);
            e.printStackTrace();
        }

        //If Both < 10, just concat into Double
        return Double.parseDouble(s1 + "." + s2);
    }

    /**
     * Creates an angle in degress  (0 - 360 deg) from two integers
     * @param i1 Integer between 0 and 25
     * @param i2 Integer between 0 and 25
     * @return Angle (Degrees) between 0 and 360.
     */
    private Integer createAngle(Integer i1, Integer i2) {
        //Sanitise Integers
        i1 = Math.abs(i1);
        i2 = Math.abs(i2);

        String intString = i1.toString() + i2.toString();
        Integer concatInt = Integer.parseInt(intString);
        if (concatInt <= 360) {
            return concatInt;
        }

        //Max concat Int is if i1 and i2 were 25. So it would be 2525
        //2525 / 360 = 7.013888888888889. So we divide by that to make sure always < 360
        return  Math.round(concatInt / 7.013888888888889f);
    }

    /**
     * Returns -1 (Anti-Clockwise) if the supplied number is between 0 and 12.
     * Returns 1 (Clockwise) if the supplied number is between 13 and 25.
     * @param num Parsed Tweet Character (Integer 0-25)
     * @return Integer representing rotational direction
     */
    private Integer createDirection(Integer num) {
        //Sanitise Integer
        num = Math.abs(num);

        if (num >= 0 && num <= 12) {
            return -1;
        }
        return 1;
    }

    private Integer createMovement(Integer mv1, Integer mv2) {
        try {
            //Sanitise Integers
            mv1 = Math.abs(mv1);
            mv2 = Math.abs(mv2);

            String moveString = mv1.toString() + mv2.toString();
            Integer concatInt = Integer.parseInt(moveString);

            if (concatInt < 1000) {
                return concatInt;
            }

            //Max concat Int is if mv1 and mv2 were 25. so it would be 2525
            //2525 / 1000 = 2.525. So we divide by that to make sure always < 1000
            return Math.round(concatInt / 2.525f);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            //Default to 250
            return 250;
        }
    }

    private void updateCurrentTweet() {
        if (currentTweetArray != null && currentTweetArray.size() > 0) {
            currentTweet = currentTweetArray.remove(0);

            TweetSanitiser ts = new TweetSanitiser(currentTweet.get("text").toString());
            currentTweet.put("text", ts.extractLetters());

            TWEETS_USED++;
        } else {
            System.out.println("Current Tweet Array Is Empty!");
        }
    }

    private boolean allValuesValidated() {
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

    public void invalidateAllValues() {
        //Angles are 0 - 360 so set 999
        FIRE_POWER = -1.0;
        ROTATE_DIRECTION = 99;
        ROTATE = 999;
        MOVE_UP = -1;
        MOVE_RIGHT = -1;
        MOVE_DOWN = -1;
        MOVE_LEFT = -1;
        ROTATE_GUN_DIRECTION = 99;
        ROTATE = 999;
        ROTATE_SCANNER = 999;
        ROTATE_SCANNER_DIRECTION = 99;
    }

    public void setUSER(String USER) {
        this.USER = USER;
    }

    /**
     * Robot Firepower. Min 0.1, Max 3.0
     * @param FIRE_POWER Firepower (0.1 - 3.0)
     */
    private void setFIRE_POWER(double FIRE_POWER) {
        this.FIRE_POWER = Double.parseDouble(dp1.format(FIRE_POWER));
    }

    private void setROTATE(Integer ROTATE) {
        this.ROTATE = ROTATE;
    }

    public Integer getMOVE_UP() {
        return MOVE_UP;
    }

    private void setMOVE_UP(Integer MOVE_UP) {
        this.MOVE_UP = MOVE_UP;
    }

    public Integer getMOVE_RIGHT() {
        return MOVE_RIGHT;
    }

    private void setMOVE_RIGHT(Integer MOVE_RIGHT) {
        this.MOVE_RIGHT = MOVE_RIGHT;
    }

    public Integer getMOVE_DOWN() {
        return MOVE_DOWN;
    }

    private void setMOVE_DOWN(Integer MOVE_DOWN) {
        this.MOVE_DOWN = MOVE_DOWN;
    }

    public Integer getMOVE_LEFT() {
        return MOVE_LEFT;
    }

    private void setMOVE_LEFT(Integer MOVE_LEFT) {
        this.MOVE_LEFT = MOVE_LEFT;
    }

    public Integer getROTATE_DIRECTION() {
        return ROTATE_DIRECTION;
    }

    private void setROTATE_DIRECTION(Integer ROTATE_DIRECTION) {
        this.ROTATE_DIRECTION = ROTATE_DIRECTION;
    }

    public Integer getROTATE_GUN() {
        return ROTATE_GUN;
    }

    private void setROTATE_GUN(Integer ROTATE_GUN) {
        this.ROTATE_GUN = ROTATE_GUN;
    }

    public Integer getROTATE_GUN_DIRECTION() {
        return ROTATE_GUN_DIRECTION;
    }

    private void setROTATE_GUN_DIRECTION(Integer ROTATE_GUN_DIRECTION) {
        this.ROTATE_GUN_DIRECTION = ROTATE_GUN_DIRECTION;
    }

    public double getFIRE_POWER() {
        return FIRE_POWER;
    }

    Double getRobotX() {
        return robotX;
    }

    Double getRobotY() {
        return robotY;
    }

    public Document getCurrentTweet() {
        return currentTweet;
    }

    public ArrayList<Document> getAllTweets() {
        return allTweets;
    }

    public String getUSER() {
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

    void setRobotX(Double robotX) {
        this.robotX = robotX;
    }

    void setRobotY(Double robotY) {
        this.robotY = robotY;
    }

    Integer getROTATE_SCANNER() {
        return ROTATE_SCANNER;
    }

    private void setROTATE_SCANNER(Integer ROTATE_SCANNER) {
        this.ROTATE_SCANNER = ROTATE_SCANNER;
    }

    Integer getROTATE_SCANNER_DIRECTION() {
        return ROTATE_SCANNER_DIRECTION;
    }

    private void setROTATE_SCANNER_DIRECTION(Integer ROTATE_SCANNER_DIRECTION) {
        this.ROTATE_SCANNER_DIRECTION = ROTATE_SCANNER_DIRECTION;
    }

    public TweetReader getTr() {
        return tr;
    }

    /*---------------------------------------------------
    /*--------------- VALIDATION METHODS ----------------
    /*-------------------------------------------------*/

    private boolean FIRE_POWERisValid() {
        return FIRE_POWER >= 0.0 && FIRE_POWER <= 3.0;
    }

    private boolean ROTATE_DIRECTIONisValid(Integer dir) {
        return dir == 1 || dir == -1;
    }

    private boolean ROTATEisValid(Integer angle) {
        return angle >= -360 && angle <= 360;
    }

    private boolean MOVEVEMENTisValid(Integer movement) {
        return movement >= 0 && movement < 1000;
    }

}
