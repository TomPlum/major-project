package controller;

import org.bson.Document;
import robocode.BattleResults;
import twitter.MongoConnection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BattleResultLogger {
    private MongoConnection conn = new MongoConnection("twitter", "results");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private LocalDateTime now = LocalDateTime.now();

    public void saveResultsToDatabase(BattleResults[] results) {
        Document finalResults = new Document();
        ArrayList<Document> formattedResults = new ArrayList<>();
        System.out.println("Battle results:");
        for (robocode.BattleResults result : results) {
            Document doc = new Document();
            doc.put("name", result.getTeamLeaderName());
            doc.put("score", result.getScore());
            doc.put("bullet_damage", result.getBulletDamage());
            doc.put("bullet_damage_bonus", result.getBulletDamageBonus());
            doc.put("firsts", result.getFirsts());
            doc.put("seconds", result.getSeconds());
            doc.put("thirds", result.getThirds());
            doc.put("last_survivor_bonus", result.getLastSurvivorBonus());
            doc.put("ram_damage", result.getRamDamage());
            doc.put("ram_damage_bonus", result.getRamDamageBonus());
            doc.put("rank", result.getRank());
            doc.put("survival", result.getSurvival());
            formattedResults.add(doc);
        }
        finalResults.put("date", dtf.format(now));
        finalResults.put("results", formattedResults);
        conn.insertDocument(finalResults);
    }
}
