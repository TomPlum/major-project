import java.util.ArrayList;

public class TweetAnalyser {
    private TweetReader tr = new TweetReader();
    private int[] alphabet = new int[26];

    private void countLetters() {
        ArrayList<String> tweets = tr.getAllValuesByKey("text");
        System.out.println("Counting Characters From " + tweets.size() + " Tweets.");
        for (String tweet : tweets) {
            TweetSanitiser ts = new TweetSanitiser(tweet);
            String cleanTweet = ts.extractLetters();
            for (int i = 1; i < cleanTweet.length(); i++) {
                char c = cleanTweet.charAt(i);
                int value = (int) c;
                if (value >= 97 && value <= 122) {
                    alphabet[c - 'a']++;
                }
            }
        }
    }

    private void printResults() {
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
    }

    public static void main(String[] args) {
        TweetAnalyser ta = new TweetAnalyser();
        ta.countLetters();
        ta.printResults();
    }
}
