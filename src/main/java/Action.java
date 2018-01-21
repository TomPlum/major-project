public class Action {
    public static void main(String[] args) {
        MongoConnection conn = new MongoConnection("twitter", "tweets");
        TweetReader tr = new TweetReader();
        for(String str : tr.getAllUsernames()) {
            System.out.println(str);
        }
        conn.removeDocumentsByKey("user", "Gordon Ramsay");
    }
}
