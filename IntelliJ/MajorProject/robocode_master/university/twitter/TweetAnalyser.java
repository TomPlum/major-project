package university.twitter;

import com.vdurmont.emoji.EmojiParser;
import java.util.ArrayList;
import java.util.List;

public class TweetAnalyser {
    private TweetReader tr = new TweetReader();
    private int[] alphabet = new int[26];
    private List<String> emoji = new ArrayList<>();
    private int avgLength = 0;
    private int minLength = 280;
    private int maxLength = 0;


    public void analyse() {
        ArrayList<String> tweets = tr.getAllValuesByKey("text");
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

        //Calculate Avg Tweet Length
        avgLength = avgLength / tweets.size();
    }

    public void printResults() {
        int total = 0;

        for (int num : alphabet) {
            total += num;
        }

        System.out.println("There are " + total + " characters in total.\n");

        float totalPercentage = 0.0f;
        for (int i = 0; i < alphabet.length; i++) {
            //ASCII Value. Letter a starts at 97
            char c = (char) (i+97);
            String letter = Character.toString(c).toUpperCase();
            float percentage = (alphabet[i] * 100.0f) / total;
            totalPercentage += percentage;
            System.out.println(letter + ": " + alphabet[i] + " (" + percentage + "%)");
        }

        System.out.println("\nOverall Percentage is " + totalPercentage + "% (Some accuracy lost in decimals)");
        System.out.println("Tweet Statistics:");
        System.out.println("MIN: " + minLength + "/280 characters.");
        System.out.println("AVG: " + avgLength + "/280 characters.");
        System.out.println("MAX: " + maxLength + "/280 characters.");
        System.out.println("EMOJI: " + emoji.size() + " in total.");
    }

    public static void main (String args[]) {
        TweetAnalyser ta = new TweetAnalyser();
        ta.analyse();
        ta.printResults();
    }
}
