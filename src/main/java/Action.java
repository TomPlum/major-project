import twitter.TweetAnalyser;

public class Action {
    public static void main(String[] args) {
        /*
        connection.MongoConnection conn = new connection.MongoConnection("twitter", "tweets");
        twitter.TweetReader tr = new twitter.TweetReader();
        for(String str : tr.getAllUsernames()) {
            System.out.println(str);
        }
        conn.removeDocumentsByKey("user", "Donald J. Trump");

        twitter.TweetReader tr = new twitter.TweetReader();
        Document doc = tr.getTweetByTweetId(950648597221138432L);
        String text = (String) doc.get("text");
        List<String> emoji = EmojiParser.extractEmojis(text);
        System.out.println(emoji.toString());
        System.out.println(emoji.size());

       */

        TweetAnalyser ta = new TweetAnalyser();
        ta.analyse();
        ta.printResults();
    }
}
