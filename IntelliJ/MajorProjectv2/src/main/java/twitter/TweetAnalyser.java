package twitter;

import com.vdurmont.emoji.EmojiParser;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * -----------------------------------------------------------------------------------------------------------
 * This class analyses the 'text' attributes from all the Twitter Status objects in the MongoDB Atlas Cluster.
 * -----------------------------------------------------------------------------------------------------------
 * @author Thomas Plumpton
 * @version 1.0.0
 */
public class TweetAnalyser {
    private TweetReader tr = new TweetReader();
    private int[] alphabet = new int[26];
    private float[] percentages = new float[26];
    private float totalPercentage = 0.0f;
    private String[] alphabetLetters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private List<String> emoji = new ArrayList<>();
    private int avgLength = 0;
    private int minLength = 280;
    private int maxLength = 0;
    private int tweetCount;
    private int totalCharacters = 0;

    /**
     * Calculates several statistics of the Tweets from the MongoDB Atlas Cluster. Including;
     * - Character Statistics: Frequency of Use + Relative Percentage
     * - Minimum Tweet Length
     * - Average Tweet Length
     * - Maximum Tweet Length
     * - Frequency of Emoji Usage
     */
    void analyse() {
        ArrayList<String> tweets = tr.getAllValuesByKey("text");
        tweetCount = tweets.size();
        System.out.println("Counting Characters From " + tweets.size() + " Tweets.");
        for (String tweet : tweets) {
            //Count Emoji, then add to list
            List<String> emojiInCurrentTweet = EmojiParser.extractEmojis(tweet);
            if (emojiInCurrentTweet != null) {
                emoji.addAll(emojiInCurrentTweet);
            }

            int tweetLength = tweet.length();

            //Add Tweet Length
            avgLength += tweet.length();

            //Set Min Length
            if (tweetLength < minLength) {
                minLength = tweetLength;
            }

            //Set Max Length
            if (tweetLength > maxLength) {
                maxLength = tweetLength;
            }

            //Sanitise Tweet Before Counting Characters
            TweetSanitiser ts = new TweetSanitiser(tweet);
            String cleanTweet = ts.extractLetters();
            //Count Characters
            for (int i = 1; i < cleanTweet.length(); i++) {
                char c = cleanTweet.charAt(i);
                int value = (int) c;
                if (value >= 97 && value <= 122) {
                    alphabet[c - 'a']++;
                }
            }
        }

        //Calculate Percentages
        int total = 0;
        for (int num : alphabet) {
            total += num;
        }

        for (int i = 0; i < alphabet.length; i++) {
            //ASCII Value. Letter a starts at 97
            float percentage = (alphabet[i] * 100.0f) / total;
            percentages[i] = percentage;
            totalPercentage += percentage;
        }

        //Calculate Avg Tweet Length
        avgLength = avgLength / tweets.size();
    }

    /**
     * Prints the results of analyse() to the console.
     */
    void printResults() {
        for (int num : alphabet) {
            totalCharacters += num;
        }

        System.out.println("There are " + totalCharacters + " characters in total.\n");

        for (int i = 0; i < percentages.length; i++) {
            System.out.println(String.valueOf(alphabetLetters[i]).toUpperCase() + ": " + alphabet[i] + " (" + percentages[i] + "%)");
        }

        System.out.println("\nOverall Percentage is " + totalPercentage + "% (Some accuracy lost in decimals)");
        System.out.println("Tweet Statistics:");
        System.out.println("MIN: " + minLength + "/280 characters.");
        System.out.println("AVG: " + avgLength + "/280 characters.");
        System.out.println("MAX: " + maxLength + "/280 characters.");
        System.out.println("EMOJI: " + emoji.size() + " in total.");
    }

    /**
     * Saves the results from analyse() into the MongoDB Atlas Cluster
     */
    private void saveToDatabase() {
        MongoConnection mc = new MongoConnection("twitter", "analysis");
        Document stats = new Document();
        TweetReader tr = new TweetReader();
        TweetHandler th = new TweetHandler();
        stats.put("tweetCount", tweetCount);
        stats.put("characterCount", totalCharacters);
        stats.put("min", minLength);
        stats.put("avg", avgLength);
        stats.put("max", maxLength);
        stats.put("emoji", emoji);
        stats.put("emojiCount", emoji.size());
        ArrayList<Document> jsonArray = new ArrayList<>(26);
        for (int i = 0; i < percentages.length; i++) {
            Document json = new Document();
            json.put("letter", alphabetLetters[i].toUpperCase());
            json.put("count", alphabet[i]);
            json.put("percentage", percentages[i]);
            jsonArray.add(json);
        }
        stats.put("alpha", jsonArray);
        stats.put("totalPercentage", totalPercentage);
        stats.put("users", th.getUserDetails(tr.getAllUsernames()));
        mc.insertDocument(stats);
    }

    private void saveUsersToDatabase() {
        MongoConnection mc = new MongoConnection("twitter", "users");
        ArrayList<String> users = tr.getAllUsernames();
        for (String user : users) {
            Document doc = new Document();
            doc.put("name", user);
            mc.insertDocument(doc);
        }
    }

    public static void main(String[] args) {
        TweetAnalyser ta = new TweetAnalyser();
        ta.analyse();
        ta.printResults();
        //ta.saveToDatabase();

        //ta.saveUsersToDatabase();
    }
}
